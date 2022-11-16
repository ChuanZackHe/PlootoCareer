package org.aiit.mes.craft.domain.dto;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.aiit.mes.common.constant.DataStateEnum;
import org.aiit.mes.common.page.BasePage;
import org.aiit.mes.craft.domain.dao.entity.CraftFlowEntity;

import javax.validation.constraints.Min;
import java.util.Optional;

import static org.aiit.mes.common.constant.Constants.STATE_TYPES;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName CraftFlowRepresent
 * @Description
 * @createTime 2021.09.07 16:54
 */
@Data
@ApiModel(value = "分页查询工艺流程请求体")
public class CraftFlowQuery extends BasePage<CraftFlowEntity> {

    @JsonProperty(value = "bind_resource_id")
    @ApiModelProperty(value = "绑定工厂资源id", required = false)
    private Integer bindResourceId;

    @Min(1)
    @JsonProperty(value = "output_material_id")
    @ApiModelProperty(value = "流程产出物料id", required = false)
    private Long outputMaterialId;

    @ApiModelProperty(value = "流程激活状态, 默认ACTIVE", required = false, example = "ACTIVE", allowableValues = STATE_TYPES)
    private DataStateEnum state;

    @Override
    public QueryWrapper<CraftFlowEntity> toQueryWrapper() {
        QueryWrapper<CraftFlowEntity> wrapper = new QueryWrapper<>();

        // 默认查询所有绑定资源id
        Optional.ofNullable(this.bindResourceId).ifPresent(id -> wrapper.eq("bind_resource_id", id));
        // 默认查询active的state
        wrapper.eq("state", Optional.ofNullable(this.state).orElse(DataStateEnum.ACTIVE));
        return wrapper;
    }

    @Override
    public LambdaQueryWrapper<CraftFlowEntity> toLambdaQueryWrapper() {
        LambdaQueryWrapper<CraftFlowEntity> wrapper = super.toLambdaQueryWrapper();
        // 默认查询所有绑定资源id
        Optional.ofNullable(this.bindResourceId).ifPresent(id -> wrapper.eq(CraftFlowEntity::getBindResourceId, id));
        // 默认查询active的state
        wrapper.eq(CraftFlowEntity::getState, Optional.ofNullable(this.state).orElse(DataStateEnum.ACTIVE));
        wrapper.orderByDesc(CraftFlowEntity::getId);
        return wrapper;
    }
}
