package org.aiit.mes.craft.domain.crafttree;

import cn.hutool.core.util.RandomUtil;
import org.aiit.mes.common.primitive.UId;
import org.aiit.mes.common.util.UUIDUtil;
import org.aiit.mes.craft.domain.dao.entity.CraftFlowNodeEntity;
import org.aiit.mes.craft.domain.primitive.MaterialId;
import org.aiit.mes.craft.domain.vo.MaterialInfoV;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName CraftStepNodeTest
 * @Description
 * @createTime 2021.12.23 10:15
 */
class CraftTreeNodeTest {

    private static final Logger logger = LoggerFactory.getLogger(CraftTreeNodeTest.class);

    @Test
    void hashCodeAndEqual() {
        Double num = 10.0;
        MaterialInfoV m1 = MaterialInfoV.builder().id(RandomUtil.randomLong(1000, 5000)).num(num).build();
        CraftFlowNodeEntity craftFlowNodeEntity = CraftFlowNodeEntity.builder().id(UUIDUtil.newUUID())
                                                                     .materialId(m1.getId()).materialInfo(m1).build();
        CraftFlowTreeNode n1 = new CraftFlowTreeNode(craftFlowNodeEntity);

        CraftFlowTreeNode n2 = new CraftFlowTreeNode(craftFlowNodeEntity);

        assertEquals(n1.hashCode(), n2.hashCode());
        assertEquals(n1, n2);
    }

    @Test
    void multiply() {
        Double num = 10.0;
        MaterialInfoV m1 = MaterialInfoV.builder().id(RandomUtil.randomLong(1000, 5000)).num(num).build();
        CraftFlowNodeEntity craftFlowNodeEntity = CraftFlowNodeEntity.builder().id(UUIDUtil.newUUID())
                                                                     .materialId(m1.getId()).materialInfo(m1).build();
        CraftFlowTreeNode n1 = new CraftFlowTreeNode(craftFlowNodeEntity);

        assertEquals(new MaterialId(m1.getId()), n1.getMaterialId());
        assertEquals(new UId(craftFlowNodeEntity.getId()), n1.getCraftStepId());

        assertEquals(num, n1.getData().getMaterialInfo().getNum());

        n1.multiply(1.0);
        assertEquals(num * 1.0, n1.getData().getMaterialInfo().getNum());

        n1.multiply(3.0);
        assertEquals(num * 3.0, n1.getData().getMaterialInfo().getNum());
    }

    @Test
    void calculateMultiple() {
        MaterialInfoV m1 = MaterialInfoV.builder().id(RandomUtil.randomLong(1000, 5000)).num(10.0).build();
        MaterialInfoV m2 = MaterialInfoV.builder().id(RandomUtil.randomLong(1000, 5000)).num(20.0).build();
        MaterialInfoV m3 = MaterialInfoV.builder().id(RandomUtil.randomLong(1000, 5000)).num(null).build();
        CraftFlowTreeNode n1 = new CraftFlowTreeNode(CraftFlowNodeEntity.builder().id(UUIDUtil.newUUID())
                                                                        .materialId(m1.getId()).materialInfo(m1)
                                                                        .build());
        CraftFlowTreeNode n2 = new CraftFlowTreeNode(CraftFlowNodeEntity.builder().id(UUIDUtil.newUUID())
                                                                        .materialId(m2.getId()).materialInfo(m2)
                                                                        .build());
        CraftFlowTreeNode n3 = new CraftFlowTreeNode(CraftFlowNodeEntity.builder().id(UUIDUtil.newUUID())
                                                                        .materialId(m3.getId()).materialInfo(m3)
                                                                        .build());

        assertEquals(0.5, n1.calculateMultiple(n2));
        assertEquals(2.0, n2.calculateMultiple(n1));
        assertEquals(1.0, n1.calculateMultiple(n3));
        assertEquals(1.0, n3.calculateMultiple(n1));
        assertEquals(1.0, n3.calculateMultiple(n3));
        logger.info("{}", n1.calculateMultiple(n2));
    }
}