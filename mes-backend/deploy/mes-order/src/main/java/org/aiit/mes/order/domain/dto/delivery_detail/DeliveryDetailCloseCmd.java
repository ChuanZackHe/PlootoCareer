package org.aiit.mes.order.domain.dto.delivery_detail;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.aiit.mes.order.constant.OrderConstant;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
@Builder
@ApiModel(description = "交付子单关闭dto")
public class DeliveryDetailCloseCmd {

    @ApiModelProperty(value = "id", example = "1", required = true)
    @JsonProperty("id")
    @NotNull(message = OrderConstant.NOT_NULL_ORDER_DETAIL_CODE)
    private Long id;

    @ApiModelProperty(value = "额外信息", example = "意外破损")
    @Length(max = 255)
    @JsonProperty("description")
    private String description;
}
