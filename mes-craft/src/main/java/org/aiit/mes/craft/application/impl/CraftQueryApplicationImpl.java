package org.aiit.mes.craft.application.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.aiit.mes.base.material.application.IBaseMaterialQueryApplication;
import org.aiit.mes.base.material.domain.dto.BaseMaterialQuery;
import org.aiit.mes.base.material.domain.entity.BaseMaterialEntity;
import org.aiit.mes.common.exception.ParamInvalidException;
import org.aiit.mes.common.page.BasePage;
import org.aiit.mes.common.primitive.LongId;
import org.aiit.mes.common.util.CommonUtils;
import org.aiit.mes.craft.application.ICraftQueryApplication;
import org.aiit.mes.craft.domain.aggregate.FlowAgg;
import org.aiit.mes.craft.domain.crafttree.CraftFlowTreeMap;
import org.aiit.mes.craft.domain.crafttree.CraftFlowTreeNode;
import org.aiit.mes.craft.domain.dao.entity.CraftComponentEntity;
import org.aiit.mes.craft.domain.dao.entity.CraftFlowEntity;
import org.aiit.mes.craft.domain.dao.service.ICraftComponentService;
import org.aiit.mes.craft.domain.dao.service.ICraftFlowService;
import org.aiit.mes.craft.domain.dao.service.ICraftNodeService;
import org.aiit.mes.craft.domain.dto.AnalyzeBomForProduceQuery;
import org.aiit.mes.craft.domain.dto.CraftComponentQuery;
import org.aiit.mes.craft.domain.dto.CraftFlowQuery;
import org.aiit.mes.craft.domain.primitive.MaterialId;
import org.aiit.mes.craft.domain.repository.FlowComponentRepo;
import org.aiit.mes.craft.domain.repository.FlowRepo;
import org.aiit.mes.craft.domain.vo.CraftBomTreeRepresentV;
import org.aiit.mes.warehouse.stock.application.IStockQueryApplication;
import org.aiit.mes.warehouse.stock.domain.dao.entity.StockSummaryEntity;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName CraftApplicationImpl
 * @Description
 * @createTime 2021.09.02 18:00
 */
@Service
public class CraftQueryApplicationImpl implements ICraftQueryApplication {

    private static final Logger logger = LoggerFactory.getLogger(CraftQueryApplicationImpl.class);

    @Resource
    private FlowRepo flowRepo;

    @Resource
    private FlowComponentRepo componentRepo;

    @Resource
    private ICraftComponentService craftComponentService;

    @Resource
    private ICraftFlowService craftFlowService;

    @Resource
    private ICraftNodeService craftNodeService;

    // todo: 调用base模块的application查询物料接口
    @Resource
    private IBaseMaterialQueryApplication baseMaterialQueryApplication;

    @Resource
    private IStockQueryApplication stockQueryApplication;

    @Override
    public Page<CraftComponentEntity> pageListComponents(CraftComponentQuery query) {
        logger.info("list flow-components by filter:{}", query);
        LambdaQueryWrapper<CraftComponentEntity> wrapper = CraftComponentQuery.getLambdaQuery(query);
        Page<CraftComponentEntity> ret = craftComponentService.getBaseMapper()
                                                              .selectPage(CraftComponentQuery.getPage(query), wrapper);
        logger.info("[success]list flow-components, size:{}", ret.getRecords().size());
        return ret;
    }

    @Override
    public Page<CraftFlowEntity> pageListFlowSummary(CraftFlowQuery query) {
        logger.info("list flow-summary by filter:{}", query);
        LambdaQueryWrapper<CraftFlowEntity> wrapper = BasePage.getLambdaQuery(query);
        // 传入了 输出物料id，则增加过滤 flow的id
        if (Optional.ofNullable(query).map(CraftFlowQuery::getOutputMaterialId).isPresent()) {
            List<Long> filterFlowIds = craftNodeService.listFlowIdsByStockInMaterialId(query.getOutputMaterialId());
            logger.info("find flow with output material id:{}, flow ids:{}", query.getOutputMaterialId(),
                        filterFlowIds);
            // 没有匹配的flow id，匹配flow id-1，返回空查询
            if (CollectionUtils.isEmpty(filterFlowIds)) {
                wrapper.eq(CraftFlowEntity::getId, -1);
            }
            else {
                wrapper.in(CraftFlowEntity::getId, filterFlowIds);
            }
        }

        Page<CraftFlowEntity> ret = craftFlowService.getBaseMapper().selectPage(CraftFlowQuery.getPage(query), wrapper);

        logger.info("[success]list flow-summary, size:{}", ret.getRecords().size());
        return ret;
    }

    @Override
    public FlowAgg showFlowById(LongId flowId) {
        return flowRepo.getFlowAgg(flowId);
    }

    @Override
    public CraftFlowTreeMap queryRawMaterialTree(AnalyzeBomForProduceQuery query) {
        logger.info("query material tree, query:{}", query);
        CraftFlowTreeMap tree = buildMaterialTree(query.getFlowId(), query.getMaterialId());
        tree.syncMaterialNum(query.getRequiredNum());
        return tree;
    }

    @Override
    public CraftFlowTreeMap queryFullProduceTree(AnalyzeBomForProduceQuery query) {
        logger.info("query full tree, query:{}", query);
        query.updateDefaultValue();

        CraftFlowTreeMap tree = buildFullTree(query.getFlowId(), query.getMaterialId());
        tree.syncMaterialNum(query.getRequiredNum());
        return tree;
    }

    @Override
    public CraftBomTreeRepresentV analyzeBomForProduce(AnalyzeBomForProduceQuery query) {
        logger.info("analyzing, query:{}", query);
        query.updateDefaultValue();
        // 开始组装树
        CraftFlowTreeMap tree = this.queryRawMaterialTree(query);

        // node包含的物料id
        List<Long> materialIds = tree.getAllStepsMap().values().stream().map(CraftFlowTreeNode::getMaterialId).map(
                MaterialId::getId).distinct().collect(
                Collectors.toList());
        logger.info("material contained in tree:{}", materialIds);
        // 查询物料信息

        List<BaseMaterialEntity> matInfos = this.queryMaterialInfosByIds(materialIds);
        logger.info("material info:{}", matInfos);
        // 查询库存信息
        // 根据开关 compareStock 进行控制，是否对比库存数量
        List<StockSummaryEntity> stockInfos = new ArrayList<>();
        if (BooleanUtils.isTrue(query.getCompareStock())) {
            stockInfos = this.queryStockInfoByMaterialIds(materialIds);
            logger.info("need compare stock, stock info:{}", stockInfos);
        }

        return CraftBomTreeRepresentV.convertFrom(tree, matInfos, stockInfos, query.getCompareStock());
    }

    @Override
    public List<BaseMaterialEntity> queryMaterialInfosByIds(List<Long> materialIds) {
        logger.info("material ids:{}", materialIds);
        BaseMaterialQuery materialQuery = new BaseMaterialQuery();
        materialQuery.setMaterialIds(materialIds);
        materialQuery.setSize(materialIds.size());
        List<BaseMaterialEntity> matInfos = baseMaterialQueryApplication.pageListBaseMaterial(materialQuery)
                                                                        .getRecords();
        return matInfos;
    }

    @Override
    public List<StockSummaryEntity> queryStockInfoByMaterialIds(List<Long> materialIds) {
        logger.info("material ids:{}", materialIds);
        List<StockSummaryEntity> stockInfos = stockQueryApplication.listStockSummarys(materialIds);
        return stockInfos;
    }

    /**
     * 完整树构建，查询flowAgg，得到所有nodes和relations，建立完整treeMap
     *
     * @param flowId
     * @param materialId
     * @return
     */
    private CraftFlowTreeMap buildFullTree(Long flowId, Long materialId) {
        try {
            logger.info("build tree by material id:{}, flow id:{}", materialId, flowId);
            FlowAgg flowAgg = flowRepo.getFlowAgg(LongId.from(flowId));
            List<CraftFlowTreeNode> matNodes = flowAgg.getNodes()
                                                      .stream().map(CraftFlowTreeNode::new)
                                                      .collect(Collectors.toList());
            MaterialId entryNodeMatId = new MaterialId(materialId);
            CraftFlowTreeMap treeMap = new CraftFlowTreeMap(matNodes, entryNodeMatId);
            // 重建relations
            treeMap.reconnectNodeByRelations(flowAgg.getRelations().getNodeRelations());

            if (!treeMap.needMergeSubTree()) {
                return treeMap;
            }

            logger.info("need merge subtree by nodes:{}", treeMap.getTempInputMap());
            // 递归建树并merge
            treeMap.getTempInputMap().values().forEach(node -> {
                Long subMaterialId = node.getMaterialId().getId();
                // 限制：线上流转物料只允许存在一条工艺流程输出该物料
                List<Long> flowIds = craftNodeService.listFlowIdsByStockInMaterialId(subMaterialId);
                Assert.notEmpty(flowIds, String.format("线上流转物料[%s]未找到对应的工艺流程", subMaterialId));
                CraftFlowTreeMap subTree = buildFullTree(flowIds.get(0), subMaterialId);
                treeMap.mergeSubTreeMap(subTree);
            });

            return treeMap;
        }
        catch (Exception e) {
            logger.error("build tree failed:{}", CommonUtils.getStackTrace(e));
            throw new ParamInvalidException(e.getMessage());
        }
    }

    /**
     * 物料树构建，单独查询物料树，得到物料 nodes，建立物料输入输出的treeMap
     *
     * @param flowId
     * @param materialId
     * @return
     */
    private CraftFlowTreeMap buildMaterialTree(Long flowId, Long materialId) {
        try {
            logger.info("build tree by material id:{}, flow id:{}", materialId, flowId);
            List<CraftFlowTreeNode> matNodes = craftNodeService.queryMaterialNodesByFlowId(flowId)
                                                               .stream()
                                                               .map(CraftFlowTreeNode::new)
                                                               .collect(Collectors.toList());
            MaterialId entryNodeMatId = new MaterialId(materialId);
            CraftFlowTreeMap treeMap = new CraftFlowTreeMap(matNodes, entryNodeMatId);

            if (!treeMap.needMergeSubTree()) {
                return treeMap;
            }

            logger.info("need merge subtree by nodes:{}", treeMap.getTempInputMap());
            // 递归建树并merge
            treeMap.getTempInputMap().values().forEach(node -> {
                Long subMaterialId = node.getMaterialId().getId();
                // 限制：线上流转物料只允许存在一条工艺流程输出该物料
                List<Long> flowIds = craftNodeService.listFlowIdsByStockInMaterialId(subMaterialId);
                Assert.notEmpty(flowIds, String.format("线上流转物料[%s]未找到对应的工艺流程", subMaterialId));
                CraftFlowTreeMap subTree = buildMaterialTree(flowIds.get(0), subMaterialId);
                treeMap.mergeSubTreeMap(subTree);
            });

            return treeMap;
        }
        catch (Exception e) {
            logger.error("build tree failed:{}", CommonUtils.getStackTrace(e));
            throw new ParamInvalidException(e.getMessage());
        }
    }
}
