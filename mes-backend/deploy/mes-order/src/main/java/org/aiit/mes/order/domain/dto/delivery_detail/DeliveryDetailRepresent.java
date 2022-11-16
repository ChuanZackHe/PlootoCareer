package org.aiit.mes.order.domain.dto.delivery_detail;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aiit.mes.common.constant.BaseMaterialType;
import org.aiit.mes.order.constant.DeliveryDetailStatusEnum;
import org.hibernate.validator.constraints.Length;

import java.sql.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "交付子单展示dto")
public class DeliveryDetailRepresent {

    @ApiModelProperty(value = "id", example = "1")
    @JsonProperty("id")
    private Long id;

    @ApiModelProperty(value = "订单子单id", example = "1")
    @JsonProperty("order_detail_id")
    private Long orderDetailId;

    @ApiModelProperty(value = "交付单编码", example = "104A")
    @Length(max = 255)
    @JsonProperty("document_id")
    private String documentId;

    @ApiModelProperty(value = "物料id", example = "1")
    @JsonProperty("material_id")
    private Long materialId;

    @ApiModelProperty(value = "物料数量", example = "1.1")
    private Double count;

    @ApiModelProperty(value = "在库数量", example = "1.1")
    @JsonProperty("stock_count")
    private Double stockCount;

    @ApiModelProperty(value = "状态", example = "运行中")
    private DeliveryDetailStatusEnum status;

    @ApiModelProperty(value = "物料名称", example = "阀门")
    @Length(max = 64)
    @JsonProperty("material_name")
    private String materialName;

    @ApiModelProperty(value = "物料类型")
    @JsonProperty("material_type")
    private String materialType;

    @ApiModelProperty(value = "已出库数量")
    @JsonProperty("stock_out_count")
    private Double stockOutCount;

    @ApiModelProperty(value = "库存是否满足")
    private Boolean satisfied;

    @ApiModelProperty(value = "额外信息")
    private String userDefineInfo;
}
