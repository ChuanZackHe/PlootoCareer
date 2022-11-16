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
import org.aiit.mes.craft.domain.dao.entity.CraftComponentEntity;
import org.aiit.mes.craft.domain.vo.MaterialInfoV;
import org.aiit.mes.craft.domain.vo.OperationInfoV;
import org.aiit.mes.craft.domain.vo.QcInfoV;
import org.hibernate.validator.constraints.Length;
import org.springframework.util.Assert;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static org.aiit.mes.common.constant.Constants.CRAFT_TYPES;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName CraftComponentCreateDto
 * @Description 创建组件cmd
 * @createTime 2021.08.30 17:26
 */
@Data
@ApiModel(value = "创建组件请求")
public class CraftComponentCreateCmd implements IToEntity, IValidate {

    /**
     * 组件名称
     */
    @NotBlank
    @ApiModelProperty(value = "组件名", required = true)
    private String name;

    /**
     * 模板类型
     */
    @NotNull
    @ApiModelProperty(value = "组件类型", required = true, allowableValues = CRAFT_TYPES, example = "MATERIAL_TRANSFER")
    private CraftStepTypeEnum type;

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
    @ApiModelProperty(value = "图标id", required = true)
    @JsonProperty("icon_id")
    private String iconId;

    /**
     * 操作角色
     */
    @Min(1)
    @NotNull
    @ApiModelProperty(value = "绑定角色id", required = true)
    @JsonProperty("role_id")
    private Long roleId;


    /**
     * 完成后二次检验角色id
     */
    @Min(1)
    @ApiModelProperty(value = "完成后二次检验角色id, 默认为空", required = false)
    @JsonProperty("verify_role_id")
    private Long verifyRoleId;
    /**
     * 模板id
     */
    @Min(1)
    @ApiModelProperty(value = "基于的模板id", required = true)
    @JsonProperty("template_id")
    private Integer templateId;

    /**
     * 物料流转相关
     */
    @JsonProperty("material_info")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "物料流转信息")
    @Valid
    private MaterialInfoV materialInfo;

    /**
     * 质检相关
     */
    @JsonProperty("qc_info")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "质检信息")
    @Valid
    private QcInfoV qcInfo;

    /**
     * 加工相关
     */
    @JsonProperty("op_info")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "加工信息")
    @Valid
    private OperationInfoV opInfo;

    @Override
    public CraftComponentEntity toEntity() {
        this.validate();
        CraftComponentEntity entity = new CraftComponentEntity();
        BeanUtil.copyProperties(this, entity);
        // 提取
        entity.syncInfo();
        return entity;
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
