package org.aiit.mes.factory.domain.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lzj
 * @version 1.0.0
 * @ClassName ResourceDBSyncEntity.java
 * @Description 封装保存工厂概况的资源列表
 * @createTime 2021年09月09日 15:33:00
 */
@Data
@Builder
@AllArgsConstructor
public class ResourceDBSyncEntity {

    List<FactoryResourceInfo> insertResources;

    List<FactoryResourceInfo> deleteResources;

    List<FactoryResourceInfo> updateResources;

    public ResourceDBSyncEntity() {
        insertResources = new ArrayList<>();
        deleteResources = new ArrayList<>();
        updateResources = new ArrayList<>();
    }
}
