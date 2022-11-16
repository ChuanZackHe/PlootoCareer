package org.aiit.mes.factory.domain.dao.service;

import org.aiit.mes.factory.domain.dao.entity.FactoryResourceInfo;
import org.aiit.mes.factory.domain.dao.entity.FactoryResourceRelation;

import java.util.List;

/**
 * @author lzj
 * @version 1.0.0
 * @ClassName IFactoryResourcePlotService.java
 * @Description Plot对应操作的Service
 * @createTime 2021年09月08日 16:56:00
 */
public interface IFactoryResourcePlotService {

    /**
     * 保存概况图
     */
    Boolean savePlot(List<FactoryResourceInfo> frontResourceInfos,
                     List<FactoryResourceRelation> frontRelations,
                     String tenantId, String userName, String parentCode);
}
