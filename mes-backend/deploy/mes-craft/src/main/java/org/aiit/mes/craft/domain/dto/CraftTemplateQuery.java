package org.aiit.mes.craft.domain.dto;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.aiit.mes.common.constant.CraftStepTypeEnum;
import org.aiit.mes.common.constant.DataStateEnum;
import org.aiit.mes.craft.domain.dao.entity.CraftTemplateEntity;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
@ApiModel(value = "批量查询过滤器", description = "批量查询模板过滤参数")
public class CraftTemplateQuery {

    public static Page<CraftTemplateEntity> DEFAULT_PAGE = defaultPage();

    @ApiModelProperty(value = "模板类型", required = false, allowableValues = CRAFT_TYPES)
    CraftStepTypeEnum type;

    @ApiModelProperty(value = "启用状态", required = false, allowableValues = STATE_TYPES)
    DataStateEnum state;

    @Min(1)
    @ApiModelProperty(value = "查询页", required = false, allowableValues = "range[1, infinity)", example = "1")
    int page = 1;

    @Min(1)
    @Max(2000)
    @ApiModelProperty(value = "分页数量", required = false, allowableValues = "range[1,2000]", example = "10")
    int size = 10;

    public static QueryWrapper<CraftTemplateEntity> getQuery(CraftTemplateQuery query) {
        return Optional.ofNullable(query)
                       .map(CraftTemplateQuery::toQueryWrapper)
                       .orElse(defaultWrapper());
    }

    public static QueryWrapper<CraftTemplateEntity> defaultWrapper() {
        QueryWrapper<CraftTemplateEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("state", DataStateEnum.ACTIVE);
        return wrapper;
    }

    private static Page<CraftTemplateEntity> defaultPage() {
        return new Page<CraftTemplateEntity>(0, 10);
    }

    public static Page<CraftTemplateEntity> getPage(CraftTemplateQuery query) {
        return Optional.ofNullable(query)
                       .map(CraftTemplateQuery::toPage)
                       .orElse(DEFAULT_PAGE);
    }

    public QueryWrapper<CraftTemplateEntity> toQueryWrapper() {
        QueryWrapper<CraftTemplateEntity> wrapper = new QueryWrapper<>();
        Optional.ofNullable(this.type)
                .ifPresent(t -> wrapper.eq("type", this.type));
        // 默认查询active的state
        wrapper.eq("state", Optional.ofNullable(this.state)
                                    .orElse(DataStateEnum.ACTIVE));
        return wrapper;
    }

    public Page<CraftTemplateEntity> toPage() {
        return new Page<CraftTemplateEntity>(page, size);
    }
}
