package org.aiit.mes.craft.domain.aggregate;

import lombok.Getter;
import org.aiit.mes.common.iface.IAggregateCRUD;
import org.aiit.mes.common.iface.IToRepresent;
import org.aiit.mes.common.primitive.LongId;
import org.aiit.mes.common.util.PropertyCopyUtil;
import org.aiit.mes.craft.domain.dao.entity.CraftFlowEntity;
import org.aiit.mes.craft.domain.dao.entity.CraftFlowNodeEntity;
import org.aiit.mes.craft.domain.dao.entity.CraftFlowRelationEntity;
import org.aiit.mes.craft.domain.dto.CraftFlowRepresent;
import org.aiit.mes.craft.domain.dto.CraftNodeRepresent;
import org.aiit.mes.craft.domain.repository.FlowRepo;
import org.aiit.mes.craft.domain.util.FlowGraphUtil;
import org.aiit.mes.craft.domain.vo.FlowNodeRelationV;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static org.aiit.mes.craft.domain.util.FlowGraphUtil.calculateFlowNodeCoordinate;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName FlowAgg
 * @Description 流程聚合根, 包含流程、流程节点、节点关系
 * @createTime 2021.09.02 09:53
 */
public class FlowAgg implements IToRepresent, IAggregateCRUD {

    private static final Logger logger = LoggerFactory.getLogger(FlowAgg.class);

    @Getter
    private Long flowId;

    @Getter
    private CraftFlowEntity flow;

    @Getter
    private List<CraftFlowNodeEntity> nodes;

    @Getter
    private CraftFlowRelationEntity relations;

    private FlowRepo flowRepo;

    /**
     * 待更新flow
     */
    @Getter
    private CraftFlowEntity toUpdateFlow;

    @Getter
    private List<CraftFlowNodeEntity> toUpdateNodes = Collections.emptyList();

    private Set<String> toUpdateNodeIds = Collections.emptySet();

    private Set<String> oldNodeIds = Collections.emptySet();

    @Getter
    private CraftFlowRelationEntity toUpdateRelations;

    public FlowAgg(CraftFlowEntity flow, List<CraftFlowNodeEntity> flowNodes,
                   CraftFlowRelationEntity relation, FlowRepo flowRepo) {
        this.flow = flow;
        this.nodes = Optional.ofNullable(flowNodes).orElse(Collections.emptyList());
        this.relations = relation;
        this.flowId = flow.getId();
        this.flowRepo = flowRepo;
    }

    @Override
    public CraftFlowRepresent toRepresent() {
        calculateFlowNodeCoordinate(this);
        return this.flow.toRepresent()
                        .nodes(CraftNodeRepresent.fromEntity(this.nodes))
                        .relations(this.relations.toRepresent());
    }

    @Override
    public FlowAgg save() {
        LongId id = flowRepo.saveNewFlow(this);
        return flowRepo.getFlowAgg(id);
    }

    @Override
    public Boolean delete() {
        return flowRepo.deleteFlow(this);
    }

    @Override
    public FlowAgg update() {
        //TODO: 更新校验：对比flowEntity和toUpdateFlowEntity
        flowRepo.updateFlow(this);
        return flowRepo.getFlowAgg(new LongId(this.flowId));
    }

    /**
     * 更新流程图
     *
     * @return
     */
    public FlowAgg updateGraph() {
        // todo 业务校验：
        // todo 1. 验证relation里面的节点和nodes的节点一致。

        // 预留，用于后续同时更新flow信息
        if (Objects.nonNull(toUpdateFlow)) {
            flowRepo.updateFlow(this);
        }
        // 比较并更新 nodes
        doChangeNodes();
        // 更新 relations;
        doChangeRelations();
        // 查询返回
        FlowAgg updatedFLowAgg = flowRepo.getFlowAgg(new LongId(this.flowId));
        // 校验更新后的图合规
        FlowGraphUtil.verifyFlowGraph(updatedFLowAgg);

        return updatedFLowAgg;
    }

    private void doChangeRelations() {
        flowRepo.updateFlowRelationById(this);
    }

    private void doChangeNodes() {
        flowRepo.batchDeleteFlowNodes(getToDeleteNodeIdList());
        flowRepo.batchInsertFlowNodes(getToInsertNodeList());
        flowRepo.batchUpdateFlowNodes(getToUpdateNodeList());
    }

    /**
     * 比较原nodes和新nodes，得到要删除的原nodes ids
     *
     * @return
     */
    private List<String> getToDeleteNodeIdList() {
        // 待更新为空，删除全部
        if (CollectionUtils.isEmpty(this.toUpdateNodes)) {
            return this.nodes.stream().map(
                    CraftFlowNodeEntity::getId).collect(
                    Collectors.toList());
        }
        // 待更新非空
        // 老nodes存在，在新nodes中不存在，表示要做删除操作
        return this.nodes.stream().map(CraftFlowNodeEntity::getId).filter(id -> !this.toUpdateNodeIds.contains(id))
                         .collect(
                                 Collectors.toList());
    }

    /**
     * 获取待插入node列表
     * 新nodes存在，老nodes中不存在的，即为待插入数据
     *
     * @return
     */
    private List<CraftFlowNodeEntity> getToInsertNodeList() {
        // 老nodes中不存在，在新nodes中存在，表示要做insert操作
        return this.toUpdateNodes.stream().filter(node -> !this.oldNodeIds.contains(node.getId()))
                                 .collect(Collectors.toList());
    }

    /**
     * 获取待更新node列表
     * 同时存在于新、老nodes中的id相同的nodes，即为待插入数据
     *
     * @return
     */
    private List<CraftFlowNodeEntity> getToUpdateNodeList() {
        return this.toUpdateNodes.stream().filter(node -> this.oldNodeIds.contains(node.getId()))
                                 .collect(Collectors.toList());
    }

    /**
     * 刷新待更新的flow
     *
     * @param toUpdateFlow
     * @return
     */
    public FlowAgg toUpdateFlow(CraftFlowEntity toUpdateFlow) {
        if (Objects.isNull(toUpdateFlow)) {
            return this;
        }
        this.toUpdateFlow = toUpdateFlow;
        this.toUpdateFlow.setId(this.flowId);
        return this;
    }

    public FlowAgg toUpdateNodes(List<CraftFlowNodeEntity> nodes) {
        if (CollectionUtils.isEmpty(nodes)) {
            return this;
        }
        this.toUpdateNodes = nodes;

        // 触发图更新场景，同时触发ids的刷新，方便后续计算
        this.toUpdateNodeIds = nodes.stream().map(CraftFlowNodeEntity::getId).collect(Collectors.toSet());
        this.oldNodeIds = this.nodes.stream().map(CraftFlowNodeEntity::getId).collect(Collectors.toSet());

        // 同时填充node的flowId字段，该字段不对外部用户暴露
        this.toUpdateNodes.stream().forEach(node -> node.setFlowId(this.flowId));
        return this;
    }

    public FlowAgg toUpdateRelations(CraftFlowRelationEntity toUpdateRelations) {
        if (Objects.isNull(toUpdateRelations)) {
            return this;
        }
        this.toUpdateRelations = toUpdateRelations;
        this.toUpdateRelations.setId(this.relations.getId());
        this.toUpdateRelations.setFlowId(this.flow.getId());
        return this;
    }

    public FlowAgg copyWithNewName(String name) {
        this.prepareReplicate(name);
        FlowAgg newFlow = this.save();

        return newFlow.toUpdateNodes(this.getNodes())
                      .toUpdateRelations(this.getRelations())
                      .updateGraph();

    }

    /**
     * 准备复制：
     * 1. 重命名flowname\清空flowid
     * 2. 覆盖node id、node relations
     */
    private void prepareReplicate(String name) {
        this.flow.setId(null);
        this.flow.setName(name);

        // 老id：新id映射
        HashMap<String, String> nodeIdMap = new HashMap<>();
        List<CraftFlowNodeEntity> newNodes = this.nodes.stream().map(oldN -> {
            CraftFlowNodeEntity newN = PropertyCopyUtil.copyToClass(oldN, CraftFlowNodeEntity.class);
            newN.setId(UUID.randomUUID().toString());
            nodeIdMap.put(oldN.getId(), newN.getId());
            logger.info("copy old node {} to new node {}", oldN.getId(), newN.getId());
            return newN;
        }).collect(Collectors.toList());
        logger.info("copy {} nodes finish", newNodes.size());
        this.nodes = newNodes;

        // 刷新relations
        List<FlowNodeRelationV> newRelations = Optional.ofNullable(this.relations.getNodeRelations())
                                                       .orElse(Collections.emptyList())
                                                       .stream()
                                                       .map(oldR -> {
                                                           FlowNodeRelationV newR = FlowNodeRelationV.newRelation(
                                                                   nodeIdMap.get(oldR.getPreNode()),
                                                                   nodeIdMap.get(oldR.getNextNode()));
                                                           logger.info("copyt relation from {} to {}", oldR, newR);
                                                           return newR;
                                                       }).collect(Collectors.toList());
        logger.info("copy {} relations finish", newRelations.size());
        this.relations.setNodeRelations(newRelations);
    }
}
