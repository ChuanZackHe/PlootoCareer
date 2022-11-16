package org.aiit.mes.craft.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.aiit.mes.common.dto.WrappedHttpResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.aiit.mes.common.module.FunctionTypeAnnotation;
import org.aiit.mes.common.module.FunctionTypeEnum;
import org.aiit.mes.craft.application.ICraftCmdApplication;
import org.aiit.mes.craft.application.ICraftQueryApplication;
import org.aiit.mes.craft.domain.dao.entity.CraftComponentEntity;
import org.aiit.mes.craft.domain.dao.service.ICraftTemplateService;
import org.aiit.mes.craft.domain.dto.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author heyu
 * 工艺管理控制器
 */
@Api(tags = "V1版本-工艺步骤管理")
@RestController
@RequestMapping("/v1/craft")
@Validated
@FunctionTypeAnnotation(function = FunctionTypeEnum.CRAFT_STEP)
public class CraftComponentControllerV1 {

    private static final Logger logger = LoggerFactory.getLogger(CraftComponentControllerV1.class);

    @Resource
    private ICraftCmdApplication craftCmdApplication;

    @Resource
    private ICraftQueryApplication craftQueryApplication;

    @Resource
    private ICraftTemplateService craftTemplateService;

    @ApiOperation("批量查询模板信息")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/template/list")
    @ResponseBody
//    @RequiresPermissions("craft:template:list")
    public WrappedHttpResponse<List<CraftComponentRepresent>> listTemplate() {
        // todo 为了方便前端统一参数，先用固定模板
        return WrappedHttpResponse.success(CraftComponentRepresent.TEMPLATES);
    }

    @ApiOperation("批量分页指定组件信息")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/component/list")
    @ResponseBody
//    @RequiresPermissions("craft:component:list")
    public WrappedHttpResponse<List<CraftComponentRepresent>> pageListComponent(
            @RequestBody(required = false)
            @ApiParam(value = "批量查询组件过滤器", required = false)
            @Validated
                    CraftComponentQuery query) {
        Page<CraftComponentEntity> ret = craftQueryApplication.pageListComponents(query);

        // TODO： 增加过滤条件映射到 ListTemplateFilterDto
        return WrappedHttpResponse.successPage(ret,
                                               ret.getRecords()
                                                  .stream()
                                                  .map(CraftComponentEntity::toRepresent)
                                                  .collect(Collectors.toList()));
    }

    @ApiOperation("查询指定组件信息")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/component/{component-id}/show")
    @ResponseBody
//    @RequiresPermissions("craft:component:show")
    public WrappedHttpResponse<CraftComponentRepresent> showComponent(
            @PathVariable("component-id") @Min(1) Long id) {
        return WrappedHttpResponse.success(craftCmdApplication.getComponentById(id).toRepresent());
    }

    @ApiOperation(value = "删除指定组件", tags = {"UPDATE"})
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/component/{component-id}/delete")
    @ResponseBody
    @RequiresPermissions(value = {"craft:component:delete"})
    public WrappedHttpResponse<Long> deleteComponent(@PathVariable("component-id") @Min(1) Long id) {
        return WrappedHttpResponse.success(craftCmdApplication.getComponentById(id).delete());
    }

    @ApiOperation(value = "更新指定组件", tags = {"UPDATE"})
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/component/{component-id}/update")
    @ResponseBody
    @RequiresPermissions(value = {"craft:component:update"})
    public WrappedHttpResponse<CraftComponentRepresent> deleteComponent(
            @PathVariable("component-id") @Min(1) Long id,
            @Validated @RequestBody CraftComponentUpdateCmd updateCmd) {
        updateCmd.compareId(id);
        return WrappedHttpResponse.success(craftCmdApplication.updateComponent(updateCmd).toRepresent());
    }

    @ApiOperation(value = "新建组件", tags = {"UPDATE"})
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/component/create")
    @ResponseBody
    @RequiresPermissions(value = {"craft:component:create"})
    public WrappedHttpResponse<CraftComponentRepresent> createComponent(
            @RequestBody
            @Validated CraftComponentCreateCmd componentCreateCmd) {
        return WrappedHttpResponse.success(craftCmdApplication.saveNewComponent(componentCreateCmd).toRepresent());
    }
}
