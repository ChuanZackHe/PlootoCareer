package org.aiit.mes.factory.domain.dao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.aiit.mes.common.constant.DataStateEnum;
import org.aiit.mes.common.constant.FactoryResourceTypeEnum;
import org.aiit.mes.common.exception.HasChildException;
import org.aiit.mes.common.util.DaysUtil;
import org.aiit.mes.factory.application.IFactoryApplication;
import org.aiit.mes.factory.domain.dao.entity.FactoryResourceInfo;
import org.aiit.mes.factory.domain.dao.entity.FactoryResourceRelation;
import org.aiit.mes.factory.domain.dao.mapper.IFactoryResourceMapper;
import org.aiit.mes.factory.domain.dao.mapper.IFactoryResourceRelationMapper;
import org.aiit.mes.factory.domain.dao.service.IFactoryResourceService;
import org.aiit.mes.factory.domain.dto.*;
import org.aiit.mes.system.admin.domain.dao.service.IAdminService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.aiit.mes.common.util.ObjectCompareUtil.compareObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * @author lzj
 * @version 1.0.0
 * @ClassName FactoryResourceServiceTest.java
 * @Description 工厂管理测试UT
 * @createTime 2021年08月16日 16:20:00
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@Slf4j
public class FactoryResourceServiceImplTest {

    private final String deletedDay = DaysUtil.getDate(1);

    @Resource
    IFactoryResourceService resourceService;

    @Resource
    IFactoryApplication plotApplication;

    @Resource
    IFactoryResourceMapper resourceMapper;

    @Resource
    IFactoryResourceRelationMapper relationMapper;

    @Resource
    IAdminService adminService;

    String ancestorCode = "CF99999999";

    String tenantId = "58a0e82e-0ea2-4046-85c9-9e0e4b19ab21";

    String userName = "admin";

    FactoryResourceInfo factoryInfo;

    FactoryResourceInfo workShopInfo1;

    FactoryResourceInfo workShopInfo2;

    FactoryResourceInfo workShopInfo3;

    FactoryResourceInfo machineInfo1;

    FactoryResourceInfo machineInfo2;

    FactoryResourceInfo machineInfo3;

    FactoryResourceInfo workShopToInsert;

    FactoryResourceRelation relation;

    FactoryResourceRelation relation2;

    FactoryResourceRelation wrongRelation;

//    private final dbStatusActive = new SimpleDateFormat("YYYY")

    @BeforeEach
    public void setUp() {
        factoryInfo = FactoryResourceInfo.builder()
                                         .code(ancestorCode)
                                         .parentCode("EMPTY")
                                         .ancestorCode(ancestorCode)
                                         .name("测试厂房")
                                         .type(FactoryResourceTypeEnum.FACTORY.getType())
//                                         .isDeleted(Constants.DB_STATUS_ACTIVE)
                                         .state(DataStateEnum.ACTIVE)
                                         .build();
        factoryInfo.setTenantId(tenantId);

        workShopInfo1 = FactoryResourceInfo.builder()
                                           .code("CJ99999999")
                                           .parentCode(ancestorCode)
                                           .ancestorCode(ancestorCode)
                                           .name("测试车间1")
                                           .type(FactoryResourceTypeEnum.WORKSHOP.getType())
//                                           .isDeleted(null)
                                           .state(DataStateEnum.ACTIVE)
                                           .build();
        workShopInfo1.setTenantId(tenantId);

        workShopInfo2 = FactoryResourceInfo.builder()
                                           .code("CJ99999998")
                                           .parentCode(ancestorCode)
                                           .ancestorCode(ancestorCode)
                                           .name("测试车间2")
                                           .type(FactoryResourceTypeEnum.WORKSHOP.getType())
//                                           .isDeleted(null)
                                           .state(DataStateEnum.ACTIVE)
                                           .build();
        workShopInfo2.setTenantId(tenantId);

        workShopInfo3 = FactoryResourceInfo.builder()
                                           .code("CJ99999997")
                                           .parentCode(ancestorCode)
                                           .ancestorCode(ancestorCode)
                                           .name("测试车间3")
                                           .type(FactoryResourceTypeEnum.WORKSHOP.getType())
//                                           .isDeleted(null)
                                           .state(DataStateEnum.ACTIVE)
                                           .build();
        workShopInfo3.setTenantId(tenantId);

        workShopToInsert = FactoryResourceInfo.builder()
                                              .code("CJ99999996")
                                              .parentCode(ancestorCode)
                                              .ancestorCode(ancestorCode)
                                              .name("测试插入用车间")
                                              .type(FactoryResourceTypeEnum.WORKSHOP.getType())
//                                              .isDeleted(null)
                                              .state(DataStateEnum.ACTIVE)
                                              .build();
        workShopToInsert.setTenantId(tenantId);

        machineInfo1 = FactoryResourceInfo.builder()
                                          .code("JQ99999999")
                                          .parentCode(workShopInfo1.getCode())
                                          .ancestorCode(factoryInfo.getCode())
                                          .name("测试机器1")
                                          .type(FactoryResourceTypeEnum.MACHINE.getType())
                                          .state(DataStateEnum.ACTIVE)
                                          .xRel(15)
                                          .yRel(-5)
//                                          .isDeleted(null)
                                          .build();
        machineInfo1.setTenantId(tenantId);

        machineInfo2 = FactoryResourceInfo.builder()
                                          .code("JQ99999998")
                                          .parentCode(workShopInfo1.getCode())
                                          .ancestorCode(factoryInfo.getCode())
                                          .name("测试机器2")
                                          .type(FactoryResourceTypeEnum.MACHINE.getType())
                                          .state(DataStateEnum.ACTIVE)
                                          .xRel(100)
                                          .yRel(-50)
//                                          .isDeleted(null)
                                          .build();
        machineInfo2.setTenantId(tenantId);

        machineInfo3 = FactoryResourceInfo.builder()
                                          .code("JQ99999997")
                                          .parentCode(workShopInfo1.getCode())
                                          .ancestorCode(factoryInfo.getCode())
                                          .name("测试机器3")
                                          .type(FactoryResourceTypeEnum.MACHINE.getType())
                                          .state(DataStateEnum.ACTIVE)
                                          .xRel(1000)
                                          .yRel(-500)
//                                          .isDeleted(null)
                                          .build();
        machineInfo3.setTenantId(tenantId);

        relation = FactoryResourceRelation.builder()
                                          .preCode(machineInfo1.getCode())
                                          .afterCode(machineInfo2.getCode())
                                          .build();
        relation.setTenantId(tenantId);
        relation.setCreateUsername(userName);
        relation.setUpdateUsername(userName);

        relation2 = FactoryResourceRelation.builder()
                                           .preCode(machineInfo2.getCode())
                                           .afterCode(machineInfo3.getCode())
                                           .build();
        relation2.setTenantId(tenantId);
        relation2.setCreateUsername(userName);
        relation2.setUpdateUsername(userName);

        wrongRelation = FactoryResourceRelation.builder()
                                               .preCode(workShopInfo1.getCode())
                                               .afterCode(workShopInfo1.getCode())
                                               .build();
        wrongRelation.setTenantId(tenantId);
        wrongRelation.setCreateUsername(userName);
        wrongRelation.setUpdateUsername(userName);


        resourceMapper.insert(factoryInfo);
        resourceMapper.insert(workShopInfo1);
        resourceMapper.insert(workShopInfo2);
        resourceMapper.insert(workShopInfo3);
//        factoryInfo.insert();
//        workShopInfo1.insert();
//        workShopInfo2.insert();
//        workShopInfo3.insert();
    }

    @AfterEach
    public void tearDown() {
        factoryInfo.deleteById();
        workShopInfo1.deleteById();
        workShopInfo2.deleteById();
        workShopInfo3.deleteById();
        if (Objects.nonNull(workShopToInsert.getId())) {
            workShopToInsert.deleteById();
        }

        machineInfo1.delete(new QueryWrapper<FactoryResourceInfo>().eq("code", machineInfo1.getCode()));
        machineInfo2.delete(new QueryWrapper<FactoryResourceInfo>().eq("code", machineInfo2.getCode()));
        machineInfo3.delete(new QueryWrapper<FactoryResourceInfo>().eq("code", machineInfo3.getCode()));

        Map<String, Object> params = new HashMap<>();
        params.put("tableName", "fac_resource_info");
        params.put("dateBefore", deletedDay);

        assert adminService.adminDeleteDeleted(params) > 0;

        deleteIfExistRelation(wrongRelation);

        deleteIfExistRelation(relation);

        deleteIfExistRelation(relation2);
    }

    @Test
    public void listChildResource() {
        Map<String, Object> params = new HashMap<>();

        params.put("parent_code", ancestorCode);
        params.put("tenant_id", tenantId);
        List<ResourceRepresentDto> pageQuery1 =
                resourceService.listChildResourcePages(params, 1, 2)
                               .getRecords()
                               .stream()
                               .map(FactoryResourceInfo::toRepresent)
                               .collect(
                                       Collectors.toList());
        List<ResourceRepresentDto> validatedPage = Stream.of(workShopInfo1, workShopInfo2)
                                                         .map(FactoryResourceInfo::toRepresent)
                                                         .collect(Collectors.toList());
        for (int i = 0; i < pageQuery1.size(); i++) {
            assert compareObject(pageQuery1.get(i), validatedPage.get(i));
        }

        ResourceRepresentDto pageQuery2 = resourceService.listChildResourcePages(params, 2, 2)
                                                         .getRecords()
                                                         .stream()
                                                         .map(FactoryResourceInfo::toRepresent)
                                                         .collect(Collectors.toList())
                                                         .get(0);
        assert compareObject(pageQuery2, workShopInfo3.toRepresent());
    }

    @Test
    public void listResourceByTenantId() {
        ListResourceFilterDto childFilter = ListResourceFilterDto.builder()
                                                                 .parentCode(ancestorCode)
                                                                 .state(DataStateEnum.ACTIVE)
                                                                 .type(FactoryResourceTypeEnum.WORKSHOP.getType())
                                                                 .pageNum(1)
                                                                 .pageSize(10)
                                                                 .build();

        ListResourceFilterDto ancestorFilter = ListResourceFilterDto.builder()
                                                                    .code(ancestorCode)
                                                                    .type(FactoryResourceTypeEnum.FACTORY.getType())
                                                                    .name(factoryInfo.getName())
                                                                    .parentCode("EMPTY")
                                                                    .ancestorCode(ancestorCode)
                                                                    .state(DataStateEnum.ACTIVE)
                                                                    .pageNum(1)
                                                                    .pageSize(10)
                                                                    .build();

        List<ResourceRepresentDto> childRes =
                resourceService.listResourceByTenantId(tenantId, childFilter)
                               .getRecords()
                               .stream()
                               .map(
                                       FactoryResourceInfo::toRepresent)
                               .collect(Collectors.toList());
        List<ResourceRepresentDto> validatdChildRes =
                Stream.of(workShopInfo1, workShopInfo2, workShopInfo3)
                      .map(FactoryResourceInfo::toRepresent)
                      .collect(
                              Collectors.toList());
        for (int i = 0; i < childRes.size(); i++) {
            assert compareObject(childRes.get(i), validatdChildRes.get(i));
        }
        Page<FactoryResourceInfo> page = resourceService.listResourceByTenantId(tenantId, ancestorFilter);
//        ResourceRepresentDto ancestor =
//                resourceService.listResourceByTenantId(tenantId, ancestorFilter)
//                               .getRecords()
//                               .get(0)
//                               .toRepresent();
        ResourceRepresentDto ancestor = page.getRecords().get(0).toRepresent();

        assert compareObject(ancestor, factoryInfo.toRepresent());

    }

    @Test
    public void showResourceDetail() {
        ResourceDetailDto detail = resourceService.showResourceDetail(factoryInfo.getCode());
        assert compareObject(detail, factoryInfo.toDetail());
    }

    @Test
    public void updateResourceDetail() {
        factoryInfo.setName("修改后的测试工厂");

        Boolean ret = resourceService.updateResourceDetail(factoryInfo.toDetail());
        assertTrue(ret);
    }

    @Test
    public void createResource() {
        Boolean ret = resourceService.createResource(workShopToInsert);
        assertTrue(ret);
    }


    @Test
    public void deleteResourceDetail() throws HasChildException {
        createResource();
        Boolean ret = resourceService.deleteResourceDetail(workShopToInsert.getCode());
        assertTrue(ret);
        Assertions.assertThrows(HasChildException.class,
                                () -> resourceService.deleteResourceDetail(factoryInfo.getCode()));
    }

    @Test()
    public void saveAndShowPlot() {
        String parentCode = workShopInfo1.getCode();
        String wrongParentCode = workShopInfo2.getCode();

        List<ResourceDetailDto> details =
                Stream.of(machineInfo1, machineInfo2, machineInfo3)
                      .map(FactoryResourceInfo::toDetail)
                      .collect(
                              Collectors.toList());

        List<ResourceRelationDto> relations =
                Stream.of(relation.toRepresent())
                      .collect(Collectors.toList());

        List<ResourceRelationDto> wrongRelations =
                Stream.of(wrongRelation.toRepresent())
                      .collect(Collectors.toList());

        FactoryPlotAggDto plotDto = new FactoryPlotAggDto(parentCode, details, relations, null);

        Boolean ret = plotApplication.savePlot(plotDto, tenantId, userName);
        assertTrue(ret);


        machineInfo1.setName("修改信息的车间");
        List<ResourceDetailDto> updateDetails =
                Stream.of(machineInfo1, machineInfo2, machineInfo3)
                      .map(FactoryResourceInfo::toDetail)
                      .collect(Collectors.toList());

        plotDto = new FactoryPlotAggDto(parentCode, updateDetails, relations, null);
        ret = plotApplication.savePlot(plotDto, tenantId, userName);

        assertTrue(ret);

        FactoryResourceInfo updated = resourceMapper.selectOne(new QueryWrapper<FactoryResourceInfo>().eq("code",
                                                                                                          machineInfo1.getCode()));
        assertEquals(updated.getName(), machineInfo1.getName());

        List<ResourceDetailDto> delDetails =
                Stream.of(machineInfo1, machineInfo2)
                      .map(FactoryResourceInfo::toDetail)
                      .collect(Collectors.toList());

        plotDto = new FactoryPlotAggDto(parentCode, delDetails, relations, null);
        ret = plotApplication.savePlot(plotDto, tenantId, userName);
        assertTrue(ret);
        assert Objects.isNull(resourceMapper.selectOne(new QueryWrapper<FactoryResourceInfo>().eq("code",
                                                                                                  machineInfo3.getCode())));

        relations.add(relation2.toRepresent());
        plotDto.setFrontRelations(relations);
        ret = plotApplication.savePlot(plotDto, tenantId, userName);

        assertTrue(
                Objects.nonNull(relationMapper.selectOne(new QueryWrapper<FactoryResourceRelation>().eq("pre_code"
                                                                                                            ,
                                                                                                        relation2.getPreCode())
                                                                                                    .eq("after_code",
                                                                                                        relation2.getAfterCode()))));

        relations.remove(relations.size() - 1);
        plotDto.setFrontRelations(relations);
        ret = plotApplication.savePlot(plotDto, tenantId, userName);

        assertTrue(
                Objects.isNull(relationMapper.selectOne(new QueryWrapper<FactoryResourceRelation>().eq("pre_code"
                                                                                                           ,
                                                                                                       relation2.getPreCode())
                                                                                                   .eq("after_code",
                                                                                                       relation2.getAfterCode()))));

        FactoryPlotAggDto plot = plotApplication.showPlot(parentCode, tenantId);

        assertTrue(compareObject(plot, plotDto));

    }

    private void deleteIfExistRelation(FactoryResourceRelation relation) {
        QueryWrapper<FactoryResourceRelation> wrapper = new QueryWrapper<>();
        if (relationMapper.selectCount(wrapper.eq("tenant_id", tenantId)
                                              .eq("pre_code",
                                                  relation.getPreCode())
                                              .eq("after_code",
                                                  relation.getAfterCode())) == 1) {
            relationMapper.delete(wrapper.eq("tenant_id", tenantId)
                                         .eq("pre_code",
                                             relation.getPreCode())
                                         .eq("after_code",
                                             relation.getAfterCode()));
        }
    }

}