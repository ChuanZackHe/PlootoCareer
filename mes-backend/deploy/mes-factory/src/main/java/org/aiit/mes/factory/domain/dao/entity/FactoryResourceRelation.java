package org.aiit.mes.factory.domain.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import org.aiit.mes.common.iface.IToRepresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aiit.mes.common.util.PropertyCopyUtil;
import org.aiit.mes.factory.domain.dto.ResourceRelationDto;
import org.aiit.mes.factory.domain.vo.FactoryRelationPair;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lzj
 * @version 1.0.0
 * @ClassName FactoryResourceRelation.java
 * @Description 平级资源之间的关系表，定义一个先后顺序
 * @createTime 2021年08月16日 10:26:00
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "fac_resource_relation")
public class FactoryResourceRelation extends Model<FactoryResourceRelation> implements IToRepresent {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    //前驱节点代码
    private String preCode;

    //后继节点代码
    private String afterCode;

    //租户ID
    @TableField(fill = FieldFill.INSERT)
    private String tenantId;

    /**
     * 新建时间
     * 设置MyMetaObjectHandler在insert的时候填充
     */
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


    @Override
    public Serializable pkVal() {
        return id;
    }

    public FactoryRelationPair toMapKey() {
        return new FactoryRelationPair(preCode, afterCode);
    }

    @Override
    public ResourceRelationDto toRepresent() {
        return PropertyCopyUtil.copyToClass(this, ResourceRelationDto.class);
    }
}