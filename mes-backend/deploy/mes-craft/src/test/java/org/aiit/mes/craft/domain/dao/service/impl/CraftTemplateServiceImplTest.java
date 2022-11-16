package org.aiit.mes.craft.domain.dao.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.aiit.mes.common.constant.CraftStepTypeEnum;
import org.aiit.mes.common.constant.DataStateEnum;
import org.aiit.mes.common.constant.ParamTypeEnum;
import org.aiit.mes.craft.domain.dao.entity.CraftTemplateEntity;
import org.aiit.mes.craft.domain.dao.service.ICraftTemplateService;
import org.aiit.mes.craft.domain.vo.TemplateParamV;
import org.aiit.mes.system.admin.domain.dao.service.IAdminService;
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
 * @ClassName CraftTemplateServiceImplTest
 * @Description
 * @createTime 2021.08.26 14:04
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@Slf4j
public class CraftTemplateServiceImplTest {

    @Resource
    private ICraftTemplateService craftTemplateService;

    @Resource
    private IAdminService adminService;

    @Test
    public void removeById() {
        List<TemplateParamV> paramList = new ArrayList<>();
        TemplateParamV param = TemplateParamV.builder()
                                             .name("test_param1")
                                             .type(ParamTypeEnum.STRING)
                                             .isRequired(true)
                                             .defaultValue("")
                                             .allowModify(false)
                                             .build();
        paramList.add(param);
        param = TemplateParamV.builder()
                              .name("test_param2")
                              .type(ParamTypeEnum.INT)
                              .isRequired(false)
                              .defaultValue(0)
                              .allowModify(true)
                              .build();
        paramList.add(param);
        param = TemplateParamV.builder()
                              .name("测试参数3")
                              .type(ParamTypeEnum.BOOL)
                              .isRequired(false)
                              .build();

        paramList.add(param);

        CraftTemplateEntity craftTemplateDO = CraftTemplateEntity.builder()
                                                                 .name("测试模板")
                                                                 .isSystem(true)
                                                                 .type(CraftStepTypeEnum.MATERIAL_TRANSFER)
                                                                 .params(paramList)
                                                                 .state(DataStateEnum.ACTIVE)
                                                                 .build();
        craftTemplateDO.setTenantId("58a0e82e-0ea2-4046-85c9-9e0e4b19ab21");
        assertTrue(craftTemplateService.save(craftTemplateDO));
        CraftTemplateEntity queryResult = craftTemplateService.getById(craftTemplateDO.getId());

        log.info("query result:{}", queryResult.toString());
        assertEquals(queryResult.getName(), craftTemplateDO.getName());
        assertEquals(queryResult.getId(), craftTemplateDO.getId());
        assertEquals(queryResult.getIsSystem(), craftTemplateDO.getIsSystem());
        assertEquals(queryResult.getIsDeleted(), craftTemplateDO.getIsDeleted());
        assertEquals(queryResult.getTenantId(), craftTemplateDO.getTenantId());
        assertEquals(queryResult.getParams()
                                .size(), craftTemplateDO.getParams()
                                                        .size());

        craftTemplateService.removeById(craftTemplateDO.getId());
        assertNull(craftTemplateService.getById(craftTemplateDO.getId()));

        // 物理删除
        assertTrue(adminService.adminDeleteByTableAndDay("craft_template", 0) > 0);
    }

    @Test
    public void createSystemTemplate() {
        CraftTemplateEntity craftTemplate = CraftTemplateEntity.builder()
                                                               .name("空白物料出库模板")
                                                               .type(CraftStepTypeEnum.MATERIAL_TRANSFER)
                                                               .isSystem(true)
                                                               .state(DataStateEnum.ACTIVE)
                                                               .build();
        craftTemplate.setTenantId("58a0e82e-0ea2-4046-85c9-9e0e4b19ab21");
    }

}