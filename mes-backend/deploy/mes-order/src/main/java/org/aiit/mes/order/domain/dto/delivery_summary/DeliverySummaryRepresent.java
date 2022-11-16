package org.aiit.mes.order.domain.dto.delivery_summary;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aiit.mes.common.constant.CommonSummaryStatusEnum;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "交付总单展示dto")
public class DeliverySummaryRepresent {

    @ApiModelProperty(value = "交付总单编码", example = "104A")
    @Length(max = 255)
    @JsonProperty("document_id")
    private String documentId;

    @ApiModelProperty(value = "状态", example = "运行中")
    @JsonProperty("status")
    private CommonSummaryStatusEnum status;

    @ApiModelProperty(value = "交付日期", example = "1970-10-10")
    @JsonProperty("delivery_date")
    private Date deliveryDate;

    @ApiModelProperty(value = "订单总单编码", example = "104A")
    @Length(max = 255)
    @JsonProperty("order_summary_code")
    private String orderSummaryCode;

    @ApiModelProperty(value = "额外信息", example = "意外破损")
    private String description;

    private Date createTime;

    private Date updateTime;

    private String createUsername;
}
