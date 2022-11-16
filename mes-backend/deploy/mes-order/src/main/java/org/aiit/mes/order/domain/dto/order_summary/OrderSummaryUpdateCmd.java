package org.aiit.mes.order.domain.dto.order_summary;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aiit.mes.common.iface.IToEntity;
import org.aiit.mes.common.util.PropertyCopyUtil;
import org.aiit.mes.order.constant.OrderConstant;
import org.aiit.mes.order.domain.dao.entity.OrderSummaryEntity;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Date;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "订单总单修改dto")
public class OrderSummaryUpdateCmd implements IToEntity {

    @Length(max = 255)
    @ApiModelProperty(value = "订单总单编码", example = "104A", required = true)
    @JsonProperty("document_id")
    @NotNull(message = OrderConstant.NOT_NULL_ORDER_SUMMARY_CODE)
    private String documentId;

    @ApiModelProperty(value = "客户id", example = "1")
    @JsonProperty("customer_id")
    private Long customerId;

    @Length(max = 64)
    @ApiModelProperty(value = "客户名称", example = "侠客岛")
    @JsonProperty("customer_name")
    private String customerName;

    @ApiModelProperty(value = "价格", example = "10")
    @JsonProperty("price")
    private Double price;

    @ApiModelProperty(value = "截止日期")
    @JsonProperty("closing_date")
    private Date closingDate;

    @ApiModelProperty(value = "额外信息", example = "意外破损")
    private String description;

    @Override
    public OrderSummaryEntity toEntity() {
        return PropertyCopyUtil.copyToClass(this, OrderSummaryEntity.class);
    }
}
