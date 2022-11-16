package org.aiit.mes.factory.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aiit.mes.common.constant.DataStateEnum;

import javax.validation.constraints.NotBlank;

/**
 * @author lzj
 * @version 1.0.0
 * @ClassName ListResourceFilterDto.java
 * @Description 主界面查询资源过滤条件dto，采用@TableField完成对小写下划线和驼峰命名的转换
 * @createTime 2021年08月24日 10:03:00
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListResourceFilterDto {

    @ApiModelProperty("资源代码")
    private String code;

    @ApiModelProperty("类型")
    private Integer type;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("父资源代码")
    @TableField(value = "parent_code")
    @JsonProperty(value = "parent_code")
    private String parentCode;

    @ApiModelProperty("顶级资源代码")
    @TableField(value = "ancestor_code")
    @JsonProperty(value = "ancestor_code")
    private String ancestorCode;

    @ApiModelProperty("状态")
    private DataStateEnum state;

    @ApiModelProperty("当前页码")
    @JsonProperty("page_num")
    @NotBlank
    private Integer pageNum;

    @ApiModelProperty("每页大小")
    @JsonProperty("page_size")
    @NotBlank
    private Integer pageSize;
    
}
