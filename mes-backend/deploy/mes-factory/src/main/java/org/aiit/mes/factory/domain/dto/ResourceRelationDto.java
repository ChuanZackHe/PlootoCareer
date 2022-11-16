package org.aiit.mes.factory.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aiit.mes.common.iface.IToEntity;
import org.aiit.mes.common.util.PropertyCopyUtil;
import org.aiit.mes.factory.domain.dao.entity.FactoryResourceRelation;

import javax.validation.constraints.NotNull;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName ResourceRelationDto
 * @Description 新建资源绑定关系dto
 * @createTime 2021.08.10 14:34
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceRelationDto implements IToEntity {

    @NotNull
    @ApiModelProperty("前驱资源代码")
    @JsonProperty("pre_code")
    private String preCode;

    @NotNull
    @ApiModelProperty("后继资源代码")
    @JsonProperty("after_code")
    private String afterCode;

    @NotNull
    @ApiModelProperty("租户id")
    private String tenantId;

    @Override
    public FactoryResourceRelation toEntity() {
        return PropertyCopyUtil.copyToClass(this, FactoryResourceRelation.class);
    }
}
