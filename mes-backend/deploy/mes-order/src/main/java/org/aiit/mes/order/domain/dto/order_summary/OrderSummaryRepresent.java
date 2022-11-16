package org.aiit.mes.order.domain.dto.order_summary;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderSummaryRepresent {

    @Length(max = 255)
    @ApiModelProperty(value = "订单总单编码", example = "104A")
    @JsonProperty("document_id")
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

    @ApiModelProperty(value = "状态", example = "运行中")
    @Length(max = 64)
    @JsonProperty("status")
    private String status;

    @ApiModelProperty(value = "额外信息", example = "意外破损")
    @Length(max = 255)
    @JsonProperty("description")
    private String description;

    private Date createTime;

    private Date updateTime;

    private String createUsername;

}
