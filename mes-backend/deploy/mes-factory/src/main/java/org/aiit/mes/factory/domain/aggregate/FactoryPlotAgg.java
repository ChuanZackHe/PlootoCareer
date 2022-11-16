package org.aiit.mes.factory.domain.aggregate;

import org.aiit.mes.common.iface.IToRepresent;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aiit.mes.factory.domain.dao.entity.FactoryResourceGroupRelation;
import org.aiit.mes.factory.domain.dao.entity.FactoryResourceInfo;
import org.aiit.mes.factory.domain.dao.entity.FactoryResourceRelation;
import org.aiit.mes.factory.domain.dto.FactoryPlotAggDto;
import org.aiit.mes.factory.domain.dto.FactoryResourceGroupRelationDto;
import org.aiit.mes.factory.domain.dto.ResourceDetailDto;
import org.aiit.mes.factory.domain.dto.ResourceRelationDto;
import org.aiit.mes.factory.domain.repository.FactoryPlotRepository;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author lzj
 * @version 1.0.0
 * @ClassName ResourceAgg.java
 * @Description PlotEntity定义
 * @createTime 2021年09月06日 15:43:00
 */
@Data
@NoArgsConstructor
public class FactoryPlotAgg implements IToRepresent {

    //该图对应的资源代码
    private String code;

    //该图对应的资源集合
    private List<FactoryResourceInfo> childResources;

    //该图对应的边集合
    private List<FactoryResourceRelation> relations;

    private List<FactoryResourceGroupRelation> facResourceGroupRelations;

    @Resource
    private FactoryPlotRepository factoryPlotRepository;

    public static FactoryPlotAgg fromDto(FactoryPlotAggDto aggDto) {
        FactoryPlotAgg agg = new FactoryPlotAgg();

        agg.setCode(aggDto.getCode());
        agg.setChildResources(aggDto.getFrontResourceInfos()
                                    .stream()
                                    .map(ResourceDetailDto::toEntity)
                                    .collect(
                                            Collectors.toList()));
        agg.setRelations(aggDto.getFrontRelations()
                               .stream()
                               .map(ResourceRelationDto::toEntity)
                               .collect(Collectors.toList()));
        agg.setFacResourceGroupRelations(Optional.ofNullable(aggDto.getFacResourceGroupRelations()).orElse(
                Collections.emptyList()).stream().map(
                FactoryResourceGroupRelationDto::toEntity).collect(Collectors.toList()));

        return agg;
    }

    @Override
    public FactoryPlotAggDto toRepresent() {
        return FactoryPlotAggDto.fromAgg(this);
    }

    public FactoryPlotAggDto showPlot(String resourceCode, String tenantId) {
        return factoryPlotRepository.showPlot(resourceCode, tenantId);
    }

    public Boolean savePlot(FactoryPlotAggDto aggDto, String tenantId, String userName) {
        return factoryPlotRepository.savePlot(aggDto.toEntity(), tenantId, userName);
    }
}
