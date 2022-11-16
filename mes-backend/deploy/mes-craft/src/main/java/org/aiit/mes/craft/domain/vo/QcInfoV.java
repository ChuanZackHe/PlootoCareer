package org.aiit.mes.craft.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aiit.mes.common.constant.CraftQcCompareEnum;
import org.aiit.mes.common.exception.ParamInvalidException;
import org.aiit.mes.common.iface.IValidate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.Objects;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName QcInfoV
 * @Description
 * @createTime 2021.09.01 09:50
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "质检信息")
public class QcInfoV implements IValidate {

    /**
     * 空质检模板
     */
    public static final QcInfoV TEMPLATE_QC = new QcInfoV(-1, null, null, -1f, null, -1, -1);

    /**
     * 质检相关字段
     * qc_percent	int		质检百分比（抽查百分之多少）
     * qc_item	varchar(256)		质检项（重量、大小、厚度）
     * qc_compare	varchar(32)		质检判断关系：大于、小于、等于、不等于
     * qc_threshold	float		质检合格阈值
     * qc_description	text		质检步骤描述
     */
    @ApiModelProperty(value = "质检百分比")
    @JsonProperty("qc_percent")
    private Integer qcPercent;

    @ApiModelProperty(value = "质检条目")
    @JsonProperty("qc_item")
    private String qcItem;

    @ApiModelProperty(value = "质检比较类型")
    @JsonProperty("qc_compare")
    private CraftQcCompareEnum qcCompare;

    @ApiModelProperty(value = "质检比较阈值")
    @JsonProperty("qc_threshold")
    private Float qcThreshold;

    @ApiModelProperty(value = "质检步骤说明")
    @JsonProperty("qc_instruction")
    private String qcInstruction;

    @ApiModelProperty(value = "质检使用的工具组id")
    @JsonProperty("qc_tool_group_id")
    private Integer qcToolGroupId;

    @ApiModelProperty(value = "预留：质检库-质检条目id")
    @JsonProperty("qc_case_id")
    private Integer qcCaseId;

    @Override
    public boolean validate() {
        try {
            if (Objects.nonNull(qcCaseId)) {
                // 质检库-质检条目id存在，优先使用质检库中的条目
                Assert.isTrue(qcCaseId > 0, "质检库条目id必须大于等于0");
                Assert.isTrue(qcPercent >= 0, "质检百分比qc_percent必须大于等于0");

                return true;
            }
            // 质检库-质检条目id不存在，使用手动填写的质检信息。
            // todo： 校验qcToolGroupId
            Assert.isTrue(qcPercent >= 0, "质检百分比qc_percent必须大于等于0");
            Assert.isTrue(StringUtils.isNotBlank(qcItem), "质检项qc_item不为空");
            Assert.isTrue(Objects.nonNull(qcCompare), "质检比较类型qc_compare不为空");
            Assert.isTrue(Objects.nonNull(qcInstruction), "质检步骤说明qc_instruction不为空");
        }
        catch (Exception e) {
            throw new ParamInvalidException(e.getMessage());
        }
        return true;
    }
}
