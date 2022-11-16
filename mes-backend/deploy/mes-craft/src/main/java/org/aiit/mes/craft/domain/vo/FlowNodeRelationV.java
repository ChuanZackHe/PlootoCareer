package org.aiit.mes.craft.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;
import org.aiit.mes.common.exception.ParamInvalidException;
import org.aiit.mes.common.util.UUIDUtil;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName FlowNodeRelationV
 * @Description
 * @createTime 2021.08.31 14:08
 */
@Data
@Builder
@ApiModel(value = "工艺步骤关系")
public class FlowNodeRelationV {

    @JsonProperty(value = "pre_node")
    private final String preNode;

    @JsonProperty(value = "next_node")
    private final String nextNode;

    public FlowNodeRelationV(String preNode, String nextNode) {
        // uuid格式，允许为空
        if (BooleanUtils.isFalse(validate(preNode)) || BooleanUtils.isFalse(validate(nextNode))) {
            throw new ParamInvalidException("node code must be uuid:" + this.toString());
        }
        this.nextNode = nextNode;
        this.preNode = preNode;
    }

    public static FlowNodeRelationV newRelation(String preNode, String nextNode) {
        return new FlowNodeRelationV(preNode, nextNode);
    }

    private boolean validate(String nodeCode) {
        return StringUtils.isEmpty(nodeCode) || UUIDUtil.isValidUUID(nodeCode);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FlowNodeRelationV{");
        sb.append("preNode='")
          .append(preNode)
          .append('\'');
        sb.append(", nextNode='")
          .append(nextNode)
          .append('\'');
        sb.append('}');
        return sb.toString();
    }
}
