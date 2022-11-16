package org.aiit.mes.factory.domain.aggregate.factory;

import org.aiit.mes.factory.domain.dao.service.IFactoryResourcePlotService;
import org.aiit.mes.factory.domain.dao.service.IFactoryResourceRelationService;
import org.aiit.mes.factory.domain.dao.service.IFactoryResourceService;
import org.aiit.mes.factory.domain.repository.FactoryPlotRepository;

/**
 * @author lzj
 * @version 1.0.0
 * @ClassName PlotAggFactory.java
 * @Description plot工厂类
 * @createTime 2021年09月08日 14:41:00
 */
public class FactoryPlotAggFactory {

    private static FactoryPlotRepository plotRepo = null;

    private static IFactoryResourcePlotService plotService;

    private static IFactoryResourceService plotResourceService;

    private static IFactoryResourceRelationService plotRelationService;

    public static void init(FactoryPlotRepository repo,
                            IFactoryResourcePlotService mapService,
                            IFactoryResourceService resourceService,
                            IFactoryResourceRelationService relationService) {
        plotRepo = repo;
        plotResourceService = resourceService;
        plotRelationService = relationService;
        plotService = mapService;
    }
}
