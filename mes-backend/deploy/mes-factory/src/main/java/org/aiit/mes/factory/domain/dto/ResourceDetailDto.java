package org.aiit.mes.factory.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aiit.mes.common.constant.DataStateEnum;
import org.aiit.mes.common.iface.IToEntity;
import org.aiit.mes.common.util.PropertyCopyUtil;
import org.aiit.mes.factory.domain.dao.entity.FactoryResourceInfo;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author lzj
 * @version 1.0.0
 * @ClassName ResourceDetailDto.java
 * @Description 用来展示资源细节
 * @createTime 2021年08月24日 14:13:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResourceDetailDto implements IToEntity {

    private Long id;

    @NotBlank(message = "资源代码不可为空")
    @Length(min = 2, max = 20)
    @ApiModelProperty("资源代码")
    private String code;

    @NotBlank(message = "类型不可为空")
    @ApiModelProperty("类型")
    private Integer type;

    @NotBlank(message = "名称不可为空")
    @ApiModelProperty("名称")
    @Length(min = 2, max = 20)
    private String name;

    @NotBlank(message = "父资源代码不可为空")
    @Length(min = 2, max = 20)
    @ApiModelProperty("父资源代码")
    @JsonProperty("parent_code")
    private String parentCode;

    @NotBlank(message = "顶级资源代码不可为空")
    @Length(min = 2, max = 20)
    @ApiModelProperty("顶级资源代码")
    @JsonProperty("ancestor_code")
    private String ancestorCode;

    @NotBlank(message = "状态不可为空")
    @ApiModelProperty("状态：启用，冻结，空闲，忙碌")
    private DataStateEnum state;

    @NotBlank(message = "租户id不可为空")
    @ApiModelProperty("租户id")
    @JsonProperty("tenant_id")
    private String tenantId;

    @ApiModelProperty("x坐标")
    @JsonProperty("x_rel")
    private Integer xRel;

    @ApiModelProperty("y坐标")
    @JsonProperty("y_rel")
    private Integer yRel;

    @Override
    public FactoryResourceInfo toEntity() {
        return PropertyCopyUtil.copyToClass(this, FactoryResourceInfo.class);
    }
}