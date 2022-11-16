package org.aiit.mes.craft.application;

import org.aiit.mes.common.primitive.LongId;
import org.aiit.mes.craft.domain.aggregate.FlowAgg;
import org.aiit.mes.craft.domain.aggregate.FlowComponentAgg;
import org.aiit.mes.craft.domain.dto.*;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName ICraftApplication
 * @Description
 * @createTime 2021.09.02 17:53
 */
public interface ICraftCmdApplication {

    /**
     * 组件相关
     */
    public FlowComponentAgg updateComponent(CraftComponentUpdateCmd updateCmd);

    public FlowComponentAgg saveNewComponent(CraftComponentCreateCmd createCmd);

    public FlowComponentAgg getComponentById(Long id);

    public boolean deleteComponentById(Long id);

    /**
     * 流程相关
     *
     * @param flowId
     * @return
     */
    public boolean deleteFlowById(LongId flowId);

    public FlowAgg saveNewFlow(CraftFlowCreateCmd flowCreateCmd);

    public FlowAgg updateFlow(CraftFlowUpdateCmd flowUpdateCmd);

    public FlowAgg copyFlow(CraftFlowCopyCmd flowCopyCmd);

    public FlowAgg updateFlowGraph(CraftFlowGraphUpdateCmd graphUpdateCmd);

    public FlowAgg showFlowById(LongId flowId);

}
