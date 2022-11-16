package org.aiit.mes.craft.domain.dao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.aiit.mes.craft.domain.dao.entity.CraftFlowRelationEntity;

/**
 * @param
 * @Author heyu
 * @description
 * @return
 * @throws
 */
public interface ICraftFlowRelationService extends IService<CraftFlowRelationEntity> {

    /**
     * 查询flowId 对应的relation
     *
     * @param flowId
     * @return
     */
    public CraftFlowRelationEntity getRelationByFlowId(long flowId);

    /**
     * 删除flow id对应的relation
     *
     * @return
     */
    public boolean deleteRelationByFlowId(Long flowId);
}
