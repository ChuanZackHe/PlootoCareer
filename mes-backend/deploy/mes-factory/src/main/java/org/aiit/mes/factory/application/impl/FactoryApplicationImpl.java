package org.aiit.mes.factory.application.impl;

import org.aiit.mes.factory.application.IFactoryApplication;
import org.aiit.mes.factory.domain.dto.FactoryPlotAggDto;
import org.aiit.mes.factory.domain.dto.FactoryResourceStatueChangeCmd;
import org.aiit.mes.factory.domain.repository.FactoryPlotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author lzj
 * @version 1.0.0
 * @ClassName FactoryApplicationImpl.java
 * @Description plot整体的Application
 * @createTime 2021年09月08日 13:17:00
 */
@Service
public class FactoryApplicationImpl implements IFactoryApplication {

    private static final Logger logger = LoggerFactory.getLogger(FactoryApplicationImpl.class);

    @Resource
    private FactoryPlotRepository factoryPlotRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean savePlot(FactoryPlotAggDto aggDto, String tenantId, String userName) {
        return factoryPlotRepository.savePlot(aggDto.toEntity(), tenantId, userName);
    }

    @Override
    public FactoryPlotAggDto showPlot(String code, String tenantId) {
        return factoryPlotRepository.showPlot(code, tenantId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateResourceStatus(FactoryResourceStatueChangeCmd statusChangeCmd) {
        logger.info("update resource status, update cmd:{}", statusChangeCmd);
        return factoryPlotRepository.updateResourceStatus(statusChangeCmd);
    }
}
