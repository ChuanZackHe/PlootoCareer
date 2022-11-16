package org.aiit.mes.factory.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aiit.mes.common.iface.IToEntity;
import org.aiit.mes.common.util.PropertyCopyUtil;
import org.aiit.mes.factory.domain.dao.entity.FactoryResourceGroupRelation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * @author lzj
 * @version 1.0.0
 * @ClassName FactoryResourceGroupRelationDto.java
 * @Description TODO
 * @createTime 2021年09月14日 16:41:00
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FactoryResourceGroupRelationDto implements IToEntity {

    @ApiModelProperty("主键")
    @NotNull
    private Long id;

    @NotBlank
    @ApiModelProperty("工厂资源code")
    private String code;

    @NotBlank
    @ApiModelProperty("资源组ID")
    private Long resourceGroupId;

    @NotNull
    @ApiModelProperty("资源组名称")
    private String resourceGroupName;

    @Override
    public FactoryResourceGroupRelation toEntity() {
        return PropertyCopyUtil.copyToClass(this, FactoryResourceGroupRelation.class);
    }
}