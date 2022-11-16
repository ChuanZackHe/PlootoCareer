package org.aiit.mes.craft.domain.repository;

import org.aiit.mes.common.exception.OperationFailedException;
import org.aiit.mes.common.primitive.LongId;
import org.aiit.mes.craft.domain.aggregate.FlowAgg;
import org.aiit.mes.craft.domain.aggregate.factory.FlowAggFactory;
import org.aiit.mes.craft.domain.dao.entity.CraftFlowNodeEntity;
import org.aiit.mes.craft.domain.dao.entity.CraftFlowRelationEntity;
import org.aiit.mes.craft.domain.dao.service.ICraftFlowRelationService;
import org.aiit.mes.craft.domain.dao.service.ICraftFlowService;
import org.aiit.mes.craft.domain.dao.service.ICraftNodeService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.aiit.mes.common.constant.Constants.*;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName FlowRepository
 * @Description
 * @createTime 2021.09.02 10:30
 */
@Repository
public class FlowRepo implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(FlowRepo.class);

    @Resource
    private ICraftFlowService craftFlowDao;

    @Resource
    private ICraftNodeService craftFlowNodeDao;

    @Resource
    private ICraftFlowRelationService craftFlowRelationDao;

    public FlowAgg getFlowAgg(LongId flowId) {
        return FlowAggFactory.getFlowAgg(flowId);
    }

    public boolean deleteFlow(FlowAgg flowAgg) {
        logger.info("delete flow, id:{},", flowAgg.getFlowId());
        boolean ret = craftFlowRelationDao.deleteRelationByFlowId(flowAgg.getFlowId());
        OperationFailedException.checkDeleteSuccess(ret, FLOW, null);

        // nodes 为空，说明没有创建nodes条目
        if (!CollectionUtils.isEmpty(flowAgg.getNodes())) {
            ret = ret && craftFlowNodeDao.deleteNodesByFlowId(flowAgg.getFlowId());
            logger.info("delete flow nodes for flow:{}", flowAgg.getFlowId());
            OperationFailedException.checkDeleteSuccess(ret, FLOW_NODES, null);
        }
        ret = ret && craftFlowDao.removeById(flowAgg.getFlowId());
        OperationFailedException.checkDeleteSuccess(ret, FLOW_RELATION, null);
        logger.info("delete relation for flow:{}", flowAgg.getFlowId());
        return ret;
    }

    public int batchDeleteFlowNodes(List<String> nodeIdList) {
        if (CollectionUtils.isEmpty(nodeIdList)) {
            return 0;
        }
        logger.info("to batch delete nodes size:{}, ids:{}", nodeIdList.size(), nodeIdList);
        int ret = craftFlowNodeDao.getBaseMapper().deleteBatchIds(nodeIdList);
        logger.info("[success]batch delete nodes, num:{}", ret);
        return ret;
    }

    public boolean batchUpdateFlowNodes(List<CraftFlowNodeEntity> nodeEntityList) {

        if (CollectionUtils.isEmpty(nodeEntityList)) {
            return true;
        }
        logger.info("to batch update nodes size:{}, ids:{}", nodeEntityList.size(),
                    nodeEntityList.stream().map(CraftFlowNodeEntity::getId).collect(Collectors.toList()));
        boolean ret = craftFlowNodeDao.updateBatchById(nodeEntityList, 100);
        if (!ret) {
            throw OperationFailedException.updateFailed(FLOW_NODES, null);
        }
        logger.info("[success]batch update nodes, num:{}", nodeEntityList.size());
        return true;
    }

    public boolean batchInsertFlowNodes(List<CraftFlowNodeEntity> nodeEntityList) {
        logger.info("to batch insert nodes size:{}, ids:{}",
                    Optional.ofNullable(nodeEntityList).map(List::size).orElse(0),
                    nodeEntityList.stream().map(CraftFlowNodeEntity::getId).collect(Collectors.toList()));
        if (CollectionUtils.isEmpty(nodeEntityList)) {
            return true;
        }
        boolean ret = craftFlowNodeDao.saveBatch(nodeEntityList, 100);
        if (!ret) {
            throw OperationFailedException.insertFailed(FLOW_NODES, null);
        }
        logger.info("[success]batch insert nodes, num:{}", nodeEntityList.size());
        return true;
    }

    /**
     * 保存新流程，同步生成一条relations记录
     *
     * @param flowAgg
     * @return
     */
    public LongId saveNewFlow(FlowAgg flowAgg) {
        if (!craftFlowDao.save(flowAgg.getFlow())) {
            throw new RuntimeException("save flow failed");
        }
        LongId id = new LongId(flowAgg.getFlow().getId());
        // 同步创建relations记录
        if (!craftFlowRelationDao.save(
                CraftFlowRelationEntity.builder().flowId(id.getId()).build())) {
            throw new RuntimeException("save flow failed");
        }
        return id;
    }

    /**
     * 更新已有的flow， 根据flow-id查找
     *
     * @param flowAgg
     * @return
     */
    public void updateFlow(FlowAgg flowAgg) {
        if (!craftFlowDao.updateById(flowAgg.getToUpdateFlow())) {
            throw new RuntimeException(String.format("update flow %s failed", flowAgg.getFlowId()));
        }
    }


    public void updateFlowRelationById(FlowAgg flowAgg) {
        logger.info("to update flow relations：{}", flowAgg.getToUpdateRelations());
        if (Objects.isNull(flowAgg.getToUpdateRelations())) {
            return;
        }
        if (!craftFlowRelationDao.updateById(flowAgg.getToUpdateRelations())) {
            throw OperationFailedException.updateFailed(String.format("%s:%s", FLOW_RELATION,
                                                                      flowAgg.getToUpdateRelations().getId()),
                                                        null);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 传递repo到Factory
        FlowAggFactory.init(this, craftFlowDao, craftFlowNodeDao, craftFlowRelationDao);
    }
}
