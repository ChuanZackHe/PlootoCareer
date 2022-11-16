package org.aiit.mes.craft.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aiit.mes.common.constant.ParamTypeEnum;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName TemplateParam
 * @Description
 * @createTime 2021.08.25 11:11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TemplateParamV {

    /**
     * 参数名：前端展示用
     */
    private String name;

    /**
     * 参数属性名
     */
    private String attribute;

    /**
     * 参数类型
     */
    private ParamTypeEnum type;

    /**
     * 是否必填
     */
    @JsonProperty("is_required")
    private Boolean isRequired;

    /**
     * 默认值
     */
    @JsonProperty("default_value")
    private Object defaultValue;

    /**
     * 允许修改默认值
     */
    @JsonProperty("allow_modify")
    private Boolean allowModify;
}
