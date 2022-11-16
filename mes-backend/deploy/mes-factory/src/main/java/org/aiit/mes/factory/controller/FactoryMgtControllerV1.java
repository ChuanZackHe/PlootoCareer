package org.aiit.mes.factory.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.aiit.mes.common.dto.WrappedHttpResponse;
import org.aiit.mes.common.module.FunctionTypeAnnotation;
import org.aiit.mes.common.module.FunctionTypeEnum;
import org.aiit.mes.factory.application.IFactoryApplication;
import org.aiit.mes.factory.domain.dao.entity.FactoryResourceInfo;
import org.aiit.mes.factory.domain.dao.service.IFactoryResourceService;
import org.aiit.mes.factory.domain.dto.*;
import org.aiit.mes.system.user.domain.dto.UserVO;
import org.aiit.mes.system.user.domain.entity.UserEntity;
import org.aiit.mes.system.util.ShiroUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author heyu
 * 工厂管理控制器
 */
@Api(tags = "V1版本-工厂（车间）管理")
@RestController
@RequestMapping("/v1/factory")
@FunctionTypeAnnotation(function = FunctionTypeEnum.FACTORY_LIVE)
public class FactoryMgtControllerV1 {

    @Resource
    private IFactoryResourceService factoryResourceService;

    @Autowired
    private IFactoryApplication plotApplication;

    @ApiOperation("批量查询工厂资源信息")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/resource/list")
    @ResponseBody
//    @RequiresPermissions("factory:resources:list")
    public WrappedHttpResponse<List<ResourceRepresentDto>> listFactoryInfo(
            @RequestBody(required = false) ListResourceFilterDto queryCondition) {
        UserVO user = ShiroUtils.getUserInfo();
        if (Objects.isNull(queryCondition)) {
            queryCondition = new ListResourceFilterDto();
            queryCondition.setPageNum(1);
            queryCondition.setPageSize(10);
        }
        if (Objects.isNull(queryCondition.getPageNum())) {
            queryCondition.setPageNum(1);
        }
        if (Objects.isNull(queryCondition.getPageSize())) {
            queryCondition.setPageSize(10);
        }
        Page<FactoryResourceInfo> queryResult = null;
        queryResult = factoryResourceService.listResourceByTenantId(user.getTenantId(), queryCondition);

        return WrappedHttpResponse.successPageRepresent(queryResult);
    }

    @ApiOperation("查询指定工厂资源的下属资源信息")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/child-resources/list")
    @ResponseBody
//    @RequiresPermissions("factory:child-resources:list")
    public WrappedHttpResponse<List<ResourceRepresentDto>> listChildResource(@RequestBody FactoryResourcePager pager) {
        Map<String, Object> params = new HashMap<>();
        String tenantId = ShiroUtils.getUserTenantId();
        params.put("parent_code", pager.getResourceCode());
        params.put("tenant_id", tenantId);

        Page<FactoryResourceInfo> queryResult = null;
        queryResult = factoryResourceService.listChildResourcePages(params, pager.getPageNo(), pager.getPageSize());
        return WrappedHttpResponse.successPageRepresent(queryResult);
    }

    @ApiOperation(value = "新增工厂资源", tags = {"UPDATE"})
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/resource/add")
    @ResponseBody
    @RequiresPermissions(value = {"factory:resource:add"})
    public WrappedHttpResponse<ResourceDetailDto> showResourceDetail(@RequestBody ResourceDetailDto resourseDetail) {
        FactoryResourceInfo resourceInfo = resourseDetail.toEntity();
        Boolean ret = factoryResourceService.createResource(resourceInfo);
        if (ret) {
            return WrappedHttpResponse.success(resourceInfo.toDetail());
        }
        return WrappedHttpResponse.fail(resourceInfo.toDetail());

    }

    @ApiOperation("查询指定工厂资源的详细信息")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/resource/{resourceCode}/show")
    @ResponseBody
//    @RequiresPermissions("factory:resource:show")
    public WrappedHttpResponse<ResourceDetailDto> showResourceDetail(@PathVariable String resourceCode) {
        ResourceDetailDto resourceInfo;
        resourceInfo = factoryResourceService.showResourceDetail(resourceCode);

        return WrappedHttpResponse.success(resourceInfo);
    }

    @ApiOperation(value = "更新指定工厂资源的详细信息", tags = {"UPDATE"})
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/resource-detail/update")
    @ResponseBody
    @RequiresPermissions(value = {"factory:resource-detail:update"})
    public WrappedHttpResponse<Boolean> updateResourceDetail(@RequestBody ResourceDetailDto resourceInfo) {
        Boolean res = factoryResourceService.updateResourceDetail(resourceInfo);

        if (res) {
            return WrappedHttpResponse.success(Boolean.TRUE);
        }
        return WrappedHttpResponse.fail(res);
    }

    @ApiOperation(value = "删除指定工厂资源", tags = {"UPDATE"})
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/resource/{resourceCode}/delete")
    @ResponseBody
    @RequiresPermissions(value = {"factory:resource:delete"})
    public WrappedHttpResponse<Boolean> deleteResourceDetail(@PathVariable String resourceCode) {
        Boolean res = factoryResourceService.deleteResourceDetail(resourceCode);

        if (res) {
            return WrappedHttpResponse.success(res);
        }
        return WrappedHttpResponse.fail(res);
    }

    @ApiOperation("获取指定资源组下面的工厂资源")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/resource/group/{name}/list")
    @ResponseBody
//    @RequiresPermissions("factory:resource:group:list")
    public WrappedHttpResponse<List<ResourceRepresentDto>> listByResourceGroupName(@PathVariable String name) {
        return WrappedHttpResponse.success(factoryResourceService.listByResourceGroupName(name).stream().map(FactoryResourceInfo::toRepresent).collect(
                Collectors.toList()));
    }

    @ApiOperation(value = "保存工厂资源层级概况", tags = {"UPDATE"})
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/factory-summary/save")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    @RequiresPermissions(value = {"factory:factory-summary:save"})
    public WrappedHttpResponse<FactoryPlotAggDto> savePlot(@RequestBody FactoryPlotAggDto aggDto) {
        String tenantId = ShiroUtils.getUserInfo()
                                    .getTenantId();
        String userName = ShiroUtils.getUserInfo()
                                    .getUsername();

        Boolean ret = plotApplication.savePlot(aggDto, tenantId, userName);
        if (ret) {
            return WrappedHttpResponse.success(plotApplication.showPlot(aggDto.getCode(), tenantId));
        }
        return WrappedHttpResponse.fail(aggDto);
    }

    @ApiOperation("获取指定工厂资源的层级概况")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/factory-summary/{parentCode}/show")
    @ResponseBody
//    @RequiresPermissions("factory:factory-summary:show")
    public WrappedHttpResponse<FactoryPlotAggDto> showPlot(@PathVariable String parentCode) {
        String tenantId = ShiroUtils.getUserInfo()
                                    .getTenantId();

        FactoryPlotAggDto plot = plotApplication.showPlot(parentCode, tenantId);

        return WrappedHttpResponse.success(plot);

    }

}
