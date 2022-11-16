package org.aiit.mes.order.controller;

import org.aiit.mes.common.dto.WrappedHttpResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.aiit.mes.common.module.FunctionTypeAnnotation;
import org.aiit.mes.common.module.FunctionTypeEnum;
import org.aiit.mes.order.application.IOrderQueryApplication;
import org.aiit.mes.order.domain.dto.order_summary.OrderOverviewQuery;
import org.aiit.mes.order.domain.dto.order_summary.OrderOverviewRepresent;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ：张少卿
 * @description：TODO
 * @date ：2022/2/15 10:31 上午
 */
@Api(tags = "V1版本-订单管理")
@RestController
@RequestMapping("/v1/order")
@Validated
@FunctionTypeAnnotation(function = FunctionTypeEnum.ORDER_OVERVIEW)
public class OrderOverviewController {

    @Resource
    private IOrderQueryApplication orderQueryApplication;

    @ApiOperation(value = "查询订单的总览", tags = {"GET"})
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "用户授权token", dataType = "String", required =
            true, defaultValue = "{{token}}", example = "{{token}}")
    @PostMapping("/summary/overview")
    @ResponseBody
    @RequiresPermissions("order:summary:overview")
    public WrappedHttpResponse<List<OrderOverviewRepresent>> listOrderOverview(
            @RequestBody @Validated OrderOverviewQuery orderOverviewQuery) {
        return WrappedHttpResponse.successPage(orderQueryApplication.listOrderOverview(orderOverviewQuery));
    }
}
