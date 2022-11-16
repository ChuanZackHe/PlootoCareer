package org.aiit.mes.craft.domain.repository;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.aiit.mes.common.constant.CraftStepTypeEnum;
import org.aiit.mes.common.constant.MaterialTransferEnum;
import org.aiit.mes.common.primitive.LongId;
import org.aiit.mes.common.util.UUIDUtil;
import org.aiit.mes.craft.domain.aggregate.FlowAgg;
import org.aiit.mes.craft.domain.dao.entity.CraftFlowEntity;
import org.aiit.mes.craft.domain.dao.entity.CraftFlowNodeEntity;
import org.aiit.mes.craft.domain.dao.entity.CraftFlowRelationEntity;
import org.aiit.mes.craft.domain.dao.service.ICraftFlowRelationService;
import org.aiit.mes.craft.domain.dao.service.ICraftFlowService;
import org.aiit.mes.craft.domain.dao.service.ICraftNodeService;
import org.aiit.mes.craft.domain.vo.FlowNodeRelationV;
import org.aiit.mes.craft.domain.vo.MaterialInfoV;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName FlowRepositoryTest
 * @Description
 * @createTime 2021.09.02 17:29
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@Slf4j
public class FlowRepoTest {

    @Resource
    private FlowRepo flowRepo;

    @Resource
    private ICraftFlowService craftFlowService;

    @Resource
    private ICraftNodeService craftFlowNodeService;

    @Resource
    private ICraftFlowRelationService craftFlowRelationService;

    @Test
    public void getFlowAgg() {
        // 预置数据
        Long matId = RandomUtil.randomLong(100000, 200000);
        Double matNum = RandomUtil.randomDouble(100, 200);
        CraftFlowEntity flow = CraftFlowEntity.builder()
                                              .name(RandomStringUtils.random(10))
                                              .bindResourceId(122)
                                              .build();
        flow.setTenantId("58a0e82e-0ea2-4046-85c9-9e0e4b19ab21");
        assertTrue(craftFlowService.save(flow));

        CraftFlowNodeEntity craftFlowNode = CraftFlowNodeEntity.builder()
                                                               .id(UUIDUtil.newUUID())
                                                               .name("测试步骤")
                                                               .description("测试用步骤")
                                                               .flowId(flow.getId())
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
                                                                .flowId(flow.getId())
                                                                .iconId("fa fa-hand-paper-o")
                                                                .type(CraftStepTypeEnum.MATERIAL_TRANSFER)
                                                                .materialId(matId)
                                                                .materialNum(matNum)
                                                                .transferType(MaterialTransferEnum.STOCK_IN)
                                                                .materialInfo(MaterialInfoV.builder()
                                                                                           .id(matId)
                                                                                           .code(
                                                                                                   "TEST_MAT")
                                                                                           .name("测试物料")
                                                                                           .num(matNum)
                                                                                           .transferType(
                                                                                                   MaterialTransferEnum.STOCK_IN)
                                                                                           .unit("吨")
                                                                                           .build())
                                                                .build();
        assertTrue(craftFlowNodeService.save(craftFlowNode2));

        CraftFlowRelationEntity relation = CraftFlowRelationEntity.builder()
                                                                  .flowId(flow.getId())
                                                                  .build()
                                                                  .addRelations(FlowNodeRelationV.newRelation(
                                                                          craftFlowNode.getId(),
                                                                          craftFlowNode2.getId()));
        relation.setTenantId("58a0e82e-0ea2-4046-85c9-9e0e4b19ab21");
        assertTrue(craftFlowRelationService.save(relation));

        FlowAgg flowAgg = flowRepo.getFlowAgg(new LongId(flow.getId()));

        assertNotNull(flowAgg);

        assertTrue(flowRepo.deleteFlow(flowAgg));
    }
}