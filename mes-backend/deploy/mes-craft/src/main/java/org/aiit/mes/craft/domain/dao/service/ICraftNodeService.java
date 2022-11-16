package org.aiit.mes.craft.domain.dao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.aiit.mes.craft.domain.dao.entity.CraftFlowNodeEntity;

import java.util.List;

/**
 * @param
 * @Author heyu
 * @description
 * @return
 * @throws
 */
public interface ICraftNodeService extends IService<CraftFlowNodeEntity> {

    public List<CraftFlowNodeEntity> listNodeByFlowId(Long flowId);

    /**
     * 按照flow id删除node
     *
     * @return
     */
    public boolean deleteNodesByFlowId(Long flowId);

    public List<Long> listFlowIdsByStockInMaterialId(Long materialId);

    /**
     * 根据flow id查询对应流程的物料输入输出节点
     *
     * @param flowId
     * @return
     */
    public List<CraftFlowNodeEntity> queryMaterialNodesByFlowId(Long flowId);
}
