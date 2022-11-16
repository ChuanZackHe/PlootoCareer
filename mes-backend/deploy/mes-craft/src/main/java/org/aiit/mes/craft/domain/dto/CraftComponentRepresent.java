package org.aiit.mes.craft.domain.dto;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aiit.mes.common.constant.Constants;
import org.aiit.mes.common.constant.CraftStepTypeEnum;
import org.aiit.mes.common.constant.DataStateEnum;
import org.aiit.mes.craft.domain.dao.entity.CraftComponentEntity;
import org.aiit.mes.craft.domain.vo.MaterialInfoV;
import org.aiit.mes.craft.domain.vo.OperationInfoV;
import org.aiit.mes.craft.domain.vo.QcInfoV;

import javax.validation.constraints.Min;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName CraftComponentRepresent
 * @Description
 * @createTime 2021.08.30 16:33
 */
@Data
@ApiModel(value = "组件展示对象")
@NoArgsConstructor
@AllArgsConstructor
public class CraftComponentRepresent {

    /**
     * 空白出库模板 物料图标 iconfont  icon-buxiugang
     */
    public static final CraftComponentRepresent T_MATERIAL_STOCK_OUT = new CraftComponentRepresent(1L, "物料出库模板",
                                                                                                   CraftStepTypeEnum.MATERIAL_TRANSFER,
                                                                                                   null,
                                                                                                   "icon-buxiugang",
                                                                                                   -1L,-1L,
                                                                                                   1,
                                                                                                   MaterialInfoV.TEMPLATE_STOCK_OUT,
                                                                                                   null, null,
                                                                                                   DataStateEnum.ACTIVE,
                                                                                                   Constants.DB_STATUS_ACTIVE_DATE,
                                                                                                   "system",
                                                                                                   Constants.DB_STATUS_ACTIVE_DATE,
                                                                                                   "system");

    /**
     * 空白入库模板 物料图标 iconfont  icon-buxiugang
     */
    public static final CraftComponentRepresent T_MATERIAL_STOCK_IN = new CraftComponentRepresent(2L, "物料入库模板",
                                                                                                  CraftStepTypeEnum.MATERIAL_TRANSFER,
                                                                                                  null,
                                                                                                  "icon-buxiugang", -1L,
                                                                                                  -1L,
                                                                                                  2,
                                                                                                  MaterialInfoV.TEMPLATE_STOCK_IN,
                                                                                                  null, null,
                                                                                                  DataStateEnum.ACTIVE,
                                                                                                  Constants.DB_STATUS_ACTIVE_DATE,
                                                                                                  "system",
                                                                                                  Constants.DB_STATUS_ACTIVE_DATE,
                                                                                                  "system");

    /**
     * 空白现场流转模板 物料图标 iconfont  icon-buxiugang
     */
    public static final CraftComponentRepresent T_MATERIAL_ON_THE_SCENE = new CraftComponentRepresent(3L, "物料现场流转-输出模板",
                                                                                                      CraftStepTypeEnum.MATERIAL_TRANSFER,
                                                                                                      null,
                                                                                                      "icon-buxiugang",
                                                                                                      -1L,
                                                                                                      -1L,
                                                                                                      3,
                                                                                                      MaterialInfoV.TEMPLATE_ON_THE_SCENE,
                                                                                                      null, null,
                                                                                                      DataStateEnum.ACTIVE,
                                                                                                      Constants.DB_STATUS_ACTIVE_DATE,
                                                                                                      "system",
                                                                                                      Constants.DB_STATUS_ACTIVE_DATE,
                                                                                                      "system");

    /**
     * 空白现场流转模板 物料图标 iconfont  icon-buxiugang
     */
    public static final CraftComponentRepresent T_MATERIAL_ON_THE_SCENE_INPUT = new CraftComponentRepresent(7L,
                                                                                                            "物料现场流转-输入模板",
                                                                                                            CraftStepTypeEnum.MATERIAL_TRANSFER,
                                                                                                            null,
                                                                                                            "icon-buxiugang",
                                                                                                            -1L,
                                                                                                            -1L,
                                                                                                            7,
                                                                                                            MaterialInfoV.TEMPLATE_ON_THE_SCENE_INPUT,
                                                                                                            null, null,
                                                                                                            DataStateEnum.ACTIVE,
                                                                                                            Constants.DB_STATUS_ACTIVE_DATE,
                                                                                                            "system",
                                                                                                            Constants.DB_STATUS_ACTIVE_DATE,
                                                                                                            "system");

    /**
     * 空白 普通 加工模板 工序图标 fa fa-gavel
     */
    public static final CraftComponentRepresent T_OPERATION_COMMON = new CraftComponentRepresent(4L, "普通加工模板",
                                                                                                 CraftStepTypeEnum.OPERATE,
                                                                                                 null, "fa fa-gavel",
                                                                                                 -1L,
                                                                                                 -1L,
                                                                                                 4,
                                                                                                 null, null,
                                                                                                 OperationInfoV.TEMPLATE_OP_COMMON,
                                                                                                 DataStateEnum.ACTIVE,
                                                                                                 Constants.DB_STATUS_ACTIVE_DATE,
                                                                                                 "system",
                                                                                                 Constants.DB_STATUS_ACTIVE_DATE,
                                                                                                 "system");

    /**
     * 空白 组装 加工模板 工序图标 fa fa-gavel
     */
    public static final CraftComponentRepresent T_OPERATION_ASSEMBLE = new CraftComponentRepresent(5L, "组装加工模板",
                                                                                                   CraftStepTypeEnum.OPERATE,
                                                                                                   null, "fa fa-gavel",
                                                                                                   -1L,
                                                                                                   -1L,
                                                                                                   4,
                                                                                                   null, null,
                                                                                                   OperationInfoV.TEMPLATE_OP_ASSEMBLE,
                                                                                                   DataStateEnum.ACTIVE,
                                                                                                   Constants.DB_STATUS_ACTIVE_DATE,
                                                                                                   "system",
                                                                                                   Constants.DB_STATUS_ACTIVE_DATE,
                                                                                                   "system");

    /**
     * 质检图标 fa fa-hand-paper-o
     */
    public static final CraftComponentRepresent T_QC = new CraftComponentRepresent(6L, "质检模板",
                                                                                   CraftStepTypeEnum.QC,
                                                                                   null, "fa fa-hand-paper-o", -1L,
                                                                                   -1L,
                                                                                   5,
                                                                                   null,
                                                                                   QcInfoV.TEMPLATE_QC, null,
                                                                                   DataStateEnum.ACTIVE,
                                                                                   Constants.DB_STATUS_ACTIVE_DATE,
                                                                                   "system",
                                                                                   Constants.DB_STATUS_ACTIVE_DATE,
                                                                                   "system");

    /**
     * 模板列表
     */
    public static final List<CraftComponentRepresent> TEMPLATES = Arrays.asList(T_MATERIAL_STOCK_OUT,
                                                                                T_MATERIAL_STOCK_IN,
                                                                                T_MATERIAL_ON_THE_SCENE,
                                                                                T_MATERIAL_ON_THE_SCENE_INPUT,
                                                                                T_OPERATION_COMMON,
                                                                                T_OPERATION_ASSEMBLE,
                                                                                T_QC);

    @ApiModelProperty(value = "组件id")
    private Long id;

    @ApiModelProperty(value = "组件名称")
    private String name;

    @ApiModelProperty(value = "组件类型")
    private CraftStepTypeEnum type;

    @ApiModelProperty(value = "组件描述")
    private String description;

    @ApiModelProperty(value = "前端图标id")
    @JsonProperty(value = "icon_id")
    private String iconId;

    @ApiModelProperty(value = "操作角色id")
    @JsonProperty(value = "role_id")
    private Long roleId;

    @ApiModelProperty(value = "完成后二次检验角色id, 默认为空")
    @JsonProperty("verify_role_id")
    private Long verifyRoleId;

    @ApiModelProperty(value = "基于的模板id")
    @JsonProperty("template_id")
    private Integer templateId;

    /**
     * 物料流转相关
     * material_id	bigint(20)  	default 0	物料id
     * material_num	int		物料数量
     * transfer_type	varchar(20)	default 'NA'	流转类型：出库、入库、线上流转
     */

    @ApiModelProperty(value = "物料流转信息")
    @JsonProperty(value = "material_info")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private MaterialInfoV materialInfo;

    /**
     * 质检相关
     */
    @ApiModelProperty(value = "质检信息")
    @JsonProperty(value = "qc_info")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private QcInfoV qcInfo;

    /**
     * 加工相关
     */

    @ApiModelProperty(value = "加工信息")
    @JsonProperty(value = "op_info")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private OperationInfoV opInfo;

    /**
     * 用户定义状态 状态:DataStateEnum.ACTIVE/
     */
    private DataStateEnum state;

    /**
     * 新建时间
     * 设置MyMetaObjectHandler在insert的时候填充
     */
    @JsonProperty(value = "create_time")
    private Date createTime;

    /**
     * 新建人
     * 设置MyMetaObjectHandler在insert的时候填充
     */
    @JsonProperty(value = "create_username")
    private String createUsername;

    /**
     * 更新时间
     * 设置MyMetaObjectHandler在insert或update的时候填充
     */
    @JsonProperty(value = "update_time")
    private Date updateTime;

    /**
     * 更新人
     * 设置MyMetaObjectHandler在insert或update的时候填充
     */
    @JsonProperty(value = "update_username")
    private String updateUsername;

    public static CraftComponentRepresent fromEntity(CraftComponentEntity entity) {
        CraftComponentRepresent repr = new CraftComponentRepresent();
        BeanUtil.copyProperties(entity, repr);
        return repr;
    }
}
