package org.aiit.mes.factory.domain.dao.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.aiit.mes.factory.domain.dao.entity.FactoryResourceInfo;
import org.aiit.mes.factory.domain.dto.ListResourceFilterDto;
import org.aiit.mes.factory.domain.dto.ResourceDetailDto;

import java.util.List;
import java.util.Map;

/**
 * @author lzj
 * @version 1.0.0
 * @ClassName IFactoryResourceService.java
 * @Description 工厂管理Service接口
 * @createTime 2021年08月23日 15:21:00
 */
public interface IFactoryResourceService extends IService<FactoryResourceInfo> {

    /**
     * 根据查询条件列出资源列表
     */
    Page<FactoryResourceInfo> listResourceByTenantId(String tenantId, ListResourceFilterDto queryCondition);

    /**
     * 列出指定资源的下一级资源(分页版)
     */
    Page<FactoryResourceInfo> listChildResourcePages(Map<String, Object> params, int pageNo, int pageSize);

    /**
     * 列出指定资源的下一级资源(不分页，用于内部API，不暴露到外层接口)
     */
    List<FactoryResourceInfo> listChildResource(String parentCode, String tenantId);

    /**
     * 列出资源具体信息
     */
    ResourceDetailDto showResourceDetail(String resourceCode);

    /**
     * 更新资源具体信息
     */
    Boolean updateResourceDetail(ResourceDetailDto resourceInfo);

    /**
     * 删除资源
     */
    Boolean deleteResourceDetail(String resourceCode);

    /**
     * 新增资源
     */
    Boolean createResource(FactoryResourceInfo resourceInfo);

    /**
     * 工厂概况-保存层级概况，解析前台传过来的数据，返回包装了增删改列表的Entity
     */
    Boolean processResources(List<FactoryResourceInfo> frontResourceInfos,
                             String tenantId, String userName,
                             String parentCode);

    public Integer getParentType(String parentCode);

    /**
     * 根据工厂资源组的名称获取资源组下所有工厂资源
     * @param name
     * @return
     */
    List<FactoryResourceInfo> listByResourceGroupName(String name);

    List<FactoryResourceInfo> listByResourceCodes(List<String> codes);
}
