package org.aiit.mes.craft.domain.aggregate.factory;

import org.aiit.mes.common.exception.ResourceNotFoundException;
import org.aiit.mes.common.primitive.LongId;
import org.aiit.mes.craft.domain.aggregate.FlowAgg;
import org.aiit.mes.craft.domain.dao.entity.CraftFlowEntity;
import org.aiit.mes.craft.domain.dao.service.ICraftFlowRelationService;
import org.aiit.mes.craft.domain.dao.service.ICraftFlowService;
import org.aiit.mes.craft.domain.dao.service.ICraftNodeService;
import org.aiit.mes.craft.domain.repository.FlowRepo;

import java.util.Collections;
import java.util.Optional;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName FlowAggFactory
 * @Description
 * @createTime 2021.09.07 16:14
 */
public class FlowAggFactory {

    private static FlowRepo flowRepo = null;

    private static ICraftFlowService craftFlowService;

    private static ICraftNodeService craftFlowNodeService;

    private static ICraftFlowRelationService craftFlowRelationService;

    public static void init(FlowRepo repository,
                            ICraftFlowService flowService,
                            ICraftNodeService nodeService,
                            ICraftFlowRelationService relationService) {
        flowRepo = repository;
        craftFlowService = flowService;
        craftFlowNodeService = nodeService;
        craftFlowRelationService = relationService;

    }

    public static FlowAgg getFlowAgg(LongId flowId) {
        CraftFlowEntity flow = craftFlowService.getById(flowId.getId());
        Optional.ofNullable(flow)
                .orElseThrow(() -> ResourceNotFoundException.newExp("flow", flowId.getId()));

        return new FlowAgg(flow,
                           Optional.ofNullable(craftFlowNodeService.listNodeByFlowId(flowId.getId())).orElse(
                                   Collections.emptyList()),
                           craftFlowRelationService.getRelationByFlowId(flowId.getId()),
                           flowRepo);
    }


    public static FlowAgg toCreateFlow(CraftFlowEntity toCreateEntity) {
        return new FlowAgg(toCreateEntity, null, null, flowRepo);
    }

    public static FlowAgg toUpdateFlow(CraftFlowEntity toUpdateEntity) {
        FlowAgg flowAgg = getFlowAgg(new LongId(toUpdateEntity.getId()));
        flowAgg.toUpdateFlow(toUpdateEntity);
        return flowAgg;
    }
}
