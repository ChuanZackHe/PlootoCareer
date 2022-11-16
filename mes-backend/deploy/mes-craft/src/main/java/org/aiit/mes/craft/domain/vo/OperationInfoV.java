package org.aiit.mes.craft.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aiit.mes.common.constant.CraftOperationTypeEnum;
import org.aiit.mes.common.exception.ParamInvalidException;
import org.aiit.mes.common.iface.IValidate;
import org.aiit.mes.common.util.DoubleUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.Optional;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName OpInfoV
 * @Description
 * @createTime 2021.09.01 09:52
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "加工信息")
public class OperationInfoV implements IValidate {
    /**加工相关字段*/
    /**
     * 空-普通加工模板
     */
    public static final OperationInfoV TEMPLATE_OP_COMMON = OperationInfoV.builder().opTime(-1)
                                                                          .opResourceCode(null)
                                                                          .opInstruction(null)
                                                                          .opNotice(null)
                                                                          .opToolGroupId(-1L)
                                                                          .outputMaterialId(null)
                                                                          .outputMaterialNum(null)
                                                                          .outputMaterialName(null)
                                                                          .outputMaterialUnit(null)
                                                                          .build();

    /**
     * 空-组装加工模板
     */
    public static final OperationInfoV TEMPLATE_OP_ASSEMBLE = OperationInfoV.builder().opTime(-1)
                                                                            .opResourceCode(null)
                                                                            .opInstruction(null)
                                                                            .opNotice(null)
                                                                            .opToolGroupId(-1L)
                                                                            .outputMaterialId(-1L)
                                                                            .outputMaterialNum(-1d)
                                                                            .outputMaterialName("组装后物料")
                                                                            .outputMaterialUnit("组装后物料单位")
                                                                            .build();

    /**
     * op_time	int		加工耗时
     * op_resource_code	varchar(32)		绑定工厂资源code：对应工厂概况-资源表
     * op_instruction	text		加工步骤说明
     * op_notice	text		加工特别提醒
     */
    @ApiModelProperty(value = "加工耗时")
    @JsonProperty("op_time")
    private Integer opTime;

    @ApiModelProperty(value = "绑定工厂资源组编码")
    @JsonProperty("op_resource_code")
    private String opResourceCode;

    @ApiModelProperty(value = "加工步骤说明")
    @JsonProperty("op_instruction")
    private String opInstruction;

    @ApiModelProperty(value = "加工特别注意事项")
    @JsonProperty("op_notice")
    private String opNotice;

    @ApiModelProperty(value = "加工使用的工具组id")
    @JsonProperty("op_tool_group_id")
    private Long opToolGroupId;

    @ApiModelProperty(value = "加工类型", required = true, example = "COMMON:普通加工，ASSEMBLE:组装加工")
    @JsonProperty("op_type")
    private CraftOperationTypeEnum opType;

    @ApiModelProperty(value = "特殊：组装加工时必填 步骤使用，组装后的输出物料id")
    @JsonProperty("output_material_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long outputMaterialId;

    @ApiModelProperty(value = "特殊：组装加工时必填 步骤使用，组装后的输出物料单位")
    @JsonProperty("output_material_unit")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String outputMaterialUnit;

    @ApiModelProperty(value = "特殊：组装加工时必填 步骤使用，组装后的输出物料名称")
    @JsonProperty("output_material_name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String outputMaterialName;

    public void setOutputMaterialNum(Double outputMaterialNum) {
        this.outputMaterialNum = DoubleUtil.round(outputMaterialNum);
    }

    @ApiModelProperty(value = "特殊：组装加工时必填 步骤使用，组装后的输出物料数量")
    @JsonProperty("output_material_num")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double outputMaterialNum;

    @Override
    public boolean validate() {
        try {
            // todo： 校验qcToolGroupId
            Assert.isTrue(StringUtils.isNotBlank(opResourceCode), "绑定工厂资源编码op_resource_code不为空");
            Assert.isTrue(StringUtils.isNotBlank(opInstruction), "加工步骤说明op_instruction不为空");
            // todo： 校验output

            // 填充默认值, type为null时默认类型为COMMON
            Optional.ofNullable(opType).orElseGet(() -> this.opType = CraftOperationTypeEnum.COMMON);

            // 校验 组装加工
            if (!CraftOperationTypeEnum.ASSEMBLE.equals(opType)) {
                return true;
            }
            Assert.isTrue(Objects.nonNull(outputMaterialId) && outputMaterialId > 0, "组装加工类型要求填写输出物料id");
            Assert.isTrue(StringUtils.isNotBlank(outputMaterialName), "组装加工类型要求填写输出物料名称");
            Assert.isTrue(StringUtils.isNotBlank(outputMaterialUnit), "组装加工类型要求填写输出物料单位");
            Assert.isTrue(Objects.nonNull(outputMaterialNum) && outputMaterialNum > 0, "组装加工类型要求填写输出物料数量");
        }
        catch (Exception e) {
            throw new ParamInvalidException(e.getMessage());
        }
        return true;
    }

    public void multiply(Double x) {
        if (Objects.isNull(this.outputMaterialNum)) {
            return;
        }
        this.outputMaterialNum = DoubleUtil.multiple(this.outputMaterialNum, x);
    }
}
