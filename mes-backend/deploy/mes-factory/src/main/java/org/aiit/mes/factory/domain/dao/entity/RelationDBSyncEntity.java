package org.aiit.mes.factory.domain.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lzj
 * @version 1.0.0
 * @ClassName RelationDBSyncEntity.java
 * @Description 工厂概况-保存层级关系，processRelation解析完成的待处理关系列表封装
 * @createTime 2021年09月09日 15:50:00
 */
@Data
@AllArgsConstructor
@Builder
public class RelationDBSyncEntity {

    List<FactoryResourceRelation> insertResources;

    List<FactoryResourceRelation> deleteResources;

    public RelationDBSyncEntity() {
        insertResources = new ArrayList<>();
        deleteResources = new ArrayList<>();
    }
}
