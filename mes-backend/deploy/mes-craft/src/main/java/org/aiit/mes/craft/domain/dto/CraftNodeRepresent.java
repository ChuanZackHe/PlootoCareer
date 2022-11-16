package org.aiit.mes.craft.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.aiit.mes.common.constant.CraftStepTypeEnum;
import org.aiit.mes.common.util.PropertyCopyUtil;
import org.aiit.mes.craft.domain.dao.entity.CraftFlowNodeEntity;
import org.aiit.mes.common.primitive.Coordinate;
import org.aiit.mes.craft.domain.vo.MaterialInfoV;
import org.aiit.mes.craft.domain.vo.OperationInfoV;
import org.aiit.mes.craft.domain.vo.QcInfoV;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName CraftNodeRepresent
 * @Description
 * @createTime 2021.09.07 16:55
 */
@Data
@ApiModel(value = "工艺步骤展示")
public class CraftNodeRepresent {

    private String id;

    private String name;

    private CraftStepTypeEnum type;

    private String description;

    @JsonProperty("icon_id")
    private String iconId;

    @JsonProperty("role_id")
    private Long roleId;

    @ApiModelProperty(value = "完成后二次检验角色id, 默认为空")
    @JsonProperty("verify_role_id")
    private Long verifyRoleId;

    @JsonProperty("material_info")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private MaterialInfoV materialInfo;

    @JsonProperty("qc_info")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private QcInfoV qcInfo;

    @JsonProperty("op_info")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private OperationInfoV opInfo;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String extra;

    @JsonProperty("update_time")
    private Date updateTime;

    @ApiModelProperty(value = "坐标")
    private Coordinate coordinate;

    public static CraftNodeRepresent fromEntity(CraftFlowNodeEntity node) {
        return PropertyCopyUtil.copyToClass(node, CraftNodeRepresent.class);
    }

    public static List<CraftNodeRepresent> fromEntity(List<CraftFlowNodeEntity> nodes) {
        return nodes.stream()
                    .map(n -> CraftNodeRepresent.fromEntity(n))
                    .collect(Collectors.toList());
    }
}
