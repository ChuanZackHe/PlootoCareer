package org.aiit.mes.craft.domain.dao.service.impl;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.aiit.mes.common.constant.CraftStepTypeEnum;
import org.aiit.mes.common.constant.MaterialTransferEnum;
import org.aiit.mes.common.util.UUIDUtil;
import org.aiit.mes.craft.domain.dao.entity.CraftFlowNodeEntity;
import org.aiit.mes.craft.domain.dao.service.ICraftNodeService;
import org.aiit.mes.craft.domain.vo.MaterialInfoV;
import org.aiit.mes.system.admin.domain.dao.service.IAdminService;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName CraftFlowNodeServiceImplTest
 * @Description
 * @createTime 2021.09.01 10:55
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@Slf4j
public class CraftFlowNodeServiceImplTest {

    @Resource
    IAdminService adminService;

    @Resource
    private ICraftNodeService craftFlowNodeService;

    @AfterEach
    public void clean() {
        // 物理删除
        assertTrue(adminService.adminDeleteByTable("craft_flow_node") > 0);
    }

    @Test
    public void removeById() {
        Long matId = RandomUtil.randomLong(100000, 200000);
        Double matNum = RandomUtil.randomDouble(100, 200);
        CraftFlowNodeEntity craftFlowNode = CraftFlowNodeEntity.builder()
                                                               .id(UUIDUtil.newUUID())
                                                               .name("测试步骤")
                                                               .description("测试用步骤")
                                                               .flowId(100L)
                                                               .iconId("fa fa-hand-paper-o")
                                                               .type(CraftStepTypeEnum.MATERIAL_TRANSFER)
                                                               .materialId(matId)
                                                               .materialNum(matNum)
                                                               .verifyRoleId(RandomUtil.randomLong(1000, 2000))
                                                               .transferType(MaterialTransferEnum.STOCK_IN)
                                                               .materialInfo(MaterialInfoV.builder()
                                                                                          .id(matId)
                                                                                          .code("TEST_MAT")
                                                                                          .name("测试物料")
                                                                                          .num(matNum)
                                                                                          .transferType(
                                                                                                  MaterialTransferEnum.STOCK_IN)
                                                                                          .unit("吨")
                                                                                          .build())
                                                               .build();

        assertTrue(craftFlowNodeService.save(craftFlowNode));
        CraftFlowNodeEntity db = craftFlowNodeService.getById(craftFlowNode.getId());
        log.info("query:{}", db);

        // 按照FlowId过滤

        assertEquals(db.getId(), craftFlowNode.getId());
        assertEquals(db.getMaterialInfo()
                       .getCode(), craftFlowNode.getMaterialInfo()
                                                .getCode());

        assertTrue(craftFlowNodeService.removeById(craftFlowNode.getId()));
        assertNull(craftFlowNodeService.getById(craftFlowNode.getId()));
    }

    @Test
    public void listNodeByFlowId() {
        long flowId = RandomUtil.randomLong(100000, 200000);
        Long matId = RandomUtil.randomLong(100000, 200000);
        Double matNum = RandomUtil.randomDouble(100, 200);
        CraftFlowNodeEntity craftFlowNode = CraftFlowNodeEntity.builder()
                                                               .id(UUIDUtil.newUUID())
                                                               .name("测试步骤")
                                                               .description("测试用步骤")
                                                               .flowId(flowId)
                                                               .iconId("fa fa-hand-paper-o")
                                                               .type(CraftStepTypeEnum.MATERIAL_TRANSFER)
                                                               .materialId(matId)
                                                               .materialNum(matNum)
                                                               .transferType(MaterialTransferEnum.STOCK_IN)
                                                               .materialInfo(MaterialInfoV.builder()
                                                                                          .id(matId)
                                                                                          .code("TEST_MAT")
                                                                                          .name("测试物料")
                                                                                          .num(matNum)
                                                                                          .transferType(
                                                                                                  MaterialTransferEnum.STOCK_IN)
                                                                                          .unit("吨")
                                                                                          .build())
                                                               .build();
        assertTrue(craftFlowNodeService.save(craftFlowNode));
        CraftFlowNodeEntity craftFlowNode2 = CraftFlowNodeEntity.builder()
                                                                .id(UUIDUtil.newUUID())
                                                                .name("测试步骤2")
                                                                .description("测试用步骤2")
                                                                .flowId(flowId)
                                                                .iconId("fa fa-hand-paper-o")
                                                                .type(CraftStepTypeEnum.MATERIAL_TRANSFER)
                                                                .materialId(matId)
                                                                .materialNum(matNum)
                                                                .transferType(MaterialTransferEnum.STOCK_IN)
                                                                .materialInfo(MaterialInfoV.builder()
                                                                                           .id(matId)
                                                                                           .code("TEST_MAT")
                                                                                           .name("测试物料")
                                                                                           .num(matNum)
                                                                                           .transferType(
                                                                                                   MaterialTransferEnum.STOCK_IN)
                                                                                           .unit("吨")
                                                                                           .build())
                                                                .build();
        assertTrue(craftFlowNodeService.save(craftFlowNode2));

        assertEquals(2, craftFlowNodeService.listNodeByFlowId(craftFlowNode.getFlowId()).size());
        assertTrue(craftFlowNodeService.deleteNodesByFlowId(flowId));
        assertTrue(CollectionUtils.isEmpty(craftFlowNodeService.listNodeByFlowId(craftFlowNode.getFlowId())));
    }

    @Test
    void testListFlowIdsByStockInMaterialId() {
        long flowId = RandomUtil.randomLong(100000, 200000);
        Long matId = RandomUtil.randomLong(100000, 200000);
        Double matNum = RandomUtil.randomDouble(100, 200);
        CraftFlowNodeEntity craftFlowNode = CraftFlowNodeEntity.builder()
                                                               .id(UUIDUtil.newUUID())
                                                               .name("测试步骤")
                                                               .description("测试用步骤")
                                                               .flowId(flowId)
                                                               .iconId("fa fa-hand-paper-o")
                                                               .type(CraftStepTypeEnum.MATERIAL_TRANSFER)
                                                               .materialId(matId)
                                                               .materialNum(matNum)
                                                               .transferType(MaterialTransferEnum.STOCK_IN)
                                                               .materialInfo(MaterialInfoV.builder()
                                                                                          .id(matId)
                                                                                          .code("TEST_MAT")
                                                                                          .name("测试物料")
                                                                                          .num(matNum)
                                                                                          .transferType(
                                                                                                  MaterialTransferEnum.STOCK_IN)
                                                                                          .unit("吨")
                                                                                          .build())
                                                               .build();
        assertTrue(craftFlowNodeService.save(craftFlowNode));
        CraftFlowNodeEntity craftFlowNode2 = CraftFlowNodeEntity.builder()
                                                                .id(UUIDUtil.newUUID())
                                                                .name("测试步骤2")
                                                                .description("测试用步骤2")
                                                                .flowId(flowId)
                                                                .iconId("fa fa-hand-paper-o")
                                                                .type(CraftStepTypeEnum.MATERIAL_TRANSFER)
                                                                .materialId(matId)
                                                                .materialNum(matNum)
                                                                .transferType(MaterialTransferEnum.STOCK_IN)
                                                                .materialInfo(MaterialInfoV.builder()
                                                                                           .id(matId)
                                                                                           .code("TEST_MAT")
                                                                                           .name("测试物料")
                                                                                           .num(matNum)
                                                                                           .transferType(
                                                                                                   MaterialTransferEnum.STOCK_IN)
                                                                                           .unit("吨")
                                                                                           .build())
                                                                .build();
        assertTrue(craftFlowNodeService.save(craftFlowNode2));

        assertEquals(1, craftFlowNodeService.listFlowIdsByStockInMaterialId(matId).size());
        assertEquals(flowId, craftFlowNodeService.listFlowIdsByStockInMaterialId(matId).get(0));
        assertEquals(0, craftFlowNodeService.listFlowIdsByStockInMaterialId(-1L).size());
        assertTrue(craftFlowNodeService.deleteNodesByFlowId(flowId));
    }

    @Test
    public void testBatchInsert() {
        Long matId = RandomUtil.randomLong(100000, 200000);
        Double matNum = RandomUtil.randomDouble(100, 200);
        long flowId = 100000000000L + RandomUtil.randomLong(1000000);
        CraftFlowNodeEntity craftFlowNode = CraftFlowNodeEntity.builder()
                                                               .id(UUIDUtil.newUUID())
                                                               .name("测试步骤")
                                                               .description("测试用步骤")
                                                               .flowId(flowId)
                                                               .iconId("fa fa-hand-paper-o")
                                                               .type(CraftStepTypeEnum.MATERIAL_TRANSFER)
                                                               .materialId(matId)
                                                               .materialNum(matNum)
                                                               .transferType(MaterialTransferEnum.STOCK_IN)
                                                               .materialInfo(MaterialInfoV.builder()
                                                                                          .id(matId)
                                                                                          .code("TEST_MAT")
                                                                                          .name("测试物料")
                                                                                          .num(matNum)
                                                                                          .transferType(
                                                                                                  MaterialTransferEnum.STOCK_IN)
                                                                                          .unit("吨")
                                                                                          .build())
                                                               .build();
        CraftFlowNodeEntity craftFlowNode2 = CraftFlowNodeEntity.builder()
                                                                .id(UUIDUtil.newUUID())
                                                                .name("测试步骤2")
                                                                .description("测试用步骤2")
                                                                .flowId(flowId)
                                                                .iconId("fa fa-hand-paper-o")
                                                                .type(CraftStepTypeEnum.MATERIAL_TRANSFER)
                                                                .materialId(matId)
                                                                .materialNum(matNum)
                                                                .transferType(MaterialTransferEnum.STOCK_IN)
                                                                .materialInfo(MaterialInfoV.builder()
                                                                                           .id(matId)
                                                                                           .code("TEST_MAT")
                                                                                           .name("测试物料")
                                                                                           .num(matNum)
                                                                                           .transferType(
                                                                                                   MaterialTransferEnum.STOCK_IN)
                                                                                           .unit("吨")
                                                                                           .build())
                                                                .build();
        List<CraftFlowNodeEntity> entityList = new ArrayList<>();
        entityList.add(craftFlowNode);
        entityList.add(craftFlowNode2);
        assertTrue(craftFlowNodeService.saveBatch(entityList, 100));

        assertEquals(2, craftFlowNodeService.listNodeByFlowId(craftFlowNode.getFlowId()).size());
        assertTrue(craftFlowNodeService.deleteNodesByFlowId(flowId));
    }
}