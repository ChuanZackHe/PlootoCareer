package org.aiit.mes.craft.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.aiit.mes.base.material.domain.entity.BaseMaterialEntity;
import org.aiit.mes.common.constant.BaseMaterialType;
import org.aiit.mes.common.util.DoubleUtil;
import org.aiit.mes.common.util.PropertyCopyUtil;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.Optional;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName CraftTreeNodeMaterialV
 * @Description
 * @createTime 2021.12.24 16:01
 */
@Data
@ApiModel(value = "需求分析树物料节点展示对象")
public class CraftBomTreeNodeRepresentV {

    /**
     * 物料id（内部数据库使用）
     * 设置mybatis-plus返回主键
     */
    @ApiModelProperty(value = "物料id")
    @JsonProperty("material_id")
    private Long id;

    /**
     * 物料编码（用户使用）
     */
    @ApiModelProperty(value = "物料编码")
    @JsonProperty("material_code")
    private String code;

    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称")
    @JsonProperty("material_name")
    private String name;

    /**
     * 基本单位
     */
    @ApiModelProperty(value = "物料单位")
    @JsonProperty("material_unit")
    private String unit;

    public void setType(BaseMaterialType type) {
        this.type = type;
        this.materialType = type.getDesc();
    }

    /**
     * 物料类型：RAW=原材料；WIP=半成品；PRD=成品
     */
    @ApiModelProperty(value = "物料类型（enum）")
    @JsonIgnore
    private BaseMaterialType type;

    @ApiModelProperty(value = "物料类型(描述）")
    @JsonProperty("material_type")
    private String materialType;


    /**
     * 是否支持外购
     */
    @ApiModelProperty(value = "物料是否支持外购")
    @JsonProperty(value = "can_buy")
    private Boolean canBuy;

    @ApiModelProperty(value = "需要数量")
    @JsonProperty(value = "count")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double requiredNum;

    @ApiModelProperty(value = "库存数量")
    @JsonProperty(value = "stock_count")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double stockNum;

    @ApiModelProperty(value = "库存数量是否满足需求")
    @JsonProperty(value = "is_satisfied")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isSatisfied;

    public static CraftBomTreeNodeRepresentV convertFrom(BaseMaterialEntity materialEntity,
                                                         Double stockNum,
                                                         Double requiredNum) {
        Assert.notNull(materialEntity, "material entity can't be null");
        CraftBomTreeNodeRepresentV nodeRepresent = PropertyCopyUtil.copyToClass(materialEntity,
                                                                                CraftBomTreeNodeRepresentV.class);
        Optional.ofNullable(requiredNum).ifPresent(req -> nodeRepresent.setRequiredNum(req));
        // 如果stock库存实例为空，说明为单纯查询物料bom清单。
        // 库存实例非空，则说明为需求分析bom树，需要展示库存数量以及是否满足需求数量。
        Optional.ofNullable(stockNum).ifPresent(count -> nodeRepresent.setStockNum(count));
        return nodeRepresent;
    }

    public void setRequiredNum(Double requiredNum) {
        this.requiredNum = requiredNum;
        if (Objects.isNull(this.requiredNum) || Objects.isNull(stockNum)) {
            return;
        }
        this.isSatisfied = DoubleUtil.ge(stockNum, requiredNum);
    }

    public void addRequiredNum(Double requiredNum) {
        if (Objects.isNull(requiredNum)) {
            return;
        }
        setRequiredNum(DoubleUtil.add(Optional.ofNullable(getRequiredNum()).orElse(0.0), requiredNum));
    }

    public void setStockNum(Double stockNum) {
        this.stockNum = stockNum;
        this.isSatisfied = DoubleUtil.ge(Optional.ofNullable(stockNum).orElse(0.0),
                                         Optional.ofNullable(requiredNum).orElse(1.0));
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CraftBomTreeNodeRepresentV{");
        sb.append("id=").append(id);
        sb.append(", code='").append(code).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", unit='").append(unit).append('\'');
        sb.append(", type=").append(type);
        sb.append(", canBuy=").append(canBuy);
        sb.append(", requiredNum=").append(requiredNum);
        sb.append(", stockNum=").append(stockNum);
        sb.append(", isSatisfied=").append(isSatisfied);
        sb.append('}');
        return sb.toString();
    }

    public String toStockWarningString() {
        final StringBuilder sb = new StringBuilder("[");
        sb.append("物料名称:").append(name);
        sb.append(", 要求数量:").append(requiredNum);
        sb.append(", 库存数量:").append(stockNum).append("]");
        return sb.toString();
    }
}
