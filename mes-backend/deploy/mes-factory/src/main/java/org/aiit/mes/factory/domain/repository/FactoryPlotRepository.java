package org.aiit.mes.factory.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.aiit.mes.common.exception.ResourceNotFoundException;
import org.aiit.mes.factory.domain.aggregate.FactoryPlotAgg;
import org.aiit.mes.factory.domain.aggregate.factory.FactoryPlotAggFactory;
import org.aiit.mes.factory.domain.dao.entity.FactoryResourceGroupRelation;
import org.aiit.mes.factory.domain.dao.entity.FactoryResourceInfo;
import org.aiit.mes.factory.domain.dao.entity.FactoryResourceRelation;
import org.aiit.mes.factory.domain.dao.service.IFactoryResourceGroupRelationService;
import org.aiit.mes.factory.domain.dao.service.IFactoryResourcePlotService;
import org.aiit.mes.factory.domain.dao.service.IFactoryResourceRelationService;
import org.aiit.mes.factory.domain.dao.service.IFactoryResourceService;
import org.aiit.mes.factory.domain.dto.FactoryPlotAggDto;
import org.aiit.mes.factory.domain.dto.FactoryResourceStatueChangeCmd;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author lzj
 * @version 1.0.0
 * @ClassName PlotRepository.java
 * @Description 工厂概况-repository
 * @createTime 2021年09月08日 14:23:00
 */
@Repository
public class FactoryPlotRepository implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(FactoryPlotRepository.class);

    @Autowired
    private IFactoryResourceService resourceService;

    @Autowired
    private IFactoryResourceRelationService relationService;

    @Autowired
    private IFactoryResourcePlotService plotService;

    @Autowired
    private IFactoryResourceGroupRelationService groupRelationService;


    @Override
    public void afterPropertiesSet() throws Exception {
        FactoryPlotAggFactory.init(this, plotService, resourceService, relationService);
    }


    public FactoryPlotAggDto showPlot(String resourceCode, String tenantId) {
        List<FactoryResourceInfo> childResources = resourceService.listChildResource(resourceCode, tenantId);
        return new FactoryPlotAggDto(resourceCode,
                                     childResources.stream().map(FactoryResourceInfo::toDetail)
                                                   .collect(Collectors.toList()),
                                     relationService.getResourceRelations(childResources, tenantId).stream().map(
                                             FactoryResourceRelation::toRepresent).collect(Collectors.toList()),
                                     groupRelationService.listResourceGroupRelations(childResources).stream().map(
                                             FactoryResourceGroupRelation::toRepresent).collect(Collectors.toList()));
    }

    public FactoryResourceInfo showResource(String resourceCode) {
        return Optional.ofNullable(resourceService.getOne(
                               new LambdaQueryWrapper<FactoryResourceInfo>().eq(FactoryResourceInfo::getCode,
                                                                                resourceCode)))
                       .orElseThrow(() -> ResourceNotFoundException.newExp("工厂资源", resourceCode));
    }

    public Boolean updateResource(FactoryResourceInfo resourceInfo) {
        if (Objects.isNull(resourceInfo)) {
            return true;
        }
        logger.info("update resource:{}", resourceInfo);
        return resourceService.updateById(resourceInfo);
    }

    public Boolean updateResourceStatus(FactoryResourceStatueChangeCmd cmd) {
        if (StringUtils.isBlank(cmd.getResourceCode())) {
            logger.info("resource code is:{}, skip change", cmd.getResourceCode());
            return true;
        }
        FactoryResourceInfo resource = showResource(cmd.getResourceCode());
        if (Objects.equals(cmd.getTargetStatus(), resource.getState())) {
            logger.info("resource state is:{}, skip change", resource.getState());
            return true;
        }
        resource.setState(cmd.getTargetStatus());
        logger.info("set resource[{}] to state[{}]", resource.getId(), cmd.getTargetStatus());
        return updateResource(resource);
    }


    public Boolean savePlot(FactoryPlotAgg factoryPlotAgg, String tenantId, String userName) {
        //保存工厂资源相互关系
        //保存资源组和工厂资源的绑定关系
        return plotService.savePlot(factoryPlotAgg.getChildResources(),
                                    factoryPlotAgg.getRelations(),
                                    tenantId, userName,
                                    factoryPlotAgg.getCode()) && groupRelationService.processResourceGroupRelations(
                factoryPlotAgg.getFacResourceGroupRelations());
    }
}
