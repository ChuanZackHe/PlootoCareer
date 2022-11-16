package org.aiit.mes.factory.domain.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import org.aiit.mes.common.iface.IToRepresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aiit.mes.common.constant.Constants;
import org.aiit.mes.common.util.PropertyCopyUtil;
import org.aiit.mes.factory.domain.dto.FactoryResourceGroupRelationDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lzj
 * @version 1.0.0
 * @ClassName FactoryResourceGroupRelation.java
 * @Description TODO
 * @createTime 2021年09月14日 16:21:00
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("fac_resource_group_relation")
public class FactoryResourceGroupRelation extends Model<FactoryResourceGroupRelation> implements IToRepresent {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 工厂资源编码
     */
    private String code;

    /**
     * 资源组ID
     */
    private Long resourceGroupId;

    /**
     * 资源组名称
     */
    private String resourceGroupName;

    @TableField(fill = FieldFill.INSERT)
    private String tenantId;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT)
    private String createUsername;

    @TableField(fill = FieldFill.INSERT)
    private Date updateTime;

    @TableField(fill = FieldFill.INSERT)
    private String updateUsername;

    @TableLogic(delval = Constants.DB_STATUS_DELETED, value = Constants.DB_STATUS_ACTIVE)
    private Date isDeleted;

    @Override
    public Serializable pkVal() {
        return id;
    }

    @Override
    public FactoryResourceGroupRelationDto toRepresent() {
        return PropertyCopyUtil.copyToClass(this, FactoryResourceGroupRelationDto.class);
    }

    public String generateKey() {
        return code + "_" + resourceGroupId;
    }
}
