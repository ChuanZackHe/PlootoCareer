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
@ApiModel(description = "订单子单修改dto")
public class OrderDetailUpdateCmd implements IToEntity {

    @ApiModelProperty(value = "id", example = "1", required = true)
    @JsonProperty("id")
    @NotNull(message = OrderConstant.NOT_NULL_ORDER_DETAIL_CODE)
    private Long id;

    @ApiModelProperty(value = "物料id", example = "1")
    @JsonProperty("material_id")
    private Long materialId;

    @ApiModelProperty(value = "价格", example = "10")
    @JsonProperty("price")
    private Double price;

    @ApiModelProperty(value = "物料数量", example = "1.1")
    @JsonProperty("material_count")
    private Double materialCount;

    @ApiModelProperty(value = "数量", example = "1")
    private Double count;

    @ApiModelProperty(value = "物料名称", example = "阀门")
    @Length(max = 64)
    @JsonProperty("material_name")
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
