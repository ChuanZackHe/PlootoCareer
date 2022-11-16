package org.aiit.mes.order.domain.dto.order_detail;

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
import org.aiit.mes.order.domain.dao.entity.OrderDetailEntity;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "订单子单创建dto")
public class OrderDetailCreateCmd implements IToEntity {

    @ApiModelProperty(value = "订单总单编码", example = "104A")
    @Length(max = 255)
    @JsonProperty("document_id")
    private String documentId;

    @ApiModelProperty(value = "物料id", example = "1", required = true)
    @NotNull(message = OrderConstant.NOT_NULL_MATERIAL_ID)
    @JsonProperty("material_id")
    private Long materialId;

    @ApiModelProperty(value = "价格", example = "10", required = true)
    @JsonProperty("price")
    private Double price;

    @ApiModelProperty(value = "物料数量", example = "1.1", required = true)
    @NotNull(message = OrderConstant.NOT_NULL_MATERIAL_COUNT)
    private Double count;

    @ApiModelProperty(value = "物料名称", example = "阀门", required = true)
    @Length(max = 64)
    @JsonProperty("material_name")
    @NotNull(message = OrderConstant.NOT_NULL_MATERIAL_NAME)
    private String materialName;

    @ApiModelProperty(value = "描述", example = "阀门")
    private String description;

    @ApiModelProperty(value = "用户自定义信息")
    private String userDefineInfo;

    @Override
    public OrderDetailEntity toEntity() {
        return PropertyCopyUtil.copyToClass(this, OrderDetailEntity.class);
    }
}
