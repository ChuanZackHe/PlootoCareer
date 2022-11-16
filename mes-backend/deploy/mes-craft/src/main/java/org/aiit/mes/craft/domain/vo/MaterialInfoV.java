package org.aiit.mes.craft.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aiit.mes.common.constant.MaterialTransferEnum;
import org.aiit.mes.common.exception.ParamInvalidException;
import org.aiit.mes.common.iface.IValidate;
import org.aiit.mes.common.util.DoubleUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

import static org.aiit.mes.common.constant.Constants.TRANSFER_TYPES;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName MaterialInfoV
 * @Description
 * @createTime 2021.09.01 09:45
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "物料信息")
public class MaterialInfoV implements IValidate {

    /**
     * 出库模板
     */
    public static final MaterialInfoV TEMPLATE_STOCK_OUT = new MaterialInfoV(-1L, -1.0, MaterialTransferEnum.STOCK_OUT,
                                                                             null, null, null);

    /**
     * 入库模板
     */
    public static final MaterialInfoV TEMPLATE_STOCK_IN = new MaterialInfoV(-1L, -1.0, MaterialTransferEnum.STOCK_IN,
                                                                            null, null, null);

    /**
     * 在线流转输出模板
     */
    public static final MaterialInfoV TEMPLATE_ON_THE_SCENE = new MaterialInfoV(-1L, -1.0,
                                                                                MaterialTransferEnum.ON_THE_SCENE,
                                                                                null,
                                                                                null, null);

    /**
     * 在线流转输入模板
     */
    public static final MaterialInfoV TEMPLATE_ON_THE_SCENE_INPUT = new MaterialInfoV(-1L, -1.0,
                                                                                      MaterialTransferEnum.ON_THE_SCENE_IN,
                                                                                      null,
                                                                                      null, null);

    @ApiModelProperty(value = "物料id", required = true)
    @Min(1)
    @NotNull
    private Long id;

    public void setNum(Double num) {
        this.num = DoubleUtil.round(num);
    }

    @ApiModelProperty(value = "物料数量", required = true, example = "10.0")
    @Min(0)
    @NotNull
    private Double num;

    @JsonProperty("transfer_type")
    @ApiModelProperty(value = "物料流转类型", required = true, allowableValues = TRANSFER_TYPES,
            example = "MATERIAL_TRANSFER")
    @NotNull
    private MaterialTransferEnum transferType;

    @ApiModelProperty(value = "物料名称")
    private String name;

    @ApiModelProperty(value = "物料单位")
    private String unit;

    @ApiModelProperty(value = "物料编码")
    private String code;

    @Override
    public boolean validate() {
        try {
            Assert.isTrue(id >= 1, "id必须大于等于1");
            Assert.isTrue(num > 0, "num必须大于0");
            Assert.notNull(transferType, "transfer_type不为空");
            Assert.isTrue(StringUtils.isNotBlank(name), "物料name不为空");
            Assert.isTrue(StringUtils.isNotBlank(unit), "物料unit不为空");
            Assert.isTrue(StringUtils.isNotBlank(code), "物料code不为空");
        }
        catch (Exception e) {
            throw new ParamInvalidException(e.getMessage());
        }
        return true;
    }

    public void multiply(Double x) {
        if (Objects.isNull(this.num)) {
            return;
        }
        this.num = DoubleUtil.multiple(this.num, x);
    }
}
