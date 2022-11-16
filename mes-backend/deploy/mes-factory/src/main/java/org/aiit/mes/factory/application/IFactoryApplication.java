package org.aiit.mes.factory.application;

import org.aiit.mes.factory.domain.dto.FactoryPlotAggDto;
import org.aiit.mes.factory.domain.dto.FactoryResourceStatueChangeCmd;

/**
 * @author lzj
 * @version 1.0.0
 * @ClassName IFactoryApplication.java
 * @Description 工厂概况Application
 * @createTime 2021年09月06日 17:30:00
 */
public interface IFactoryApplication {

    /**
     * 保存层级概况
     */
    Boolean savePlot(FactoryPlotAggDto aggDto, String tenantId, String userName);

    /**
     * 获取对应code表示资源的层级概况
     */
    FactoryPlotAggDto showPlot(String code, String tenantId);

    Boolean updateResourceStatus(FactoryResourceStatueChangeCmd statueChangeCmd);
}
