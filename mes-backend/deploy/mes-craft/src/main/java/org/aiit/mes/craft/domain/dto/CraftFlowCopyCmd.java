package org.aiit.mes.craft.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName CraftFlowRepresent
 * @Description
 * @createTime 2021.09.07 16:54
 */
@Data
@ApiModel(value = "复制工艺流程请求体")
public class CraftFlowCopyCmd {

    @NotNull
    @Min(1)
    @ApiModelProperty(value = "复制的原流程id", required = true)
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "新的流程名称", required = true)
    private String name;
}
