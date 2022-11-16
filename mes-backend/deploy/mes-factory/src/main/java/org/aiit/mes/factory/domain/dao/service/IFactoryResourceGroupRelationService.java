package org.aiit.mes.factory.domain.dao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.aiit.mes.factory.domain.dao.entity.FactoryResourceGroupRelation;
import org.aiit.mes.factory.domain.dao.entity.FactoryResourceInfo;
import org.aiit.mes.factory.domain.dao.entity.FactoryResourceRelation;

import java.util.List;

/**
 * @author 张少卿
 * @version 1.0.0
 * @ClassName IFactoryResourceGroupRelationService.java
 * @Description 工厂资源和资源组的服务接口
 * @createTime 2021年09月16日 11:16:00
 */
public interface IFactoryResourceGroupRelationService extends IService<FactoryResourceGroupRelation> {

    /**
     * 根据工厂资源列表，获取工厂资源和资源组绑定信息
     *
     * @return
     */
    List<FactoryResourceGroupRelation> listResourceGroupRelations(List<FactoryResourceInfo> childs);

    /**
     * 对绑定的资源进行处理，解绑或者新增
     *
     * @param frontRelations
     * @return
     */
    Boolean processResourceGroupRelations(List<FactoryResourceGroupRelation> frontRelations);

    List<FactoryResourceGroupRelation> listByGroupName(String name);

}