package org.aiit.mes.craft.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aiit.mes.common.constant.CraftStepTypeEnum;
import org.aiit.mes.common.constant.DataStateEnum;
import org.aiit.mes.craft.domain.vo.TemplateParamV;

import java.util.List;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName CraftTemplatePresent
 * @Description
 * @createTime 2021.08.26 16:58
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CraftTemplateRepresent {

    private Integer id;

    private String name;

    private CraftStepTypeEnum type;

    @JsonProperty(value = "is_system")
    private Boolean isSystem;

    private List<TemplateParamV> params;

    private DataStateEnum state;

}
