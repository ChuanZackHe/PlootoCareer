package org.aiit.mes.craft.domain.util;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.aiit.mes.common.constant.CraftOperationTypeEnum;
import org.aiit.mes.common.constant.CraftStepTypeEnum;
import org.aiit.mes.common.constant.MaterialTransferEnum;
import org.aiit.mes.common.exception.ParamInvalidException;
import org.aiit.mes.common.util.UUIDUtil;
import org.aiit.mes.craft.domain.crafttree.CraftFlowTreeMap;
import org.aiit.mes.craft.domain.crafttree.CraftFlowTreeNode;
import org.aiit.mes.craft.domain.dao.entity.CraftFlowNodeEntity;
import org.aiit.mes.craft.domain.vo.FlowNodeRelationV;
import org.aiit.mes.craft.domain.vo.MaterialInfoV;
import org.aiit.mes.craft.domain.vo.OperationInfoV;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.aiit.mes.common.constant.MaterialTransferEnum.STOCK_IN;
import static org.aiit.mes.common.constant.MaterialTransferEnum.STOCK_OUT;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName FlowGraphUtilTest
 * @Description
 * @createTime 2022.01.14 15:13
 */
@Slf4j
class FlowGraphUtilTest {

    @Test
    void verifyFlowGraph() {

        Double num = 10.0;
        MaterialInfoV m1 = MaterialInfoV.builder().id(RandomUtil.randomLong(1000, 5000)).num(num)
                                        .transferType(STOCK_IN).build();
        MaterialInfoV m2 = MaterialInfoV.builder().id(RandomUtil.randomLong(1000, 5000)).num(num)
                                        .transferType(STOCK_OUT).build();
        MaterialInfoV m3 = MaterialInfoV.builder().id(RandomUtil.randomLong(1000, 5000)).num(num)
                                        .transferType(MaterialTransferEnum.ON_THE_SCENE_IN).build();
        OperationInfoV op =
                OperationInfoV.builder().opType(CraftOperationTypeEnum.ASSEMBLE).outputMaterialId(m3.getId())
                              .outputMaterialNum(m3.getNum()).build();
        CraftFlowTreeNode n1 = new CraftFlowTreeNode(CraftFlowNodeEntity.builder().id(UUIDUtil.newUUID())
                                                                        .transferType(m1.getTransferType())
                                                                        .type(CraftStepTypeEnum.MATERIAL_TRANSFER)
                                                                        .materialId(m1.getId()).materialInfo(m1)
                                                                        .materialNum(m1.getNum())
                                                                        .build());
        CraftFlowTreeNode n2 = new CraftFlowTreeNode(CraftFlowNodeEntity.builder().id(UUIDUtil.newUUID())
                                                                        .transferType(m2.getTransferType())
                                                                        .type(CraftStepTypeEnum.MATERIAL_TRANSFER)
                                                                        .materialId(m2.getId()).materialInfo(m2)
                                                                        .materialNum(m2.getNum())
                                                                        .build());

        CraftFlowTreeNode n21 = new CraftFlowTreeNode(CraftFlowNodeEntity.builder().id(UUIDUtil.newUUID())
                                                                         .transferType(m2.getTransferType())
                                                                         .type(CraftStepTypeEnum.MATERIAL_TRANSFER)
                                                                         .materialId(m2.getId()).materialInfo(m2)
                                                                         .materialNum(m2.getNum())
                                                                         .build());

        CraftFlowTreeNode n3 = new CraftFlowTreeNode(CraftFlowNodeEntity.builder().id(UUIDUtil.newUUID())
                                                                        .transferType(m3.getTransferType())
                                                                        .type(CraftStepTypeEnum.MATERIAL_TRANSFER)
                                                                        .materialId(m3.getId()).materialInfo(m3)
                                                                        .materialNum(m3.getNum())
                                                                        .build());

        CraftFlowTreeNode o1 = new CraftFlowTreeNode(CraftFlowNodeEntity.builder().id(UUIDUtil.newUUID())
                                                                        .type(CraftStepTypeEnum.OPERATE)
                                                                        .opInfo(op)
                                                                        .build());
        // relation:
        // n2   n3
        //   \ /
        //    o1
        //    |
        //    n1
        List<FlowNodeRelationV> relations = new ArrayList<>();
        relations.add(FlowNodeRelationV.newRelation(n2.getCraftStepId().getId(), o1.getCraftStepId().getId()));
        relations.add(FlowNodeRelationV.newRelation(n3.getCraftStepId().getId(), o1.getCraftStepId().getId()));
        relations.add(FlowNodeRelationV.newRelation(o1.getCraftStepId().getId(), n1.getCraftStepId().getId()));

        CraftFlowTreeMap tree1 = new CraftFlowTreeMap(Arrays.asList(n1, n2, n3, o1));
        // reconnect
        tree1.reconnectNodeByRelations(relations);

        // valid tree：合法树
        FlowGraphUtil.verifyTreeGraph(tree1);
        Arrays.asList(n1, n2, n3, o1).stream().forEach(n -> n.unlinkParentsAndSons());

        // invalid tree：单独节点
        for (CraftFlowTreeNode n : Arrays.asList(n1, n2, n3)) {
            CraftFlowTreeMap finalTree = new CraftFlowTreeMap(Arrays.asList(n));
            assertThrows(ParamInvalidException.class, () -> FlowGraphUtil.verifyTreeGraph(finalTree));
        }
        // invalid tree：单独两个节点
        for (List<CraftFlowTreeNode> n : Arrays.asList(Arrays.asList(n1, n2),
                                                       Arrays.asList(n1, n3),
                                                       Arrays.asList(n3, n2))) {
            CraftFlowTreeMap finalTree = new CraftFlowTreeMap(n);
            assertThrows(ParamInvalidException.class, () -> FlowGraphUtil.verifyTreeGraph(finalTree));
        }

        // invalid tree：2节点树 + 单节点孤立
        CraftFlowTreeMap finalTree = new CraftFlowTreeMap(Arrays.asList(n1, n2, n3));
        relations.clear();
        relations.add(FlowNodeRelationV.newRelation(n2.getCraftStepId().getId(), n1.getCraftStepId().getId()));
        finalTree.reconnectNodeByRelations(relations);
        assertThrows(ParamInvalidException.class, () -> FlowGraphUtil.verifyTreeGraph(finalTree));

        // invalid tree 孤立节点 + 树
        Arrays.asList(n1, n2, n3).stream().forEach(n -> n.unlinkParentsAndSons());
        relations.clear();
        relations.add(FlowNodeRelationV.newRelation(n2.getCraftStepId().getId(), n1.getCraftStepId().getId()));
        relations.add(FlowNodeRelationV.newRelation(n3.getCraftStepId().getId(), n1.getCraftStepId().getId()));

        CraftFlowTreeMap tree2 = new CraftFlowTreeMap(Arrays.asList(n1, n2, n3));
        tree2.addSingleNode(new CraftFlowTreeNode(CraftFlowNodeEntity.builder().id(UUIDUtil.newUUID())
                                                                     .type(CraftStepTypeEnum.OPERATE)
                                                                     .build()));
        tree2.reconnectNodeByRelations(relations);
        assertThrows(ParamInvalidException.class, () -> FlowGraphUtil.verifyTreeGraph(tree2));

        // invalid tree 重复物料
        Arrays.asList(n1, n2, n3, o1).stream().forEach(n -> n.unlinkParentsAndSons());
        relations.clear();
        relations.add(FlowNodeRelationV.newRelation(n2.getCraftStepId().getId(), o1.getCraftStepId().getId()));
        relations.add(FlowNodeRelationV.newRelation(n21.getCraftStepId().getId(), o1.getCraftStepId().getId()));
        relations.add(FlowNodeRelationV.newRelation(o1.getCraftStepId().getId(), n1.getCraftStepId().getId()));

        CraftFlowTreeMap tree3 = new CraftFlowTreeMap(Arrays.asList(n1, n2, n21, o1));
        tree3.reconnectNodeByRelations(relations);
        assertThrows(ParamInvalidException.class, () -> FlowGraphUtil.verifyTreeGraph(tree3));
    }
}