package org.aiit.mes.factory.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName ResourceRepresent
 * @Description 资源展示对象，用于返回给调用者
 * @createTime 2021.08.10 14:27
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResourceRepresentDto {

    @ApiModelProperty("资源id")
    private Long id;

    @ApiModelProperty("资源代码")
    private String code;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("相对坐标x")
    @JsonProperty("x_rel")
    private Integer xrel;

    @ApiModelProperty("相对坐标y")
    @JsonProperty("y_rel")
    private Integer yrel;

    @ApiModelProperty("资源状态")
    private String state;
}
