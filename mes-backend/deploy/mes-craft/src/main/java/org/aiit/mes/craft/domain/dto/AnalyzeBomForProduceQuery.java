package org.aiit.mes.craft.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName CraftTreeQuery
 * @Description 用于查询物料树
 * @createTime 2021.12.23 16:20
 */
@Data
@Builder
@ApiModel(value = "生产用需求分析请求体")
public class AnalyzeBomForProduceQuery {

    @NotNull
    @Min(1)
    @ApiModelProperty(value = "工艺流程id", required = true)
    @JsonProperty(value = "flow_id")
    private Long flowId;

    @ApiModelProperty(value = "需要物料数量, 默认为空:返回工艺流程中配置的数量(原始物料树）", required = true)
    @JsonProperty(value = "required_num")
    @Min(0)
    private Double requiredNum;

    @NotNull
    @Min(1)
    @ApiModelProperty(value = "目标物料id", required = true)
    @JsonProperty(value = "material_id")
    private Long materialId;

    @ApiModelProperty(value = "是否对比真实库存含量, 默认为true=需要对比", required = false, example = "true")
    @JsonProperty(value = "compare_stock")
    private Boolean compareStock;

    public void updateDefaultValue() {
        this.compareStock = Objects.isNull(compareStock) ? true : compareStock;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AnalyzeBomForProduceQuery{");
        sb.append("flowId=").append(flowId);
        sb.append(", requiredNum=").append(requiredNum);
        sb.append(", materialId=").append(materialId);
        sb.append(", compareStock=").append(compareStock);
        sb.append('}');
        return sb.toString();
    }
}
