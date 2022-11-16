package org.aiit.mes.order.domain.dto.order_summary;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.aiit.mes.common.page.BasePage;
import org.aiit.mes.order.domain.dao.entity.OrderSummaryEntity;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Optional;

@Data
@ApiModel(description = "订单总单查询dto")
public class OrderSummaryQuery extends BasePage<OrderSummaryEntity> {

    @Length(max = 64)
    @ApiModelProperty(value = "客户名称", example = "侠客岛")
    @JsonProperty("customer_name")
    private String customerName;

    @ApiModelProperty(value = "状态", example = "运行中")
    @Length(max = 64)
    @JsonProperty("status")
    private String status;

    @ApiModelProperty(value = "开始时间")
    @NotNull
    @JsonProperty("start_date")
    private Long startDate;

    @ApiModelProperty(value = "结束时间")
    @NotNull
    @JsonProperty("end_date")
    private Long endDate;

    @Override
    public LambdaQueryWrapper<OrderSummaryEntity> toLambdaQueryWrapper() {
        LambdaQueryWrapper<OrderSummaryEntity> wrapper = super.toLambdaQueryWrapper();
        Optional.ofNullable(this.getStatus()).ifPresent(status -> wrapper.eq(OrderSummaryEntity::getStatus, status));
        Optional.ofNullable(this.getCustomerName()).ifPresent(
                clientName -> wrapper.like(OrderSummaryEntity::getCustomerName, clientName));
        wrapper.between(OrderSummaryEntity::getCreateTime, new Date(startDate), new Date(endDate));
        wrapper.orderByDesc(OrderSummaryEntity::getId);
        return wrapper;
    }
}
