package org.aiit.mes.order.domain.dto.delivery_detail;

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
import org.aiit.mes.order.domain.dao.entity.DeliveryDetailEntity;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.sql.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "交付子单修改dto")
public class DeliveryDetailUpdateCmd implements IToEntity {

    @ApiModelProperty(value = "id", example = "1", required = true)
    @JsonProperty("id")
    @NotNull(message = OrderConstant.NOT_NULL_DELIVERY_DETAIL_CODE)
    private Long id;

    @ApiModelProperty(value = "订单子单id", example = "1")
    @JsonProperty("order_detail_id")
    private Long orderDetailId;

    @ApiModelProperty(value = "物料id", example = "1")
    @JsonProperty("material_id")
    private Long materialId;

    @ApiModelProperty(value = "物料数量", example = "1.1")
    private Double count;

    @ApiModelProperty(value = "物料名称", example = "阀门")
    @Length(max = 64)
    @JsonProperty("material_name")
    private String materialName;

    @Override
    public DeliveryDetailEntity toEntity() {
        return PropertyCopyUtil.copyToClass(this, DeliveryDetailEntity.class);
    }
}
