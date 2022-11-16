package org.aiit.mes.factory.domain.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import org.aiit.mes.common.iface.IToRepresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aiit.mes.common.constant.Constants;
import org.aiit.mes.common.constant.DataStateEnum;
import org.aiit.mes.common.util.PropertyCopyUtil;
import org.aiit.mes.factory.domain.dto.ResourceDetailDto;
import org.aiit.mes.factory.domain.dto.ResourceRepresentDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName FcResource
 * @Description 数据库交互实体：工厂通用资源对象，资源对象包含：厂房-车间-工位/机器、仓库-货架-库位等；
 * @createTime 2021.08.10 14:02
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "fac_resource_info")
public class FactoryResourceInfo extends Model<FactoryResourceInfo> implements IToRepresent {

    /**
     * 厂内资源id（内部数据库使用）
     * 设置mybatis-plus返回主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 资源编码，用户使用
     */

    private String code;

    /**
     * 资源类型 使用 FactoryResourceTypeEnum 的 type
     */
    private Integer type;

    //名称
    private String name;

    //父资源代码
    private String parentCode;

    //所属顶级实体代码
    private String ancestorCode;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 新建人
     * 设置MyMetaObjectHandler在insert的时候填充
     */
    @TableField(fill = FieldFill.INSERT)
    private String createUsername;

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
     * 企业id
     */
    @TableField(fill = FieldFill.INSERT)
    private String tenantId;

    /**
     * 是否删除
     */
    @TableLogic(delval = Constants.DB_STATUS_DELETED, value = Constants.DB_STATUS_ACTIVE)
    @TableField(fill = FieldFill.INSERT)
    private Date isDeleted;

    /**
     * 用户定义状态 状态:启用，冻结，空闲，忙碌
     */
    private DataStateEnum state;

    //产能，预留字段
    private String capability;

    //描述，预留字段
    @TableField("`DESC`")
    private String desc;

    // 工厂概况图中x坐标
    private Integer xRel;

    // 工厂概况图中y坐标
    private Integer yRel;

    /**
     * 转化为（对外）展示对象
     *
     * @return 对应DTO对象
     */
    @Override
    public ResourceRepresentDto toRepresent() {
        return PropertyCopyUtil.copyToClass(this, ResourceRepresentDto.class);
    }

    public ResourceDetailDto toDetail() {
        return PropertyCopyUtil.copyToClass(this, ResourceDetailDto.class);
    }

    @Override
    public Serializable pkVal() {
        return id;
    }
}