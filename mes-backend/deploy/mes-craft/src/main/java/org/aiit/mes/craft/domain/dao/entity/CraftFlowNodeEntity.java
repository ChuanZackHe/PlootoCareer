package org.aiit.mes.craft.domain.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aiit.mes.common.constant.Constants;
import org.aiit.mes.common.constant.CraftStepTypeEnum;
import org.aiit.mes.common.constant.MaterialTransferEnum;
import org.aiit.mes.common.util.DoubleUtil;
import org.aiit.mes.common.primitive.Coordinate;
import org.aiit.mes.craft.domain.vo.MaterialInfoV;
import org.aiit.mes.craft.domain.vo.OperationInfoV;
import org.aiit.mes.craft.domain.vo.QcInfoV;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName CraftTemplate
 * @Description 表名：craft_flow_node 工艺流程步骤详表（组装组件拼合起来的的工艺步骤详情），
 * 当前用宽表，后续考虑非查找字段整合到json，用代码控制序列化去解析参数；跟随工艺流程简表
 * @createTime 2021.08.19 10:15
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "craft_flow_node", autoResultMap = true)
public class CraftFlowNodeEntity {

    /**
     * id（内部数据库使用）
     * 设置为手动生成的UUID
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    private Long flowId;

    /**
     * 组件名称
     */
    private String name;

    /**
     * 模板类型
     */
    private CraftStepTypeEnum type;

    /**
     * 组件描述
     */
    private String description;

    /**
     * 前端图标id
     */
    private String iconId;

    /**
     * 操作角色
     */
    private Long roleId;


    /**
     * 完成后二次检验角色id
     */
    private Long verifyRoleId;

    /**
     * 物料流转相关
     * material_id	bigint(20)  	default 0	物料id
     * material_num	int		物料数量
     * transfer_type	varchar(20)	default 'NA'	流转类型：出库、入库、线上流转
     */
    private Long materialId;

    private Double materialNum;

    private MaterialTransferEnum transferType;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private MaterialInfoV materialInfo;

    /**
     * 质检相关
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private QcInfoV qcInfo;

    /**
     * 加工相关
     */
    /**
     * 绑定工厂资源code：对应工厂概况-资源表
     */
    private String opResourceCode;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private OperationInfoV opInfo;

    /**
     * 预置字段
     */
    private String extra;

    /**
     * 默认参数
     */
    /**
     * 租户id
     */
    @TableField(fill = FieldFill.INSERT)
    private String tenantId;

    /**
     * 逻辑删除状态 状态:0-激活，2-删除
     */
    @TableLogic(delval = Constants.DB_STATUS_DELETED, value = Constants.DB_STATUS_ACTIVE)
    @TableField(fill = FieldFill.INSERT)
    private Date isDeleted;

    /**
     * 更新时间
     * 设置MyMetaObjectHandler在insert或update的时候填充
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 更新人
     * 设置MyMetaObjectHandler在insert或update的时候填充
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateUsername;

    /**
     * 坐标
     */
    @TableField(exist = false)
    private Coordinate coordinate;

    /**
     * 从内部结构体同步信息到外部
     */
    public void syncInfo() {
        Optional.ofNullable(this.materialInfo).ifPresent(m -> {
            this.materialId = m.getId();
            this.materialNum = m.getNum();
            this.transferType = m.getTransferType();
        });

        Optional.ofNullable(this.opInfo).ifPresent(o -> {this.opResourceCode = o.getOpResourceCode();});
    }

    public void multiply(Double x) {
        if (Objects.isNull(x)) {
            return;
        }
        if (Objects.nonNull(getMaterialNum())) {
            this.materialNum = DoubleUtil.multiple(this.materialNum, x);
        }
        Optional.ofNullable(getMaterialInfo()).ifPresent(m -> m.multiply(x));
        Optional.ofNullable(getOpInfo()).ifPresent(m -> m.multiply(x));
    }
}