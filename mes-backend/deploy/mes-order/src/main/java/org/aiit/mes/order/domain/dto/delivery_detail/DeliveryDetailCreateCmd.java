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
@ApiModel(description = "交付单子单新建dto")
public class DeliveryDetailCreateCmd implements IToEntity {

    @ApiModelProperty(value = "订单子单id", example = "1", required = true)
    @JsonProperty("order_detail_id")
    @NotNull(message = OrderConstant.NOT_NULL_ORDER_DETAIL_CODE)
    private Long orderDetailId;

    @ApiModelProperty(value = "交付总单编码", example = "104A", required = true)
    @Length(max = 255)
    @NotNull(message = OrderConstant.NOT_NULL_DELIVERY_SUMMARY_CODE)
    @JsonProperty("document_id")
    private String documentId;

    @ApiModelProperty(value = "物料id", required = true, example = "1")
    @JsonProperty("material_id")
    @NotNull(message = OrderConstant.NOT_NULL_MATERIAL_ID)
    private Long materialId;

    @ApiModelProperty(value = "物料数量", example = "1.1", required = true)
    @NotNull(message = OrderConstant.NOT_NULL_MATERIAL_COUNT)
    private Double count;

    @ApiModelProperty(value = "交付日期", example = "1970-10-10", required = true)
    @JsonProperty("delivery_date")
    @NotNull(message = OrderConstant.NOT_NULL_DELIVERY_DATE)
    private Date deliveryDate;

    @ApiModelProperty(value = "物料名称", example = "阀门", required = true)
    @Length(max = 64)
    @JsonProperty("material_name")
    @NotNull(message = OrderConstant.NOT_NULL_MATERIAL_NAME)
    private String materialName;

    @ApiModelProperty(value = "用户自定义信息")
    private String userDefineInfo;

    @Override
    public DeliveryDetailEntity toEntity() {
        return PropertyCopyUtil.copyToClass(this, DeliveryDetailEntity.class);
    }
}
