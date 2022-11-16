package org.aiit.mes.craft.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.aiit.mes.common.constant.DataStateEnum;
import org.aiit.mes.common.exception.ParamInvalidException;
import org.aiit.mes.common.iface.IToEntity;
import org.aiit.mes.craft.domain.dao.entity.CraftComponentEntity;
import org.aiit.mes.craft.domain.vo.MaterialInfoV;
import org.aiit.mes.craft.domain.vo.OperationInfoV;
import org.aiit.mes.craft.domain.vo.QcInfoV;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

import static org.aiit.mes.common.constant.Constants.MSG_UPDATE_ID_NOT_MATCH_IN_PATH;
import static org.aiit.mes.common.constant.Constants.STATE_TYPES;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName CraftComponentCreateDto
 * @Description 创建组件cmd
 * @createTime 2021.08.30 17:26
 */
@Data
@ApiModel(value = "更新组件请求")
public class CraftComponentUpdateCmd implements IToEntity {

    @ApiModelProperty(value = "组件id, 和url中的保持一致", required = true)
    private Long id;

    /**
     * 组件名称
     */
    @ApiModelProperty(value = "组件名")
    private String name;

    /**
     * 组件描述
     */
    @ApiModelProperty(value = "组件描述")
    private String description;

    /**
     * 前端图标id
     */
    @Length(min = 1, max = 32)
    @NotBlank
    @ApiModelProperty(value = "图标id", example = "1")
    @JsonProperty("icon_id")
    private String iconId;

    /**
     * 操作角色
     */
    @ApiModelProperty(value = "绑定角色id")
    @JsonProperty("role_id")
    private Long roleId;


    /**
     * 完成后二次检验角色id
     */
    @ApiModelProperty(value = "完成后二次检验角色id, 默认为空")
    @JsonProperty("verify_role_id")
    private Long verifyRoleId;

    @ApiModelProperty(value = "启用状态", allowableValues = STATE_TYPES)
    private DataStateEnum state;

    /**
     * 物料流转相关
     */
    @JsonProperty("material_info")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "物料流转信息")
    private MaterialInfoV materialInfo;

    /**
     * 质检相关
     */
    @JsonProperty("qc_info")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "质检信息")
    private QcInfoV qcInfo;

    /**
     * 加工相关
     */
    @JsonProperty("op_info")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "加工信息")
    private OperationInfoV opInfo;

    @Override
    public CraftComponentEntity toEntity() {
        return null;
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
