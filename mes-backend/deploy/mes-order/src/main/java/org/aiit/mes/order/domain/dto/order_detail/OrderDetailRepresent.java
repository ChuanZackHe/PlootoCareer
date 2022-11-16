package org.aiit.mes.order.domain.dto.order_detail;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aiit.mes.order.constant.OrderDetailStatusEnum;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "订单子单展示dto")
public class OrderDetailRepresent {

    @ApiModelProperty(value = "id", example = "1")
    @JsonProperty("id")
    private Long id;

    @ApiModelProperty(value = "订单总单编码", example = "104A")
    @Length(max = 255)
    @JsonProperty("document_id")
    private String documentId;

    @ApiModelProperty(value = "物料id", example = "1")
    @JsonProperty("material_id")
    private Long materialId;

    @ApiModelProperty(value = "物料名称", example = "阀门")
    @Length(max = 64)
    @JsonProperty("material_name")
    private String materialName;

    @ApiModelProperty(value = "价格", example = "10")
    @JsonProperty("price")
    private Double price;

    @ApiModelProperty(value = "物料数量", example = "1.1")
    private Double count;

    /**
     * 这个是已经确认的
     */
    @ApiModelProperty(value = "已规划数量", example = "1")
    @JsonProperty("allocated_count")
    private Double allocatedCount;

    @ApiModelProperty(value = "状态", example = "运行中")
    private OrderDetailStatusEnum status;

    @ApiModelProperty(value = "描述", example = "阀门")
    private String description;

    /**
     * 剩余规划容量
     */
    @ApiModelProperty(value = "可以进行规划的数量")
    private Double capacity;

    @ApiModelProperty(value = "用户自定义信息")
    private String userDefineInfo;

}
