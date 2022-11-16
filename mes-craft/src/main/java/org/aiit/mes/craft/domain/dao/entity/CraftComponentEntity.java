package org.aiit.mes.craft.domain.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aiit.mes.common.constant.CraftStepTypeEnum;
import org.aiit.mes.common.constant.DataStateEnum;
import org.aiit.mes.common.constant.MaterialTransferEnum;
import org.aiit.mes.common.entity.BaseEntity;
import org.aiit.mes.craft.domain.dto.CraftComponentRepresent;
import org.aiit.mes.craft.domain.vo.MaterialInfoV;
import org.aiit.mes.craft.domain.vo.OperationInfoV;
import org.aiit.mes.craft.domain.vo.QcInfoV;

import java.util.Optional;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName CraftComponentEntity
 * @Description 工艺流程-组件
 * @createTime 2021.08.27 14:00
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "craft_component", autoResultMap = true)
public class CraftComponentEntity extends BaseEntity<CraftComponentEntity> {

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
     * 模板id
     */
    private Integer templateId;

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
     * 用户定义状态 状态:DataStateEnum.ACTIVE/
     */
    private DataStateEnum state;

    /**
     * 从内部结构体同步信息到外部
     */
    public void syncInfo() {
        Optional.ofNullable(this.materialInfo).ifPresent(m -> {
            this.materialId = m.getId();
            this.materialNum =
                    m.getNum();
            this.transferType = m.getTransferType();
        });

        Optional.ofNullable(this.opInfo).ifPresent(o -> {this.opResourceCode = o.getOpResourceCode();});
    }

    @Override
    public CraftComponentRepresent toRepresent() {
        return CraftComponentRepresent.fromEntity(this);
    }
}
