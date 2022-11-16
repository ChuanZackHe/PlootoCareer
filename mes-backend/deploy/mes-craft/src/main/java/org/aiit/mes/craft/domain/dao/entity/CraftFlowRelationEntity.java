package org.aiit.mes.craft.domain.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aiit.mes.common.entity.BaseEntity;
import org.aiit.mes.craft.domain.typehandler.ListFlowRelationTypeHandler;
import org.aiit.mes.craft.domain.vo.FlowNodeRelationV;

import java.util.*;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName CraftFlowRelationEntity
 * @Description 表名：craft_flow_relation 工艺流程有向图表（记录图关系）  跟随工艺流程简表
 * @createTime 2021.08.20 11:04
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "craft_flow_relation", autoResultMap = true)
public class CraftFlowRelationEntity extends BaseEntity<CraftFlowRelationEntity> {

    private Long flowId;

    /**
     * 模板参数 数据库中为 String类型的param字符串
     * 使用自定义的listParamTypeHandler，用于将sql的json转为list<obj>
     */
    @TableField(typeHandler = ListFlowRelationTypeHandler.class)
    private List<FlowNodeRelationV> nodeRelations;

    public CraftFlowRelationEntity addRelations(FlowNodeRelationV relationV) {
        if (Objects.isNull(relationV)) {
            return this;
        }
        Optional.ofNullable(this.nodeRelations)
                .orElseGet(() -> {
                    this.nodeRelations = new ArrayList<>();
                    return this.nodeRelations;
                })
                .add(relationV);
        return this;
    }

    public CraftFlowRelationEntity replaceRelation(List<FlowNodeRelationV> nodeRelations) {
        this.nodeRelations = nodeRelations;
        return this;
    }

    @Override
    public List<FlowNodeRelationV> toRepresent() {
        return Optional.ofNullable(this.nodeRelations).orElse(Collections.emptyList());
    }
}
