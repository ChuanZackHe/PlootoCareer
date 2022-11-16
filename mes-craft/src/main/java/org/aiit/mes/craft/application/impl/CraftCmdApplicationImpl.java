package org.aiit.mes.craft.application.impl;

import org.aiit.mes.common.primitive.LongId;
import org.aiit.mes.craft.application.ICraftCmdApplication;
import org.aiit.mes.craft.domain.aggregate.FlowAgg;
import org.aiit.mes.craft.domain.aggregate.FlowComponentAgg;
import org.aiit.mes.craft.domain.aggregate.factory.FlowAggFactory;
import org.aiit.mes.craft.domain.dto.*;
import org.aiit.mes.craft.domain.repository.FlowComponentRepo;
import org.aiit.mes.craft.domain.repository.FlowRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName CraftApplicationImpl
 * @Description
 * @createTime 2021.09.02 18:00
 */
@Service
public class CraftCmdApplicationImpl implements ICraftCmdApplication {

    private static final Logger logger = LoggerFactory.getLogger(CraftCmdApplicationImpl.class);

    @Resource
    private FlowRepo flowRepo;

    @Resource
    private FlowComponentRepo componentRepo;

    private FlowComponentAgg createCmdToAgg(CraftComponentCreateCmd createCmd) {
        return componentRepo.createCmdToAgg(createCmd);
    }

    @Override
    public FlowComponentAgg updateComponent(CraftComponentUpdateCmd updateCmd) {
        FlowComponentAgg agg = getComponentById(updateCmd.getId());
        agg.loadUpdateCmd(updateCmd);
        return agg.update();
    }

    @Override
    public FlowComponentAgg saveNewComponent(CraftComponentCreateCmd createCmd) {
        return createCmdToAgg(createCmd).save();
    }

    @Override
    public FlowComponentAgg getComponentById(Long id) {
        return componentRepo.getComponent(id);
    }

    @Override
    public boolean deleteComponentById(Long id) {
        FlowComponentAgg componentAgg = getComponentById(id);
        return componentRepo.deleteComponent(componentAgg);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteFlowById(LongId flowId) {
        return flowRepo.getFlowAgg(flowId).delete();
    }

    @Override
    public FlowAgg showFlowById(LongId flowId) {
        return flowRepo.getFlowAgg(flowId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FlowAgg saveNewFlow(CraftFlowCreateCmd flowCreateCmd) {
        return FlowAggFactory.toCreateFlow(flowCreateCmd.toEntity()).save();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FlowAgg updateFlow(CraftFlowUpdateCmd flowUpdateCmd) {
        return FlowAggFactory.toUpdateFlow(flowUpdateCmd.toEntity()).update();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FlowAgg copyFlow(CraftFlowCopyCmd flowCopyCmd) {
        FlowAgg srcFlow = showFlowById(LongId.from(flowCopyCmd.getId()));
        return srcFlow.copyWithNewName(flowCopyCmd.getName());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public FlowAgg updateFlowGraph(CraftFlowGraphUpdateCmd graphUpdateCmd) {
        return flowRepo.getFlowAgg(new LongId(graphUpdateCmd.getId()))
                       .toUpdateFlow(graphUpdateCmd.toEntity())
                       .toUpdateNodes(graphUpdateCmd.extractNodeEntity())
                       .toUpdateRelations(graphUpdateCmd.extractRelationEntity())
                       .updateGraph();
    }
}
