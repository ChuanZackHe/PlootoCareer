package org.aiit.mes.factory.domain.repository;

import org.aiit.mes.factory.domain.dao.entity.FactoryResourceRelation;
import org.aiit.mes.factory.domain.dao.service.IFactoryResourceRelationService;

import javax.annotation.Resource;

/**
 * @author lzj
 * @version 1.0.0
 * @ClassName FactoryResourceRelationRepository.java
 * @Description TODO
 * @createTime 2021年09月13日 16:27:00
 */
public class FactoryResourceRelationRepository {

    @Resource
    IFactoryResourceRelationService factoryResourceRelationService;

    public Boolean createRelation(FactoryResourceRelation relation) {
        return factoryResourceRelationService.createRelation(relation);
    }


    public Boolean deleteRelation(FactoryResourceRelation relation) {
        return factoryResourceRelationService.deleteRelation(relation);
    }
}
