package org.aiit.mes.order.controller;

import org.aiit.mes.common.dto.WrappedHttpResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.aiit.mes.common.module.FunctionTypeAnnotation;
import org.aiit.mes.common.module.FunctionTypeEnum;
import org.aiit.mes.order.application.IOrderCmdApplication;
import org.aiit.mes.order.application.IOrderQueryApplication;
import org.aiit.mes.order.domain.dto.delivery_detail.*;
import org.aiit.mes.order.domain.dto.delivery_summary.DeliverySummaryCreateCmd;
import org.aiit.mes.order.domain.dto.delivery_summary.DeliverySummaryQuery;
import org.aiit.mes.order.domain.dto.delivery_summary.DeliverySummaryRepresent;
import org.aiit.mes.order.domain.dto.delivery_summary.DeliverySummaryUpdateCmd;
import org.aiit.mes.warehouse.stockout.domain.dto.StockOutSummaryCreateCmd;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ：张少卿
 * @description：TODO
 * @date ：2022/1/17 4:00 下午
 */
@Api(tags = "V1版本-交付单管理")
@RestController
@RequestMapping("/v1/order")
@Validated
@FunctionTypeAnnotation(function = FunctionTypeEnum.DELIVERY_MAINTAIN)
public class DeliveryControllerV1 {

    @Resource
    private IOrderCmdApplication orderCmdApplication;

    @Resource
    private IOrderQueryApplication orderQueryApplication;

    @ApiOperation("批量查询交付单总单信息")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/delivery/summary/list")
    @ResponseBody
//    @RequiresPermissions("delivery:summary:list")
    public WrappedHttpResponse<List<DeliverySummaryRepresent>> listDeliveryInfo(
            @RequestBody @Validated DeliverySummaryQuery queryCondition) {
        return WrappedHttpResponse.successPageRepresent(orderQueryApplication.listDeliveryInfo(queryCondition));
    }

    @ApiOperation("查询交付单总单详情")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/delivery/summary/{documentId}/show")
    @ResponseBody
//    @RequiresPermissions("delivery:summary:show")
    public WrappedHttpResponse<DeliverySummaryRepresent> listDeliveryInfo(
            @PathVariable String documentId) {
        return WrappedHttpResponse.success(orderQueryApplication.showDeliveryInfo(documentId).toRepresent());
    }

    @ApiOperation(value = "新增交付单总单", tags = {"UPDATE"})
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/delivery/summary/add")
    @ResponseBody
    @RequiresPermissions(value = {"delivery:summary:add"})
    public WrappedHttpResponse<DeliverySummaryRepresent> addDeliverySummary(
            @RequestBody @Validated DeliverySummaryCreateCmd deliverySummaryCreateCmd) {
        return WrappedHttpResponse.success(
                orderCmdApplication.addDeliverySummary(deliverySummaryCreateCmd));
    }

    @ApiOperation(value = "修改交付单总单", tags = {"UPDATE"})
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/delivery/summary/update")
    @ResponseBody
    @RequiresPermissions(value = {"delivery:summary:update"})
    public WrappedHttpResponse<DeliverySummaryRepresent> updateDeliverySummary(
            @RequestBody @Validated DeliverySummaryUpdateCmd deliverySummaryUpdateCmd) {
        return WrappedHttpResponse.success(orderCmdApplication.updateDeliverySummary(deliverySummaryUpdateCmd));
    }

    @ApiOperation(value = "删除交付单总单", tags = {"UPDATE"})
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/delivery/{documentId}/delete")
    @ResponseBody
    @RequiresPermissions(value = {"delivery:summary:delete"})
    public WrappedHttpResponse<String> deleteDeliverySummary(@PathVariable String documentId) {
        return WrappedHttpResponse.success(orderCmdApplication.deleteDeliverySummary(documentId));
    }

    @ApiOperation("批量查询交付单子单信息")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/delivery/detail/list")
    @ResponseBody
//    @RequiresPermissions("delivery:detail:list")
    public WrappedHttpResponse<List<DeliveryDetailRepresent>> listDeliveryDetailInfo(
            @RequestBody @Validated DeliveryDetailQuery queryCondition) {
        return WrappedHttpResponse.successPage(orderQueryApplication.listDeliveryDetailInfo(queryCondition));
    }

    @ApiOperation(value = "新增交付单子单", tags = {"UPDATE"})
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/delivery/detail/{documentId}/add")
    @ResponseBody
    @RequiresPermissions(value = {"delivery:detail:add"})
    public WrappedHttpResponse<DeliveryDetailRepresent> addDeliveryDetail(
            @RequestBody @Validated DeliveryDetailCreateCmd deliveryDetailCreateCmd,
            @PathVariable String documentId) {
        return WrappedHttpResponse.success(orderCmdApplication.addDeliveryDetail(deliveryDetailCreateCmd));
    }

    @ApiOperation(value = "修改交付单子单", tags = {"UPDATE"})
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/delivery/detail/update")
    @ResponseBody
    @RequiresPermissions(value = {"delivery:detail:update"})
    public WrappedHttpResponse<DeliveryDetailRepresent> updateDeliveryDetail(
            @RequestBody @Validated DeliveryDetailUpdateCmd deliveryDetailUpdateCmd) {
        return WrappedHttpResponse.success(orderCmdApplication.updateDeliveryDetail(deliveryDetailUpdateCmd));
    }

    @ApiOperation(value = "删除交付单子单", tags = {"UPDATE"})
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/delivery/detail/{documentId}/{id}/delete")
    @ResponseBody
    @RequiresPermissions(value = {"delivery:detail:delete"})
    public WrappedHttpResponse<Long> deleteDeliveryDetail(@PathVariable Long id) {
        return WrappedHttpResponse.success(orderCmdApplication.deleteDeliveryDetail(id));
    }


    @ApiOperation(value = "确认交付单总单", tags = {"UPDATE"})
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/delivery/{documentId}/confirm")
    @ResponseBody
    @RequiresPermissions(value = {"delivery:summary:confirm"})
    public WrappedHttpResponse<String> confirmDeliverySummary(@PathVariable String documentId) {
        return WrappedHttpResponse.success(orderCmdApplication.confirmDeliverySummary(documentId));
    }


    @ApiOperation(value = "关闭交付单子单", tags = {"UPDATE"})
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/delivery/detail/{documentId}/{deliveryDetailId}/close")
    @ResponseBody
    @RequiresPermissions(value = {"delivery:detail:close"})
    public WrappedHttpResponse<Long> closeDeliveryDetail(
            @RequestBody @Validated DeliveryDetailCloseCmd deliveryDetailCloseCmd) {
        return WrappedHttpResponse.success(orderCmdApplication.closeDeliveryDetail(deliveryDetailCloseCmd));
    }

    @ApiOperation(value = "创建交付单对应的出库单", tags = {"UPDATE"})
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/delivery/{documentId}/stockout")
    @ResponseBody
    @RequiresPermissions(value = {"delivery:summary:stockout"})
    public WrappedHttpResponse<String> createStockOutSummary(
            @RequestBody @Validated StockOutSummaryCreateCmd createCmd) {
        orderCmdApplication.createStockOut(createCmd);
        return WrappedHttpResponse.success(null);
    }
}
