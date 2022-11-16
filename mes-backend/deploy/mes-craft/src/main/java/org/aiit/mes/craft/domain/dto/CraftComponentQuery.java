package org.aiit.mes.craft.domain.dto;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.aiit.mes.common.constant.CraftStepTypeEnum;
import org.aiit.mes.common.constant.DataStateEnum;
import org.aiit.mes.common.page.BasePage;
import org.aiit.mes.craft.domain.dao.entity.CraftComponentEntity;

import java.util.Optional;

import static org.aiit.mes.common.constant.Constants.CRAFT_TYPES;
import static org.aiit.mes.common.constant.Constants.STATE_TYPES;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName CraftTemplateQuery
 * @Description
 * @createTime 2021.09.07 10:38
 */
@Data
@ApiModel(value = "批量查询组件过滤器", description = "批量查询组件过滤参数")
public class CraftComponentQuery extends BasePage<CraftComponentEntity> {

    @ApiModelProperty(value = "组件名，支持模糊搜索", required = false)
    private String name;

    @ApiModelProperty(value = "启用状态，默认ACTIVE", required = false, allowableValues = STATE_TYPES)
    DataStateEnum state;

    @ApiModelProperty(value = "组件类型", required = false, allowableValues = CRAFT_TYPES)
    private CraftStepTypeEnum type;

    @Override
    public LambdaQueryWrapper<CraftComponentEntity> toLambdaQueryWrapper() {
        LambdaQueryWrapper<CraftComponentEntity> wrapper = super.toLambdaQueryWrapper();
        Optional.ofNullable(this.type).ifPresent(t -> wrapper.eq(CraftComponentEntity::getType, t));
        Optional.ofNullable(this.name).ifPresent(t -> wrapper.like(CraftComponentEntity::getName, t));
        // 默认查询active的state
        wrapper.eq(CraftComponentEntity::getState, Optional.ofNullable(this.state).orElse(DataStateEnum.ACTIVE));
        wrapper.orderByDesc(CraftComponentEntity::getId);
        return wrapper;
    }

    @Override
    public Page<CraftComponentEntity> toPage() {
        return new Page<CraftComponentEntity>(page, size);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CraftComponentQuery{");
        sb.append("type=").append(type);
        sb.append(", state=").append(state);
        sb.append(", page=").append(page);
        sb.append(", size=").append(size);
        sb.append('}');
        return sb.toString();
    }
}
