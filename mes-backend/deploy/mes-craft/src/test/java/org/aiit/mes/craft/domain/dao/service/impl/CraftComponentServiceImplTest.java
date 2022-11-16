package org.aiit.mes.craft.domain.dao.service.impl;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.aiit.mes.common.constant.CraftStepTypeEnum;
import org.aiit.mes.common.constant.DataStateEnum;
import org.aiit.mes.common.constant.MaterialTransferEnum;
import org.aiit.mes.craft.domain.dao.entity.CraftComponentEntity;
import org.aiit.mes.craft.domain.dao.service.ICraftComponentService;
import org.aiit.mes.craft.domain.vo.MaterialInfoV;
import org.aiit.mes.system.admin.domain.dao.service.IAdminService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName CraftComponentServiceImplTest
 * @Description
 * @createTime 2021.08.31 10:01
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@Slf4j
public class CraftComponentServiceImplTest {

    @Resource
    private IAdminService adminService;

    @Resource
    private ICraftComponentService craftComponentService;

    @Test
    public void componentBaseTest() {
        Long matId = RandomUtil.randomLong(100000, 200000);
        Double matNum = RandomUtil.randomDouble(100, 200);
        CraftComponentEntity craftComponent = CraftComponentEntity.builder()
                                                                  .name("测试步骤")
                                                                  .state(DataStateEnum.ACTIVE)
                                                                  .iconId("fa fa-hand-paper-o")
                                                                  .roleId(10L)
                                                                  .templateId(10)
//                                                                  .tenantId("58a0e82e-0ea2-4046-85c9-9e0e4b19ab21")
                                                                  .type(CraftStepTypeEnum.MATERIAL_TRANSFER)
                                                                  .materialId(matId)
                                                                  .verifyRoleId(RandomUtil.randomLong(1000, 2000))
                                                                  .materialNum(matNum)
                                                                  .transferType(
                                                                          MaterialTransferEnum.STOCK_IN)
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
        assertTrue(craftComponentService.save(craftComponent));
        CraftComponentEntity db = craftComponentService.getById(craftComponent.getId());
        log.info("query: {}", db);
        // check
        assertEquals(db.getMaterialId(), craftComponent.getMaterialId());
        assertTrue(Math.abs(db.getMaterialNum() - craftComponent.getMaterialNum()) < 0.001);
        assertEquals(db.getMaterialInfo().getId(), craftComponent.getMaterialInfo().getId());
        assertTrue(Math.abs(db.getMaterialInfo().getNum() - craftComponent.getMaterialInfo().getNum()) < 0.001);

        assertEquals(db.getTransferType(), craftComponent.getTransferType());
        assertEquals(db.getVerifyRoleId(), craftComponent.getVerifyRoleId());
        assertEquals(db.getMaterialInfo()
                       .getCode(), craftComponent.getMaterialInfo()
                                                 .getCode());
        assertTrue(craftComponentService.removeById(craftComponent.getId()));
        assertNull(craftComponentService.getById(craftComponent.getId()));

        // 物理删除
        assertTrue(adminService.adminDeleteByTableAndDay("craft_component", 0) > 0);
    }

}