package org.aiit.mes.craft.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.aiit.mes.common.constant.DataStateEnum;
import org.aiit.mes.common.util.PropertyCopyUtil;
import org.aiit.mes.craft.domain.dao.entity.CraftFlowEntity;
import org.aiit.mes.craft.domain.vo.FlowNodeRelationV;

import java.util.Date;
import java.util.List;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName CraftFlowRepresent
 * @Description
 * @createTime 2021.09.07 16:54
 */
@Data
@ApiModel(value = "工艺流程展示")
public class CraftFlowRepresent {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "流程步骤列表")
    List<CraftNodeRepresent> nodes;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "步骤间关系列表")
    List<FlowNodeRelationV> relations;

    @ApiModelProperty(value = "流程id")
    private Long id;

    @ApiModelProperty(value = "流程名称")
    private String name;

    @JsonProperty(value = "bind_resource_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ApiModelProperty(value = "流程绑定的工厂资源id")
    private Integer bindResourceId;

    @ApiModelProperty(value = "流程启用状态")
    private DataStateEnum state;

    @JsonProperty(value = "update_time")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * 更新人
     */
    @JsonProperty(value = "update_username")
    @ApiModelProperty(value = "更新人")
    private String updateUsername;

    /**
     * 新建时间
     */
    @JsonProperty(value = "create_time")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 新建人
     */
    @JsonProperty(value = "create_username")
    @ApiModelProperty(value = "创建人")
    private String createUsername;

    public static CraftFlowRepresent fromEntity(CraftFlowEntity craftFlowEntity) {
        return PropertyCopyUtil.copyToClass(craftFlowEntity, CraftFlowRepresent.class);
    }

    public CraftFlowRepresent nodes(List<CraftNodeRepresent> nodes) {
        this.nodes = nodes;
        return this;
    }

    public CraftFlowRepresent relations(List<FlowNodeRelationV> relations) {
        this.relations = relations;
        return this;
    }
}
