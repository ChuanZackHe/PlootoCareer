package org.aiit.mes.craft.domain.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aiit.mes.common.constant.DataStateEnum;
import org.aiit.mes.common.entity.BaseEntity;
import org.aiit.mes.craft.domain.dto.CraftFlowRepresent;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName CraftFlowEntity
 * @Description 工艺流程对象
 * @createTime 2021.08.20 11:02
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "craft_flow")
public class CraftFlowEntity extends BaseEntity<CraftFlowEntity> {

    private String name;

    private Integer bindResourceId;

    /**
     * 用户定义状态 状态:DataStateEnum.ACTIVE/
     */
    private DataStateEnum state;

    @Override
    public CraftFlowRepresent toRepresent() {
        return CraftFlowRepresent.fromEntity(this);
    }
}
