package org.aiit.mes.craft.domain.dao.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.aiit.mes.common.util.UUIDUtil;
import org.aiit.mes.craft.domain.dao.entity.CraftFlowRelationEntity;
import org.aiit.mes.craft.domain.dao.service.ICraftFlowRelationService;
import org.aiit.mes.craft.domain.vo.FlowNodeRelationV;
import org.aiit.mes.system.admin.domain.dao.service.IAdminService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName CraftFlowRelationServiceImplTest
 * @Description
 * @createTime 2021.08.31 16:54
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@Slf4j
public class CraftFlowRelationServiceImplTest {

    @Resource
    IAdminService adminService;

    @Resource
    private ICraftFlowRelationService craftFlowRelationService;

    @Test
    public void removeById() {
        long flowId = 4L;
        CraftFlowRelationEntity relation = CraftFlowRelationEntity.builder()
                                                                  .flowId(flowId)
                                                                  .build()
                                                                  .addRelations(FlowNodeRelationV.newRelation(
                                                                          UUIDUtil.newUUID(), StringUtils.EMPTY))
                                                                  .addRelations(FlowNodeRelationV.newRelation(
                                                                          StringUtils.EMPTY, UUIDUtil.newUUID()))
                                                                  .addRelations(FlowNodeRelationV.newRelation(
                                                                          UUIDUtil.newUUID(), UUIDUtil.newUUID()));
        relation.setTenantId("58a0e82e-0ea2-4046-85c9-9e0e4b19ab21");

        assertTrue(craftFlowRelationService.save(relation));
        CraftFlowRelationEntity db = craftFlowRelationService.getById(relation.getId());
        log.info("query:{}", db);
        assertEquals(db.getFlowId(), relation.getFlowId());
        assertEquals(db.getId(), relation.getId());
        assertEquals(db.getNodeRelations()
                       .size(), relation.getNodeRelations()
                                        .size());

        List<String> dbRelationList = db.getNodeRelations()
                                        .stream()
                                        .map(FlowNodeRelationV::toString)
                                        .collect(Collectors.toList());

        relation.getNodeRelations()
                .stream()
                .map(FlowNodeRelationV::toString)
                .forEach(r -> {assertTrue(dbRelationList.remove(r));});

        assertTrue(CollectionUtils.isEmpty(dbRelationList));

        assertTrue(craftFlowRelationService.removeById(relation.getId()));

        assertNull(craftFlowRelationService.getById(relation.getId()));

        assertTrue(adminService.adminDeleteByTable("craft_flow_relation") > 0);
    }
}