package org.aiit.mes.craft.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.aiit.mes.common.iface.IToEntity;
import org.aiit.mes.craft.domain.dao.entity.CraftFlowEntity;

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
@ApiModel(value = "新建工艺流程请求体")
public class CraftFlowCreateCmd implements IToEntity {

    @NotBlank
    @ApiModelProperty(value = "流程名称", required = true)
    private String name;

    @JsonProperty(value = "bind_resource_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "绑定工厂资源id", required = false)
    private Integer bindResourceId;

    @Override
    public CraftFlowEntity toEntity() {
        return CraftFlowEntity.builder().name(name).bindResourceId(bindResourceId).build();
    }
}
