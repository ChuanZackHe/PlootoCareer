package org.aiit.mes.order.domain.dto.delivery_summary;

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
import org.aiit.mes.order.domain.dao.entity.DeliverySummaryEntity;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.sql.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "交付总单修改dto")
public class DeliverySummaryUpdateCmd implements IToEntity {

    @ApiModelProperty(value = "交付总单编码", example = "104A", required = true)
    @Length(max = 255)
    @JsonProperty("document_id")
    @NotNull(message = OrderConstant.NOT_NULL_DELIVERY_SUMMARY_CODE)
    private String documentId;

    @ApiModelProperty(value = "交付日期", example = "1970-10-10")
    @JsonProperty("delivery_date")
    private Date deliveryDate;

    @ApiModelProperty(value = "订单总单编码", example = "104A")
    @Length(max = 255)
    @JsonProperty("order_summary_code")
    private String orderSummaryCode;

    @ApiModelProperty(value = "额外信息", example = "意外破损")
    @Length(max = 255)
    private String description;

    @Override
    public DeliverySummaryEntity toEntity() {
        return PropertyCopyUtil.copyToClass(this, DeliverySummaryEntity.class);
    }
}
