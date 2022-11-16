package org.aiit.mes.factory.domain.dao.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.aiit.mes.factory.domain.dao.entity.FactoryResourceGroupRelation;
import org.aiit.mes.factory.domain.dao.service.IFactoryResourceGroupRelationService;
import org.aiit.mes.system.admin.domain.dao.service.IAdminService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author ：张少卿
 * @description：TODO
 * @date ：2021/10/28 5:08 下午
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@Slf4j
public class FactoryResourceGroupServiceImplTest {

    @Resource
    IFactoryResourceGroupRelationService service;

    @Resource
    IAdminService adminService;

    @Test
    public void test() {
        FactoryResourceGroupRelation testInsert = new FactoryResourceGroupRelation();
        testInsert.setCode("1");
        testInsert.setResourceGroupName("test" + RandomStringUtils.random(2));
        testInsert.setResourceGroupId(2L);
        assertTrue(service.save(testInsert));
        FactoryResourceGroupRelation testInsert2 = new FactoryResourceGroupRelation();
        testInsert2.setCode("1L");
        testInsert2.setResourceGroupName("test" + RandomStringUtils.random(2));
        testInsert2.setResourceGroupId(3L);
        assertTrue(service.save(testInsert2));
        FactoryResourceGroupRelation testInsert3 = new FactoryResourceGroupRelation();
        testInsert3.setCode("2L");
        testInsert3.setResourceGroupName("test" + RandomStringUtils.random(2));
        testInsert3.setResourceGroupId(3L);
        assertTrue(service.save(testInsert3));
        FactoryResourceGroupRelation testGet = service.getById(testInsert.getId());
        FactoryResourceGroupRelation testGet2 = service.getById(testInsert2.getId());
        FactoryResourceGroupRelation testGet3 = service.getById(testInsert3.getId());
        //单个查
        assertNotNull(testGet);
//        //根据List<FactoryResourceInfo>查
//        List<Integer> facIds = new ArrayList<>();
//        facIds.add(1);facIds.add(2);
//        assertEquals(3, service.listResourceGroupRelations(facIds).size());
        String nextName = "test33" + RandomStringUtils.random(2);
        testGet.setResourceGroupName(nextName);
        assertTrue(service.updateById(testGet));
        assertEquals(service.getById(testGet).getResourceGroupName(), nextName);
        assertTrue(service.removeById(testGet));
        assertTrue(service.removeById(testGet2));
        assertTrue(service.removeById(testGet3));
        assertTrue(adminService.adminDeleteByTable("fac_resource_group_relation") > 0);
        assertNull(service.getById(testGet.getId()));
    }

}
