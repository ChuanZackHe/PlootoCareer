package org.aiit.mes.factory.domain.dao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.aiit.mes.factory.domain.dao.entity.FactoryResourceInfo;
import org.aiit.mes.factory.domain.dao.entity.FactoryResourceRelation;

import java.util.List;

/**
 * @author lzj
 * @version 1.0.0
 * @ClassName IFactoryResourceRelationService.java
 * @Description 关系Service拆分
 * @createTime 2021年09月08日 15:02:00
 */
public interface IFactoryResourceRelationService extends IService<FactoryResourceRelation> {

    List<FactoryResourceRelation> getResourceRelations(List<FactoryResourceInfo> resources, String tenantId);

    Boolean createRelation(FactoryResourceRelation relation);

    Boolean deleteRelation(FactoryResourceRelation relation);

    Boolean processRelations(List<FactoryResourceRelation> frontRelations,
                             String tenantId, String userName,
                             String parentCode);
}
