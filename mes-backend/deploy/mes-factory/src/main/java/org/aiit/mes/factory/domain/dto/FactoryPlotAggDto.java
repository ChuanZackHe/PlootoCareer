package org.aiit.mes.factory.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aiit.mes.common.iface.IToEntity;
import org.aiit.mes.factory.domain.aggregate.FactoryPlotAgg;
import org.aiit.mes.factory.domain.dao.entity.FactoryResourceInfo;
import org.aiit.mes.factory.domain.dao.entity.FactoryResourceRelation;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lzj
 * @version 1.0.0
 * @ClassName newDtp.java
 * @Description 前端传过来的当前层级概况包装dto
 * @createTime 2021年09月02日 17:56:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FactoryPlotAggDto implements IToEntity {

    @NotBlank
    @ApiModelProperty("资源code")
    private String code;

    @NotBlank
    @ApiModelProperty("当前层级资源列表")
    @JsonProperty("front_resource_infos")
    private List<ResourceDetailDto> frontResourceInfos;

    @ApiModelProperty("当前层级关系列表")
    @JsonProperty("front_relations")
    private List<ResourceRelationDto> frontRelations;

    @ApiModelProperty("工厂资源和绑定关系")
    @JsonProperty("fac_group_relations")
    private List<FactoryResourceGroupRelationDto> facResourceGroupRelations;

    public static FactoryPlotAggDto fromAgg(FactoryPlotAgg factoryPlotAgg) {
        FactoryPlotAggDto plotDto = new FactoryPlotAggDto();

        plotDto.code = factoryPlotAgg.getCode();

        plotDto.frontResourceInfos =
                factoryPlotAgg.getChildResources()
                              .stream()
                              .map(FactoryResourceInfo::toDetail)
                              .collect(Collectors.toList());
        plotDto.frontRelations =
                factoryPlotAgg.getRelations()
                              .stream()
                              .map(FactoryResourceRelation::toRepresent)
                              .collect(Collectors.toList());

//        plotDto.facResourceGroupRelations = factoryPlotAgg.get
        return plotDto;
    }

    @Override
    public FactoryPlotAgg toEntity() {
        return FactoryPlotAgg.fromDto(this);
    }
}
