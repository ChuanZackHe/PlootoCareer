package org.aiit.mes.craft.domain.dao.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.aiit.mes.common.constant.DataStateEnum;
import org.aiit.mes.craft.domain.dao.entity.CraftFlowEntity;
import org.aiit.mes.craft.domain.dao.service.ICraftFlowService;
import org.aiit.mes.system.admin.domain.dao.service.IAdminService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author heyu
 * @version 1.0.0
 * @ClassName CraftFlowServiceImplTest
 * @Description
 * @createTime 2021.08.31 13:57
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@Slf4j
public class CraftFlowServiceImplTest {

    @Resource
    private ICraftFlowService craftFlowService;

    @Resource
    private IAdminService adminService;

    @Test
    public void removeById() {
        CraftFlowEntity flow = CraftFlowEntity.builder()
                                              .name(RandomStringUtils.random(10))
                                              .bindResourceId(122)
                                              .build();
        flow.setTenantId("58a0e82e-0ea2-4046-85c9-9e0e4b19ab21");
        assertTrue(craftFlowService.save(flow));

        CraftFlowEntity db = craftFlowService.getById(flow.getId());
        log.info("query:{}", db);
        assertEquals(db.getBindResourceId(), flow.getBindResourceId());
        assertEquals(db.getState(), DataStateEnum.ACTIVE);
        assertEquals(db.getTenantId(), flow.getTenantId());

        assertTrue(craftFlowService.removeById(flow.getId()));
        assertNull(craftFlowService.getById(flow.getId()));

        assertTrue(adminService.adminDeleteByTableAndDay("craft_flow", 0) > 0);
    }
}