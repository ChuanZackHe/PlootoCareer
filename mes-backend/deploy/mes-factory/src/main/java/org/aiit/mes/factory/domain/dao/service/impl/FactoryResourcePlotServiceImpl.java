package org.aiit.mes.factory.domain.dao.service.impl;

import org.aiit.mes.factory.domain.dao.entity.FactoryResourceInfo;
import org.aiit.mes.factory.domain.dao.entity.FactoryResourceRelation;
import org.aiit.mes.factory.domain.dao.service.IFactoryResourcePlotService;
import org.aiit.mes.factory.domain.dao.service.IFactoryResourceRelationService;
import org.aiit.mes.factory.domain.dao.service.IFactoryResourceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author lzj
 * @version 1.0.0
 * @ClassName FactoryResourcePlotServiceImpl.java
 * @Description 工厂概况图表Service实现类
 * @createTime 2021年09月08日 16:56:00
 */
@Service
public class FactoryResourcePlotServiceImpl implements IFactoryResourcePlotService {

    @Resource
    private IFactoryResourceService resourceService;

    @Resource
    private IFactoryResourceRelationService relationService;

    @Override
    public Boolean savePlot(List<FactoryResourceInfo> frontResourceInfos,
                            List<FactoryResourceRelation> frontRelations,
                            String tenantId, String userName, String parentCode) {

        Boolean resourceProcessResult = resourceService.processResources(frontResourceInfos,
                                                                         tenantId, userName,
                                                                         parentCode);

        if (Objects.nonNull(frontRelations)) {
            Boolean relationProcessResult = relationService.processRelations(frontRelations,
                                                                             tenantId,
                                                                             userName,
                                                                             parentCode);
            return resourceProcessResult & relationProcessResult;

        }
        return resourceProcessResult;
    }
}
