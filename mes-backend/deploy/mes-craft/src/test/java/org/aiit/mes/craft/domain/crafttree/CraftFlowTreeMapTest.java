package org.aiit.mes.craft.domain.crafttree;

import cn.hutool.core.util.RandomUtil;
import org.aiit.mes.common.constant.CraftStepTypeEnum;
import org.aiit.mes.common.constant.MaterialTransferEnum;
import org.aiit.mes.common.util.UUIDUtil;
import org.aiit.mes.craft.domain.dao.entity.CraftFlowNodeEntity;
import org.aiit.mes.craft.domain.primitive.MaterialId;
import org.aiit.mes.craft.domain.vo.FlowNodeRelationV;
import org.aiit.mes.craft.domain.vo.MaterialInfoV;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.aiit.mes.common.constant.MaterialTransferEnum.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName CraftFlowTreeMapTest
 * @Description
 * @createTime 2021.12.23 10:35
 */
class CraftFlowTreeMapTest {


    @Test
    void addNode() {
        MaterialInfoV m1 = MaterialInfoV.builder().id(RandomUtil.randomLong(1000, 5000)).num(
                RandomUtil.randomDouble(100, 200)).transferType(STOCK_IN).build();
        MaterialInfoV m2 =
                MaterialInfoV.builder().id(RandomUtil.randomLong(1000, 5000)).num(RandomUtil.randomDouble(100, 200))
                             .transferType(STOCK_OUT).build();
        MaterialInfoV m3 =
                MaterialInfoV.builder().id(RandomUtil.randomLong(1000, 5000)).num(RandomUtil.randomDouble(100, 200))
                             .transferType(MaterialTransferEnum.ON_THE_SCENE_IN).build();
        MaterialInfoV m4 =
                MaterialInfoV.builder().id(RandomUtil.randomLong(1000, 5000)).num(RandomUtil.randomDouble(100, 200))
                             .transferType(MaterialTransferEnum.ON_THE_SCENE).build();
        CraftFlowTreeNode n1 = new CraftFlowTreeNode(CraftFlowNodeEntity.builder().id(UUIDUtil.newUUID())
                                                                        .type(CraftStepTypeEnum.MATERIAL_TRANSFER)
                                                                        .transferType(m1.getTransferType())
                                                                        .materialId(m1.getId()).materialInfo(m1)
                                                                        .build());
        CraftFlowTreeNode n2 = new CraftFlowTreeNode(CraftFlowNodeEntity.builder().id(UUIDUtil.newUUID())
                                                                        .transferType(m2.getTransferType())
                                                                        .type(CraftStepTypeEnum.MATERIAL_TRANSFER)
                                                                        .materialId(m2.getId()).materialInfo(m2)
                                                                        .build());
        CraftFlowTreeNode n3 = new CraftFlowTreeNode(CraftFlowNodeEntity.builder().id(UUIDUtil.newUUID())
                                                                        .transferType(m3.getTransferType())
                                                                        .type(CraftStepTypeEnum.MATERIAL_TRANSFER)
                                                                        .materialId(m3.getId()).materialInfo(m3)
                                                                        .build());
        CraftFlowTreeNode n4 = new CraftFlowTreeNode(CraftFlowNodeEntity.builder().id(UUIDUtil.newUUID())
                                                                        .transferType(m4.getTransferType())
                                                                        .type(CraftStepTypeEnum.MATERIAL_TRANSFER)
                                                                        .materialId(m4.getId()).materialInfo(m4)
                                                                        .build());

        CraftFlowTreeMap tree = new CraftFlowTreeMap(Arrays.asList(n1, n2, n3, n4), new MaterialId(m1.getId()));
        assertEquals(n1, tree.getEntryNode());
        assertEquals(1, tree.getInputMap().size());
        assertEquals(1, tree.getOutputMap().size());
        assertEquals(1, tree.getTempInputMap().size());
        assertEquals(1, tree.getTempOutputMap().size());
        assertEquals(STOCK_IN, tree.getOutputMap().values().stream().findFirst().get().getData().getTransferType());
        assertEquals(STOCK_OUT, tree.getInputMap().values().stream().findFirst().get().getData().getTransferType());
        assertEquals(ON_THE_SCENE,
                     tree.getTempOutputMap().values().stream().findFirst().get().getData().getTransferType());
        assertEquals(ON_THE_SCENE_IN,
                     tree.getTempInputMap().values().stream().findFirst().get().getData().getTransferType());
    }

    @Test
    void multiplyNumBy() {
        Double num = 10.0;
        MaterialInfoV m1 = MaterialInfoV.builder().id(RandomUtil.randomLong(1000, 5000)).num(
                num).transferType(STOCK_IN).build();
        MaterialInfoV m2 =
                MaterialInfoV.builder().id(RandomUtil.randomLong(1000, 5000)).num(num)
                             .transferType(STOCK_OUT).build();
        MaterialInfoV m3 =
                MaterialInfoV.builder().id(RandomUtil.randomLong(1000, 5000)).num(num)
                             .transferType(MaterialTransferEnum.ON_THE_SCENE_IN).build();
        MaterialInfoV m4 =
                MaterialInfoV.builder().id(RandomUtil.randomLong(1000, 5000)).num(num)
                             .transferType(MaterialTransferEnum.ON_THE_SCENE).build();
        CraftFlowTreeNode n1 = new CraftFlowTreeNode(CraftFlowNodeEntity.builder().id(UUIDUtil.newUUID())
                                                                        .transferType(m1.getTransferType())
                                                                        .type(CraftStepTypeEnum.MATERIAL_TRANSFER)
                                                                        .materialId(m1.getId()).materialInfo(m1)
                                                                        .build());
        CraftFlowTreeNode n2 = new CraftFlowTreeNode(CraftFlowNodeEntity.builder().id(UUIDUtil.newUUID())
                                                                        .transferType(m2.getTransferType())
                                                                        .type(CraftStepTypeEnum.MATERIAL_TRANSFER)
                                                                        .materialId(m2.getId()).materialInfo(m2)
                                                                        .build());
        CraftFlowTreeNode n3 = new CraftFlowTreeNode(CraftFlowNodeEntity.builder().id(UUIDUtil.newUUID())
                                                                        .transferType(m3.getTransferType())
                                                                        .type(CraftStepTypeEnum.MATERIAL_TRANSFER)
                                                                        .materialId(m3.getId()).materialInfo(m3)
                                                                        .build());
        CraftFlowTreeNode n4 = new CraftFlowTreeNode(CraftFlowNodeEntity.builder().id(UUIDUtil.newUUID())
                                                                        .transferType(m4.getTransferType())
                                                                        .type(CraftStepTypeEnum.MATERIAL_TRANSFER)
                                                                        .materialId(m4.getId()).materialInfo(m4)
                                                                        .build());

        CraftFlowTreeMap tree = new CraftFlowTreeMap(Arrays.asList(n1, n2, n3, n4), new MaterialId(m1.getId()));
        assertEquals(n1, tree.getEntryNode());
        assertEquals(num, tree.getOutputMap().values().stream().findFirst().get().getData().getMaterialInfo().getNum());
        assertEquals(num, tree.getInputMap().values().stream().findFirst().get().getData().getMaterialInfo().getNum());
        assertEquals(num,
                     tree.getTempOutputMap().values().stream().findFirst().get().getData().getMaterialInfo().getNum());
        assertEquals(num,
                     tree.getTempInputMap().values().stream().findFirst().get().getData().getMaterialInfo().getNum());

        tree.multiplyMaterialNumBy(10.0);
        num *= 10.0;
        assertEquals(num, tree.getOutputMap().values().stream().findFirst().get().getData().getMaterialInfo().getNum());
        assertEquals(num, tree.getInputMap().values().stream().findFirst().get().getData().getMaterialInfo().getNum());
        assertEquals(num,
                     tree.getTempOutputMap().values().stream().findFirst().get().getData().getMaterialInfo().getNum());
        assertEquals(num,
                     tree.getTempInputMap().values().stream().findFirst().get().getData().getMaterialInfo().getNum());
    }

    @Test
    void mergeTempTreeMap() {
        Double num = 10.0;
        MaterialInfoV m1 = MaterialInfoV.builder().id(RandomUtil.randomLong(1000, 5000)).num(num)
                                        .transferType(STOCK_IN).build();
        MaterialInfoV m2 = MaterialInfoV.builder().id(RandomUtil.randomLong(1000, 5000)).num(num)
                                        .transferType(STOCK_OUT).build();
        MaterialInfoV m3 = MaterialInfoV.builder().id(RandomUtil.randomLong(1000, 5000)).num(num)
                                        .transferType(MaterialTransferEnum.ON_THE_SCENE_IN).build();
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
        CraftFlowTreeNode n3 = new CraftFlowTreeNode(CraftFlowNodeEntity.builder().id(UUIDUtil.newUUID())
                                                                        .transferType(m3.getTransferType())
                                                                        .type(CraftStepTypeEnum.MATERIAL_TRANSFER)
                                                                        .materialId(m3.getId()).materialInfo(m3)
                                                                        .materialNum(m3.getNum())
                                                                        .build());
        // relation:
        // n2   n3
        //   \ /
        //    n1
        List<FlowNodeRelationV> relations = new ArrayList<>();
        relations.add(FlowNodeRelationV.newRelation(n2.getCraftStepId().getId(), n1.getCraftStepId().getId()));
        relations.add(FlowNodeRelationV.newRelation(n3.getCraftStepId().getId(), n1.getCraftStepId().getId()));

        CraftFlowTreeMap tree1 = new CraftFlowTreeMap(Arrays.asList(n1, n2, n3), new MaterialId(m1.getId()));
        // reconnect
        tree1.reconnectNodeByRelations(relations);

        assertEquals(n1, tree1.getEntryNode());
        assertEquals(num,
                     tree1.getOutputMap().values().stream().findFirst().get().getData().getMaterialInfo().getNum());
        assertEquals(num, tree1.getInputMap().values().stream().findFirst().get().getData().getMaterialInfo().getNum());
        assertEquals(num,
                     tree1.getTempInputMap().values().stream().findFirst().get().getData().getMaterialInfo().getNum());

        // check relation:
        // n2   n3
        //   \ /
        //    n1
        assertTrue(n1.getParents().contains(n2));
        assertTrue(n1.getParents().contains(n3));
        assertEquals(2, n1.getParents().size());
        assertTrue(n1.needFork());
        assertTrue(CollectionUtils.isEmpty(n1.getSons()));

        assertEquals(1, n2.getSons().size());
        assertEquals(1, n3.getSons().size());
        assertTrue(n2.getSons().contains(n1));
        assertTrue(n3.getSons().contains(n1));
        assertTrue(CollectionUtils.isEmpty(n2.getParents()));
        assertTrue(CollectionUtils.isEmpty(n3.getParents()));

        // tree2
        Double num2 = 5.0;
        MaterialInfoV m21 = MaterialInfoV.builder().id(RandomUtil.randomLong(1000, 5000)).num(num2)
                                         .transferType(STOCK_OUT).build();
        MaterialInfoV m22 = MaterialInfoV.builder().id(RandomUtil.randomLong(1000, 5000)).num(num2)
                                         .transferType(STOCK_OUT).build();
        MaterialInfoV m23 = MaterialInfoV.builder().id(m3.getId()).num(num2)
                                         .transferType(MaterialTransferEnum.ON_THE_SCENE).build();
        CraftFlowTreeNode n21 = new CraftFlowTreeNode(CraftFlowNodeEntity.builder().id(UUIDUtil.newUUID())
                                                                         .transferType(m21.getTransferType())
                                                                         .type(CraftStepTypeEnum.MATERIAL_TRANSFER)
                                                                         .materialId(m21.getId()).materialInfo(m21)
                                                                         .materialNum(m21.getNum())
                                                                         .build());
        CraftFlowTreeNode n22 = new CraftFlowTreeNode(CraftFlowNodeEntity.builder().id(UUIDUtil.newUUID())
                                                                         .transferType(m22.getTransferType())
                                                                         .type(CraftStepTypeEnum.MATERIAL_TRANSFER)
                                                                         .materialId(m22.getId()).materialInfo(m22)
                                                                         .materialNum(m22.getNum())
                                                                         .build());
        CraftFlowTreeNode n23 = new CraftFlowTreeNode(CraftFlowNodeEntity.builder().id(UUIDUtil.newUUID())
                                                                         .transferType(m23.getTransferType())
                                                                         .type(CraftStepTypeEnum.MATERIAL_TRANSFER)
                                                                         .materialId(m23.getId()).materialInfo(m23)
                                                                         .materialNum(m23.getNum())
                                                                         .build());
        // relation:
        // n21   n22
        //   \ /
        //    n23
        List<FlowNodeRelationV> relations2 = new ArrayList<>();
        relations2.add(FlowNodeRelationV.newRelation(n21.getCraftStepId().getId(), n23.getCraftStepId().getId()));
        relations2.add(FlowNodeRelationV.newRelation(n22.getCraftStepId().getId(), n23.getCraftStepId().getId()));

        CraftFlowTreeMap subTree = new CraftFlowTreeMap(Arrays.asList(n21, n22, n23), new MaterialId(m23.getId()));
        subTree.reconnectNodeByRelations(relations2);

        assertEquals(n23, subTree.getEntryNode());
        assertEquals(num2,
                     subTree.getInputMap().values().stream().findFirst().get().getData().getMaterialInfo().getNum());
        assertEquals(num2, subTree.getTempOutputMap().values().stream().findFirst().get().getData().getMaterialInfo()
                                  .getNum());

        // check relation:
        // n21   n22
        //   \ /
        //    n23（n3）
        assertTrue(n23.getParents().contains(n21));
        assertTrue(n23.getParents().contains(n22));
        assertEquals(2, n23.getParents().size());
        assertTrue(n23.needFork());
        assertTrue(CollectionUtils.isEmpty(n23.getSons()));

        assertEquals(1, n21.getSons().size());
        assertEquals(1, n22.getSons().size());
        assertTrue(n21.getSons().contains(n23));
        assertTrue(n22.getSons().contains(n23));
        assertTrue(CollectionUtils.isEmpty(n21.getParents()));
        assertTrue(CollectionUtils.isEmpty(n22.getParents()));

        // merge tree
        tree1.mergeSubTreeMap(subTree);
        assertEquals(3, tree1.getInputMap().size());
        assertEquals(1, tree1.getOutputMap().size());
        assertEquals(0, tree1.getTempOutputMap().size());
        assertEquals(0, tree1.getTempInputMap().size());
        Arrays.asList(n2, n21, n22).stream().forEach(n ->
                                                     {
                                                         assertTrue(tree1.getInputMap().containsValue(n));
                                                         assertEquals(10.0, n.getData().getMaterialInfo().getNum());
                                                     });
        assertTrue(tree1.getOutputMap().containsValue(n1));
        assertEquals(10.0,
                     tree1.getOutputMap().values().stream().findFirst().get().getData().getMaterialInfo().getNum());

        // sync
        tree1.syncMaterialNum(20.0);
        assertEquals(20.0,
                     tree1.getOutputMap().values().stream().findFirst().get().getData().getMaterialInfo().getNum());

        // check relation:
        // n2 n21 n22
        //   \ | /
        //     n1
        assertTrue(n1.getParents().containsAll(Arrays.asList(n2, n21, n22)));
        assertEquals(3, n1.getParents().size());
        assertTrue(n1.needFork());
        assertTrue(CollectionUtils.isEmpty(n1.getSons()));

        assertEquals(0, n3.getParents().size());
        assertEquals(0, n3.getSons().size());
        assertEquals(0, n23.getParents().size());
        assertEquals(0, n23.getSons().size());

        assertEquals(1, n2.getSons().size());
        assertEquals(1, n21.getSons().size());
        assertEquals(1, n22.getSons().size());
        assertTrue(n2.getSons().contains(n1));
        assertTrue(n21.getSons().contains(n1));
        assertTrue(n22.getSons().contains(n1));

        assertTrue(CollectionUtils.isEmpty(n2.getParents()));
        assertTrue(CollectionUtils.isEmpty(n21.getParents()));
        assertTrue(CollectionUtils.isEmpty(n22.getParents()));
    }
}