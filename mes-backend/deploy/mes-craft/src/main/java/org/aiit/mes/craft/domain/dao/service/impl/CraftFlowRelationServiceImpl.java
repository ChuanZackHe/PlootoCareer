package org.aiit.mes.craft.domain.dao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.aiit.mes.craft.domain.dao.entity.CraftFlowRelationEntity;
import org.aiit.mes.craft.domain.dao.mapper.ICraftFlowRelationMapper;
import org.aiit.mes.craft.domain.dao.service.ICraftFlowRelationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * @param
 * @Author heyu
 * @description
 * @return
 * @throws
 */
@Service
public class CraftFlowRelationServiceImpl extends ServiceImpl<ICraftFlowRelationMapper, CraftFlowRelationEntity>
        implements
        ICraftFlowRelationService {

    private static final Logger logger = LoggerFactory.getLogger(CraftFlowRelationServiceImpl.class);

    @Override
    public boolean removeById(Serializable id) {
        boolean ret = super.removeById(id);
        logger.info("delete craft-flow-relation by id:{}, result:{}", id, ret);
        return ret;
    }

    /**
     * 查询flowId 对应的relation
     *
     * @param flowId
     * @return
     */
    @Override
    public CraftFlowRelationEntity getRelationByFlowId(long flowId) {
        return this.getOne(new QueryWrapper<CraftFlowRelationEntity>().eq("flow_id", flowId));
    }

    /**
     * 删除flow id对应的relation
     *
     * @return
     */
    @Override
    public boolean deleteRelationByFlowId(Long flowId) {
        return this.remove(new QueryWrapper<CraftFlowRelationEntity>().eq("flow_id", flowId));
    }
}
