package org.aiit.mes.craft.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.aiit.mes.common.exception.ParamInvalidException;
import org.aiit.mes.common.iface.IToEntity;
import org.aiit.mes.craft.domain.dao.entity.CraftFlowEntity;
import org.aiit.mes.craft.domain.dao.entity.CraftFlowNodeEntity;
import org.aiit.mes.craft.domain.dao.entity.CraftFlowRelationEntity;
import org.aiit.mes.craft.domain.vo.FlowNodeRelationV;
import org.apache.commons.collections.CollectionUtils;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.aiit.mes.common.constant.Constants.MSG_UPDATE_ID_NOT_MATCH_IN_PATH;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName CraftFlowRepresent
 * @Description
 * @createTime 2021.09.07 16:54
 */
@Data
@ApiModel(value = "更新流程图请求体")
public class CraftFlowGraphUpdateCmd implements IToEntity {

    @ApiModelProperty(value = "步骤节点列表")
    List<CraftNodeCreateCmd> nodes;

    @NotNull
    @Min(1)
    @ApiModelProperty(value = "流程id")
    private Long id;

    @ApiModelProperty(value = "节点关系列表")
    private List<FlowNodeRelationV> relations;

    public void compareId(Long id) {
        if (Objects.isNull(this.id)) {
            this.id = id;
            return;
        }
        if (!id.equals(this.id)) {
            throw new ParamInvalidException(MSG_UPDATE_ID_NOT_MATCH_IN_PATH);
        }
    }

    @Override
    public CraftFlowEntity toEntity() {
        return null;
    }

    public List<CraftFlowNodeEntity> extractNodeEntity() {
        if (CollectionUtils.isEmpty(nodes)) {
            return Collections.emptyList();
        }
        return nodes.stream().map(CraftNodeCreateCmd::toEntity).collect(Collectors.toList());
    }

    public CraftFlowRelationEntity extractRelationEntity() {
        if (CollectionUtils.isEmpty(relations)) {
            return null;
        }
        return CraftFlowRelationEntity.builder().nodeRelations(relations).build();
    }
}
