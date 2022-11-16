package org.aiit.mes.order.domain.dto.order_summary;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aiit.mes.order.domain.dto.delivery_summary.DeliverySummaryRepresent;
import org.hibernate.validator.constraints.Length;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderOverviewRepresent {

    @Length(max = 255)
    @ApiModelProperty(value = "订单总单编码", example = "104A")
    @JsonProperty("document_id")
    private String documentId;

    @Length(max = 64)
    @ApiModelProperty(value = "客户名称", example = "侠客岛")
    @JsonProperty("customer_name")
    private String customerName;

    @ApiModelProperty(value = "状态", example = "运行中")
    @Length(max = 64)
    @JsonProperty("status")
    private String status;

    @ApiModelProperty(value = "创建日期")
    private Date createTime;

    @ApiModelProperty(value = "更新日期")
    private Date updateTime;

    @ApiModelProperty(value = "截止日期")
    private Date closingDate;

    @ApiModelProperty(value = "交付单", example = "运行中")
    @JsonProperty("delivery_list")
    List<DeliverySummaryRepresent> deliverySummaryRepresentList;

}
