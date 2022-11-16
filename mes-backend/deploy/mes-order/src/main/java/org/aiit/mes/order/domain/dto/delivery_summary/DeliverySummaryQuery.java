package org.aiit.mes.order.domain.dto.delivery_summary;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.aiit.mes.common.page.BasePage;
import org.aiit.mes.order.domain.dao.entity.DeliverySummaryEntity;
import org.aiit.mes.order.domain.dao.entity.OrderSummaryEntity;
import org.hibernate.validator.constraints.Length;

import java.sql.Date;
import java.util.Optional;

@Data
@ApiModel(description = "交付总单查询dto")
public class DeliverySummaryQuery extends BasePage<DeliverySummaryEntity> {

    @ApiModelProperty(value = "交付总单编码", example = "104A")
    @Length(max = 255)
    @JsonProperty("document_id")
    private String documentId;

    @ApiModelProperty(value = "状态", example = "运行中")
    @Length(max = 64)
    @JsonProperty("status")
    private String status;

    @ApiModelProperty(value = "交付日期", example = "1970-10-10")
    @JsonProperty("delivery_date")
    private Date deliveryDate;

    @ApiModelProperty(value = "订单总单编码", example = "104A")
    @Length(max = 255)
    @JsonProperty("order_summary_code")
    private String orderSummaryCode;

    @Override
    public LambdaQueryWrapper<DeliverySummaryEntity> toLambdaQueryWrapper() {
        LambdaQueryWrapper<DeliverySummaryEntity> wrapper = super.toLambdaQueryWrapper();
        Optional.ofNullable(this.orderSummaryCode).ifPresent(
                orderSummaryCode -> wrapper.eq(DeliverySummaryEntity::getOrderSummaryCode, orderSummaryCode));
        Optional.ofNullable(this.getStatus()).ifPresent(status -> wrapper.eq(DeliverySummaryEntity::getStatus, status));
        wrapper.orderByDesc(DeliverySummaryEntity::getId);
        return wrapper;
    }

}
