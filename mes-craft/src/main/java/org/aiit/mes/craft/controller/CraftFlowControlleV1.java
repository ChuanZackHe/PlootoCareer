package org.aiit.mes.craft.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.aiit.mes.common.dto.WrappedHttpResponse;
import org.aiit.mes.common.module.FunctionTypeAnnotation;
import org.aiit.mes.common.module.FunctionTypeEnum;
import org.aiit.mes.common.primitive.LongId;
import org.aiit.mes.craft.application.ICraftCmdApplication;
import org.aiit.mes.craft.application.ICraftQueryApplication;
import org.aiit.mes.craft.domain.dao.entity.CraftFlowEntity;
import org.aiit.mes.craft.domain.dto.*;
import org.aiit.mes.craft.domain.vo.CraftBomTreeRepresentV;
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
 * @author ：张少卿
 * @description：TODO
 * @date ：2022/2/15 10:21 上午
 */
@Api(tags = "V1版本-工艺流程管理")
@RestController
@RequestMapping("/v1/craft")
@Validated
@FunctionTypeAnnotation(function = FunctionTypeEnum.CRAFT_FLOW)
public class CraftFlowControlleV1 {

    private static final Logger logger = LoggerFactory.getLogger(CraftComponentControllerV1.class);

    @Resource
    private ICraftCmdApplication craftCmdApplication;

    @Resource
    private ICraftQueryApplication craftQueryApplication;

    @ApiOperation(value = "删除工艺流程", tags = {"UPDATE"})
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/flow/{flow-id}/delete")
    @ResponseBody
    @RequiresPermissions(value = {"craft:flow:delete"})
    public WrappedHttpResponse<Long> deleteFlow(@PathVariable("flow-id") @Min(1) Long id) {
        LongId flowId = new LongId(id);
        if (craftCmdApplication.deleteFlowById(flowId)) {
            return WrappedHttpResponse.success(flowId.getId());
        }
        return WrappedHttpResponse.fail(flowId.getId());
    }

    @ApiOperation("查询工艺流程")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/flow/{flow-id}/show")
    @ResponseBody
//    @RequiresPermissions("craft:flow:show")
    public WrappedHttpResponse<CraftFlowRepresent> showFlow(@PathVariable("flow-id") @Min(1) Long id) {
        LongId flowId = new LongId(id);
        return WrappedHttpResponse.success(craftQueryApplication.showFlowById(flowId)
                                                                .toRepresent());
    }


    @ApiOperation("批量分页查询工艺流程简要信息")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/flow/list")
    @ResponseBody
//    @RequiresPermissions("craft:flow:list")
    public WrappedHttpResponse<List<CraftFlowRepresent>> pageListFlowSummary(
            @RequestBody(required = false)
            @ApiParam(value = "批量查询流程过滤器", required = false)
            @Validated
                    CraftFlowQuery query) {
        Page<CraftFlowEntity> ret = craftQueryApplication.pageListFlowSummary(query);

        // TODO： 增加过滤条件映射到 ListTemplateFilterDto
        return WrappedHttpResponse.successPage(ret,
                                               ret.getRecords()
                                                  .stream()
                                                  .map(CraftFlowEntity::toRepresent)
                                                  .collect(Collectors.toList()));
    }

    @ApiOperation(value = "创建工艺流程", tags = {"UPDATE"})
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/flow/create")
    @ResponseBody
    @RequiresPermissions(value = {"craft:flow:create"})
    public WrappedHttpResponse<CraftFlowRepresent> createFlow(@RequestBody @Validated CraftFlowCreateCmd createCmd) {
        return WrappedHttpResponse.success(craftCmdApplication.saveNewFlow(createCmd).toRepresent());
    }

    @ApiOperation(value = "更新工艺流程", tags = {"UPDATE"})
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/flow/{flow-id}/update")
    @ResponseBody
    @RequiresPermissions(value = {"craft:flow:update"})
    public WrappedHttpResponse<CraftFlowRepresent> updateFlow(@PathVariable("flow-id") @Min(1) Long id,
                                                              @RequestBody @Validated CraftFlowUpdateCmd updateCmd) {
        updateCmd.compareId(id);
        return WrappedHttpResponse.success(craftCmdApplication.updateFlow(updateCmd).toRepresent());
    }

    @ApiOperation(value = "复制工艺流程，返回保存的工艺流程详情", tags = {"UPDATE"})
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/flow/copy")
    @ResponseBody
    @RequiresPermissions(value = {"craft:flow:copy"})
    public WrappedHttpResponse<CraftFlowRepresent> copyFlow(@RequestBody @Validated CraftFlowCopyCmd copyCmd) {
        return WrappedHttpResponse.success(craftCmdApplication.copyFlow(copyCmd).toRepresent());
    }


    @ApiOperation(value = "更新工艺流程图", tags = {"UPDATE"})
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/flow/{flow-id}/graph/update")
    @ResponseBody
    @RequiresPermissions(value = {"craft:flow-graph:update"})
    public WrappedHttpResponse<CraftFlowRepresent> updateFlowGraph(@PathVariable("flow-id") @Min(1) Long id,
                                                                   @RequestBody @Validated
                                                                           CraftFlowGraphUpdateCmd graphUpdateCmd) {
        graphUpdateCmd.compareId(id);
        return WrappedHttpResponse.success(craftCmdApplication.updateFlowGraph(graphUpdateCmd).toRepresent());
    }


    @ApiOperation(value = "需求分析：根据流程id、物料id及数量，分析生产所需的原料库存是否满足", tags = {"UPDATE"})
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/bom/analyze")
    @ResponseBody
    @RequiresPermissions(value = {"craft:bom:analyze"})
    public WrappedHttpResponse<CraftBomTreeRepresentV> analyzeBomforProduce(
            @Validated @RequestBody AnalyzeBomForProduceQuery query) {
        CraftBomTreeRepresentV ret = craftQueryApplication.analyzeBomForProduce(query);
        return WrappedHttpResponse.success(ret);
    }

}
