package org.aiit.mes.craft.domain.dao.entity;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aiit.mes.common.constant.CraftStepTypeEnum;
import org.aiit.mes.common.constant.DataStateEnum;
import org.aiit.mes.common.entity.BaseEntity;
import org.aiit.mes.craft.domain.dto.CraftTemplateRepresent;
import org.aiit.mes.craft.domain.typehandler.ListParamsTypeHandler;
import org.aiit.mes.craft.domain.vo.TemplateParamV;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName CraftStep
 * @Description 工艺步骤组件对象
 * @createTime 2021.08.19 10:14
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "craft_template", autoResultMap = true)
public class CraftTemplateEntity extends BaseEntity<CraftTemplateEntity> {

    /**
     * 模板名称
     */
    private String name;

    /**
     * 模板类型
     */
    private CraftStepTypeEnum type;

    /**
     * 描述
     */
    private String description;

    /**
     * 是否为系统内置
     */
    private Boolean isSystem;

    /**
     * 模板参数 数据库中为 String类型的param字符串
     * 使用自定义的listParamTypeHandler，用于将sql的json转为list<obj>
     */
    @TableField(typeHandler = ListParamsTypeHandler.class)
    private List<TemplateParamV> params;

    /**
     * 用户定义状态 状态:激活，冻结
     */
    private DataStateEnum state;

    @Override
    public CraftTemplateRepresent toRepresent() {
        CraftTemplateRepresent templatePresentDto = new CraftTemplateRepresent();
        BeanUtil.copyProperties(this, templatePresentDto);
        return templatePresentDto;
    }

    public CraftTemplateEntity addParam(TemplateParamV param) {
        if (Objects.isNull(param)) {
            return this;
        }
        Optional.ofNullable(this.params)
                .orElseGet(() -> {
                    this.params = new ArrayList<>();
                    return this.params;
                })
                .add(param);
        return this;
    }
}
