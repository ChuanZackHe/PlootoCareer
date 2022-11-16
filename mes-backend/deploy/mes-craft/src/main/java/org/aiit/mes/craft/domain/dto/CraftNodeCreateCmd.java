package org.aiit.mes.craft.domain.dto;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.aiit.mes.common.constant.CraftStepTypeEnum;
import org.aiit.mes.common.exception.ParamInvalidException;
import org.aiit.mes.common.iface.IToEntity;
import org.aiit.mes.common.iface.IValidate;
import org.aiit.mes.craft.domain.dao.entity.CraftFlowNodeEntity;
import org.aiit.mes.craft.domain.vo.MaterialInfoV;
import org.aiit.mes.craft.domain.vo.OperationInfoV;
import org.aiit.mes.craft.domain.vo.QcInfoV;
import org.hibernate.validator.constraints.Length;
import org.springframework.util.Assert;

import javax.validation.constraints.*;

import static org.aiit.mes.common.constant.Constants.CRAFT_TYPES;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName CraftNodeRepresent
 * @Description
 * @createTime 2021.09.07 16:55
 */
@Data
@ApiModel(value = "工艺步骤创建结构体")
public class CraftNodeCreateCmd implements IToEntity, IValidate {

    @NotEmpty(message = "id不允许为空")
    @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", message = "id必须为uuid格式")
    @ApiModelProperty(value = "步骤uuid", required = true)
    private String id;

    @NotBlank
    @ApiModelProperty(value = "步骤名称", required = true)
    private String name;

    @NotNull
    @ApiModelProperty(value = "步骤类型", required = true, allowableValues = CRAFT_TYPES)
    private CraftStepTypeEnum type;

    @ApiModelProperty(value = "步骤描述")
    private String description;

    @Length(min = 1, max = 32)
    @NotBlank
    @JsonProperty("icon_id")
    @ApiModelProperty(value = "图标id")
    private String iconId;

    @Min(1)
    @JsonProperty("role_id")
    @ApiModelProperty(value = "绑定角色id")
    private Long roleId;

    @Min(1)
    @ApiModelProperty(value = "完成后二次检验角色id, 默认为空", required = false)
    @JsonProperty("verify_role_id")
    private Long verifyRoleId;

    @JsonProperty("material_info")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "物料流转信息")
    private MaterialInfoV materialInfo;

    @JsonProperty("qc_info")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "质检信息")
    private QcInfoV qcInfo;

    @JsonProperty("op_info")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "加工信息")
    private OperationInfoV opInfo;

    @Override
    public CraftFlowNodeEntity toEntity() {
        this.validate();
        CraftFlowNodeEntity nodeEntity = new CraftFlowNodeEntity();
        BeanUtil.copyProperties(this, nodeEntity);
        nodeEntity.syncInfo();
        return nodeEntity;
    }

    @Override
    public boolean validate() throws ParamInvalidException {
        try {
            switch (this.type) {
                case MATERIAL_TRANSFER:
                    Assert.isNull(this.opInfo, "包含和 MATERIAL_TRANSFER 物料流转类型无关的参数op_info");
                    Assert.isNull(this.qcInfo, "包含和 MATERIAL_TRANSFER 物料流转类型无关的参数qc_info");
                    Assert.notNull(this.materialInfo, "物料信息不可为空");
                    this.materialInfo.validate();
                    break;
                case QC:
                    Assert.isNull(this.opInfo, "包含和 QC 质检类型无关的参数op_info");
                    Assert.isNull(this.materialInfo, "包含和 QC 质检类型无关的参数material_info");
                    Assert.notNull(this.qcInfo, "质检信息不可为空");
                    this.qcInfo.validate();
                    break;
                case OPERATE:
                    Assert.isNull(this.qcInfo, "包含和 OPERATE 加工类型无关的参数qc_info");
                    Assert.isNull(this.materialInfo, "包含和 OPERATE 加工类型无关的参数material_info");
                    Assert.notNull(this.opInfo, "加工信息不可为空");
                    this.opInfo.validate();
                    break;
                default:
                    break;
            }
            return true;
        }
        catch (Exception e) {
            throw new ParamInvalidException(e.getMessage());
        }
    }
}
