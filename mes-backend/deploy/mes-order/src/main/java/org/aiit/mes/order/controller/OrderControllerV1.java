package org.aiit.mes.order.controller;

import org.aiit.mes.common.dto.WrappedHttpResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.aiit.mes.common.module.FunctionTypeAnnotation;
import org.aiit.mes.common.module.FunctionTypeEnum;
import org.aiit.mes.order.application.IOrderCmdApplication;
import org.aiit.mes.order.application.IOrderQueryApplication;
import org.aiit.mes.order.domain.dto.order_detail.*;
import org.aiit.mes.order.domain.dto.order_summary.OrderSummaryCreateCmd;
import org.aiit.mes.order.domain.dto.order_summary.OrderSummaryQuery;
import org.aiit.mes.order.domain.dto.order_summary.OrderSummaryRepresent;
import org.aiit.mes.order.domain.dto.order_summary.OrderSummaryUpdateCmd;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author zhangshaoqing
 * 订单管理控制器
 */
@Api(tags = "V1版本-订单管理")
@RestController
@RequestMapping("/v1/order")
@Validated
@FunctionTypeAnnotation(function = FunctionTypeEnum.ORDER_MAINTAIN)
public class OrderControllerV1 {

    @Resource
    private IOrderCmdApplication orderCmdApplication;

    @Resource
    private IOrderQueryApplication orderQueryApplication;

    @ApiOperation("批量查询订单总单信息")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/summary/list")
    @ResponseBody
//    @RequiresPermissions("order:summary:list")
    public WrappedHttpResponse<List<OrderSummaryRepresent>> listOrderInfo(
            @RequestBody @Validated OrderSummaryQuery queryCondition) {
        return WrappedHttpResponse.successPageRepresent(orderQueryApplication.listOrderInfo(queryCondition));
    }


    @ApiOperation("查询订单总单详细信息")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/summary/{documentId}/show")
    @ResponseBody
//    @RequiresPermissions("order:summary:show")
    public WrappedHttpResponse<OrderSummaryRepresent> showOrderInfo(
            @PathVariable String documentId) {
        return WrappedHttpResponse.success(orderQueryApplication.showOrder(documentId).toRepresent());
    }

    @ApiOperation(value = "新增订单总单", tags = {"UPDATE"})
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/summary/add")
    @ResponseBody
    @RequiresPermissions(value = {"order:summary:add"})
    public WrappedHttpResponse<OrderSummaryRepresent> addOrderSummary(
            @RequestBody @Validated OrderSummaryCreateCmd orderSummaryCreateCmd) {
        return WrappedHttpResponse.success(orderCmdApplication.addOrderSummary(orderSummaryCreateCmd));
    }

    @ApiOperation(value = "修改订单总单", tags = {"UPDATE"})
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/summary/update")
    @ResponseBody
    @RequiresPermissions(value = {"order:summary:update"})
    public WrappedHttpResponse<OrderSummaryRepresent> updateOrderSummary(
            @RequestBody @Validated OrderSummaryUpdateCmd orderSummaryUpdateCmd) {
        return WrappedHttpResponse.success(orderCmdApplication.updateOrderSummary(orderSummaryUpdateCmd));
    }

    @ApiOperation(value = "删除订单总单", tags = {"UPDATE"})
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/summary/{documentId}/delete")
    @ResponseBody
    @RequiresPermissions(value = {"order:summary:delete"})
    public WrappedHttpResponse<String> deleteOrderSummary(@PathVariable String documentId) {
        return WrappedHttpResponse.success(orderCmdApplication.deleteOrderSummary(documentId));
    }


    @ApiOperation("批量查询订单子单信息")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/detail/list")
    @ResponseBody
//    @RequiresPermissions("order:detail:list")
    public WrappedHttpResponse<List<OrderDetailRepresent>> listOrderDetailInfo(
            @RequestBody @Validated OrderDetailQuery queryCondition) {
        return WrappedHttpResponse.successPage(orderQueryApplication.listOrderDetailInfo(queryCondition));
    }

    @ApiOperation(value = "新增订单子单", tags = {"UPDATE"})
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/detail/add")
    @ResponseBody
    @RequiresPermissions(value = {"order:detail:add"})
    public WrappedHttpResponse<OrderDetailRepresent> addOrderDetail(
            @RequestBody @Validated OrderDetailCreateCmd orderDetailCreateCmd) {
        return WrappedHttpResponse.success(orderCmdApplication.addOrderDetail(orderDetailCreateCmd));
    }

    @ApiOperation(value = "修改订单子单", tags = {"UPDATE"})
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/detail/update")
    @ResponseBody
    @RequiresPermissions(value = {"order:detail:update"})
    public WrappedHttpResponse<OrderDetailRepresent> updateOrderDetail(
            @RequestBody @Validated OrderDetailUpdateCmd orderDetailUpdateCmd) {
        return WrappedHttpResponse.success(orderCmdApplication.updateOrderDetail(orderDetailUpdateCmd));
    }


    @ApiOperation(value = "删除订单子单", tags = {"UPDATE"})
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/detail/{documentId}/{id}/delete")
    @ResponseBody
    @RequiresPermissions(value = {"order:detail:delete"})
    public WrappedHttpResponse<Long> deleteOrderDetail(@PathVariable Long id) {
        return WrappedHttpResponse.success(orderCmdApplication.deleteOrderDetail(id));
    }


    @ApiOperation(value = "确认订单总单", tags = {"UPDATE"})
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/{documentId}/confirm")
    @ResponseBody
    @RequiresPermissions(value = {"order:summary:confirm"})
    public WrappedHttpResponse<String> confirmOrderSummary(@PathVariable String documentId) {
        return WrappedHttpResponse.success(orderCmdApplication.confirmOrderSummary(documentId));
    }


    @ApiOperation(value = "关闭订单子单", tags = {"UPDATE"})
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/detail/{documentId}/{orderDetailId}/close")
    @ResponseBody
    @RequiresPermissions(value = {"order:detail:close"})
    public WrappedHttpResponse<Long> closeOrderDetail(@RequestBody @Validated OrderDetailCloseCmd orderDetailCloseCmd) {
        return WrappedHttpResponse.success(orderCmdApplication.closeOrderDetail(orderDetailCloseCmd));
    }


    @ApiOperation("获取订单子单对应可以规划的数量")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/delivery/detail/{documentId}/capacity")
    @ResponseBody
//    @RequiresPermissions("delivery:detail:capacity")
    public WrappedHttpResponse<Map<Long, Double>> getCapacity(
            @RequestBody @Validated CapacityQuery capacityQuery) {
        return WrappedHttpResponse.success(orderCmdApplication.getCapacity(capacityQuery));
    }


}
