package org.aiit.mes.craft.domain.dao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.aiit.mes.common.constant.CraftStepTypeEnum;
import org.aiit.mes.common.constant.MaterialTransferEnum;
import org.aiit.mes.craft.domain.dao.entity.CraftFlowNodeEntity;
import org.aiit.mes.craft.domain.dao.mapper.ICraftFlowNodeMapper;
import org.aiit.mes.craft.domain.dao.service.ICraftNodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @param
 * @Author heyu
 * @description
 * @return
 * @throws
 */
@Service
public class CraftFlowNodeServiceImpl extends ServiceImpl<ICraftFlowNodeMapper, CraftFlowNodeEntity> implements
        ICraftNodeService {

    private static final Logger logger = LoggerFactory.getLogger(CraftFlowNodeServiceImpl.class);

    @Override
    public boolean removeById(Serializable id) {
        boolean ret = super.removeById(id);
        logger.info("delete craft-flow-node by id:{}, result:{}", id, ret);
        return ret;
    }

    /**
     * 查询flow包含的所有node
     *
     * @param flowId
     * @return
     */
    @Override
    public List<CraftFlowNodeEntity> listNodeByFlowId(Long flowId) {
        QueryWrapper<CraftFlowNodeEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("flow_id", flowId);
        return this.list(wrapper);
    }

    /**
     * 按照flow id删除node
     *
     * @return
     */
    @Override
    public boolean deleteNodesByFlowId(Long flowId) {
        return this.remove(new QueryWrapper<CraftFlowNodeEntity>().eq("flow_id", flowId));
    }

    @Override
    public List<Long> listFlowIdsByStockInMaterialId(Long materialId) {
        QueryWrapper<CraftFlowNodeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DISTINCT flow_id").orderByAsc("flow_id").lambda()
                    .eq(CraftFlowNodeEntity::getType, CraftStepTypeEnum.MATERIAL_TRANSFER)
                    .in(CraftFlowNodeEntity::getTransferType, MaterialTransferEnum.OUTPUT_TYPE_LIST)
                    .eq(CraftFlowNodeEntity::getMaterialId, materialId);
        List<Long> li = this.list(queryWrapper).stream().map(CraftFlowNodeEntity::getFlowId).collect(
                Collectors.toList());

        logger.info("query flow by material id:{}, ret:{}", materialId, li);
        return li;
    }

    @Override
    public List<CraftFlowNodeEntity> queryMaterialNodesByFlowId(Long flowId) {
        List<CraftFlowNodeEntity> materialNodes = this.list(new QueryWrapper<CraftFlowNodeEntity>()
                                                                    .lambda()
                                                                    .eq(CraftFlowNodeEntity::getFlowId, flowId)
                                                                    .eq(CraftFlowNodeEntity::getType,
                                                                        CraftStepTypeEnum.MATERIAL_TRANSFER));
        materialNodes = Optional.ofNullable(materialNodes).orElse(Collections.emptyList());
        logger.info("flow id:{}, material nodes:{}", flowId, materialNodes);
        return materialNodes;
    }
}
