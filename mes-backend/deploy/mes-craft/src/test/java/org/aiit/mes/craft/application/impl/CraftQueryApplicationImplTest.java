package org.aiit.mes.craft.application.impl;

import lombok.extern.slf4j.Slf4j;
import org.aiit.mes.base.material.domain.dto.BaseMaterialRepresent;
import org.aiit.mes.base.material.domain.entity.BaseMaterialEntity;
import org.aiit.mes.common.constant.BaseMaterialType;
import org.aiit.mes.common.constant.CraftStepTypeEnum;
import org.aiit.mes.common.util.PropertyCopyUtil;
import org.aiit.mes.common.util.UUIDUtil;
import org.aiit.mes.craft.application.ICraftQueryApplication;
import org.aiit.mes.craft.domain.crafttree.CraftFlowTreeMap;
import org.aiit.mes.craft.domain.dao.entity.CraftFlowNodeEntity;
import org.aiit.mes.craft.domain.dao.service.ICraftNodeService;
import org.aiit.mes.craft.domain.dto.AnalyzeBomForProduceQuery;
import org.aiit.mes.craft.domain.vo.CraftBomTreeRepresentV;
import org.aiit.mes.craft.domain.vo.MaterialInfoV;
import org.aiit.mes.warehouse.stock.domain.dao.entity.StockSummaryEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.aiit.mes.common.constant.MaterialTransferEnum.STOCK_IN;
import static org.aiit.mes.common.constant.MaterialTransferEnum.STOCK_OUT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName CraftQueryApplicationImplTest
 * @Description
 * @createTime 2021.12.28 16:32
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@Slf4j
class CraftQueryApplicationImplTest {

    @Autowired
    @Spy
    ICraftQueryApplication craftQueryApplication;

    @MockBean
    ICraftNodeService craftNodeService;

    Long flowId1 = 101L;

    Long inputMatId1 = 100L;

    Long inputMatId2 = 200L;

    Long outputMatId1 = 300L;

    private List<BaseMaterialEntity> materialList;

    private List<StockSummaryEntity> stockList;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);


        MaterialInfoV m1 = MaterialInfoV.builder().id(outputMatId1).num(50D).transferType(STOCK_IN).build();
        MaterialInfoV m2 = MaterialInfoV.builder().id(inputMatId1).num(100D).transferType(STOCK_OUT).build();
        MaterialInfoV m3 = MaterialInfoV.builder().id(inputMatId2).num(150D).transferType(STOCK_OUT).build();
        CraftFlowNodeEntity n1 = CraftFlowNodeEntity.builder().id(UUIDUtil.newUUID())
                                                    .type(CraftStepTypeEnum.MATERIAL_TRANSFER)
                                                    .transferType(m1.getTransferType())
                                                    .materialId(m1.getId()).materialInfo(m1).materialNum(m1.getNum())
                                                    .build();
        CraftFlowNodeEntity n2 = CraftFlowNodeEntity.builder().id(UUIDUtil.newUUID())
                                                    .transferType(m2.getTransferType())
                                                    .type(CraftStepTypeEnum.MATERIAL_TRANSFER)
                                                    .materialId(m2.getId()).materialInfo(m2).materialNum(m2.getNum())
                                                    .build();
        CraftFlowNodeEntity n3 = CraftFlowNodeEntity.builder().id(UUIDUtil.newUUID())
                                                    .transferType(m3.getTransferType())
                                                    .type(CraftStepTypeEnum.MATERIAL_TRANSFER)
                                                    .materialId(m3.getId()).materialInfo(m3).materialNum(m3.getNum())
                                                    .build();

        List<CraftFlowNodeEntity> craftFlowNodeEntities = Arrays.asList(n1, n2, n3);
        materialList =
                Arrays.asList(PropertyCopyUtil.copyToClass(BaseMaterialRepresent.builder().name("test name1").id(100L)
                                                                                .code("testCode100").type(
                                                                           BaseMaterialType.RAW).canBuy(true).unit(
                                                                           "个").build(),
                                                           BaseMaterialEntity.class),
                              PropertyCopyUtil.copyToClass(BaseMaterialRepresent.builder().name("test name2").id(200L)
                                                                                .code("testCode200").type(
                                                                           BaseMaterialType.RAW).canBuy(true).unit(
                                                                           "个").build(),
                                                           BaseMaterialEntity.class),
                              PropertyCopyUtil.copyToClass(BaseMaterialRepresent.builder().name("test name3").id(300L)
                                                                                .code("testCode300").type(
                                                                           BaseMaterialType.PRD).canBuy(true).unit(
                                                                           "个").build(),
                                                           BaseMaterialEntity.class));

        stockList = Arrays.asList(StockSummaryEntity.builder().materialId(100L).count(100D).build(),
                                  StockSummaryEntity.builder().materialId(200L).count(100D).build(),
                                  StockSummaryEntity.builder().materialId(300L).count(100D).build()
        );

        when(craftNodeService.queryMaterialNodesByFlowId(flowId1)).thenReturn(craftFlowNodeEntities);

        doReturn(materialList).when(craftQueryApplication).queryMaterialInfosByIds(anyList());
        doReturn(stockList).when(craftQueryApplication).queryStockInfoByMaterialIds(anyList());
    }

    @Test
    void pageListComponents() {
    }

    @Test
    void pageListFlowSummary() {
    }

    @Test
    void showFlowById() {
    }

    @Test
    void queryRawMaterialTree() {
        List<BaseMaterialEntity> ret = craftQueryApplication.queryMaterialInfosByIds(Arrays.asList(1L, 2L));
        assertEquals(materialList.size(), ret.size());
        log.info("{}", ret);

        CraftFlowTreeMap tree = craftQueryApplication.queryRawMaterialTree(
                AnalyzeBomForProduceQuery.builder().materialId(outputMatId1).requiredNum(10.0).flowId(flowId1).build());
        log.info("{}", tree);
        assertEquals(3, tree.getAllStepsMap().size());
        assertEquals(2, tree.getInputMap().size());
        assertEquals(1, tree.getOutputMap().size());
        assertEquals(tree.getEntryNode().getMaterialId().getId(), outputMatId1);
        assertEquals(10.0, tree.getEntryNode().getData().getMaterialNum());
    }

    @Test
    void analyzeBomForProduce() {
        Double req = 10.0;
        CraftBomTreeRepresentV ret = craftQueryApplication.analyzeBomForProduce(
                AnalyzeBomForProduceQuery.builder().materialId(outputMatId1).requiredNum(req)
                                         .flowId(flowId1).build());
        log.info("{}", ret.toString());
        assertTrue(ret.getIsStockSatisfied());
        assertEquals(req, ret.getTarget().getRequiredNum());
        assertEquals(outputMatId1, ret.getTarget().getId());
        assertEquals(2, ret.getInputs().size());
        assertEquals(100, ret.getInputs().get(0).getStockNum());
        assertEquals(100, ret.getInputs().get(1).getStockNum());

        // 单个物料超过
        req = 50.0;
        ret = craftQueryApplication.analyzeBomForProduce(
                AnalyzeBomForProduceQuery.builder().materialId(outputMatId1).requiredNum(req)
                                         .flowId(flowId1).build());
        log.info("{}", ret.toString());
        assertFalse(ret.getIsStockSatisfied());
        assertEquals(req, ret.getTarget().getRequiredNum());
        assertEquals(outputMatId1, ret.getTarget().getId());
        assertEquals(2, ret.getInputs().size());
        assertEquals(100, ret.getInputs().get(0).getStockNum());
        assertEquals(100, ret.getInputs().get(1).getStockNum());

        // 两个物料超过
        req = 99.0;
        ret = craftQueryApplication.analyzeBomForProduce(
                AnalyzeBomForProduceQuery.builder().materialId(outputMatId1).requiredNum(req)
                                         .flowId(flowId1).build());
        log.info("{}", ret.toString());
        assertFalse(ret.getIsStockSatisfied());
        assertEquals(req, ret.getTarget().getRequiredNum());
        assertEquals(outputMatId1, ret.getTarget().getId());
        assertEquals(2, ret.getInputs().size());
        assertEquals(100, ret.getInputs().get(0).getStockNum());
        assertEquals(100, ret.getInputs().get(1).getStockNum());
    }


    @Test
    void queryStockInfoByMaterialIds() {
    }
}