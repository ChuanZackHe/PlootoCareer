package org.aiit.mes.craft.domain.dto;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.aiit.mes.common.constant.DataStateEnum;
import org.aiit.mes.common.exception.ParamInvalidException;
import org.aiit.mes.common.iface.IToEntity;
import org.aiit.mes.craft.domain.dao.entity.CraftFlowEntity;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

import static org.aiit.mes.common.constant.Constants.MSG_UPDATE_ID_NOT_MATCH_IN_PATH;
import static org.aiit.mes.common.constant.Constants.STATE_TYPES;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName CraftFlowRepresent
 * @Description
 * @createTime 2021.09.07 16:54
 */
@Data
@ApiModel(value = "更新工艺流程请求体")
public class CraftFlowUpdateCmd implements IToEntity {

    @NotNull
    @Min(1)
    @ApiModelProperty(value = "流程id", required = true)
    private Long id;

    @NotBlank
    @ApiModelProperty(value = "流程名称", required = true)
    private String name;

    @JsonProperty(value = "bind_resource_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull
    @Min(1)
    @ApiModelProperty(value = "绑定工厂资源id", required = true)
    private Integer bindResourceId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "流程激活状态", required = false, allowableValues = STATE_TYPES)
    private DataStateEnum state;

    @Override
    public CraftFlowEntity toEntity() {
        CraftFlowEntity flow = new CraftFlowEntity();
        BeanUtil.copyProperties(this, flow);
        return flow;
    }

    public void compareId(Long id) {
        if (Objects.isNull(this.id)) {
            this.id = id;
            return;
        }
        if (!id.equals(this.id)) {
            throw new ParamInvalidException(MSG_UPDATE_ID_NOT_MATCH_IN_PATH);
        }
    }
}
