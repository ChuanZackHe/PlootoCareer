package org.aiit.mes.factory.domain.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.aiit.mes.factory.domain.dao.entity.FactoryResourceInfo;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName IFactoryResourceMapper
 * @Description 工厂资源操作mapper
 * @createTime 2021.08.10 14:40
 */
public interface IFactoryResourceMapper extends BaseMapper<FactoryResourceInfo> {

    boolean adminDeleteDeleted(Map<String, Object> params);

    List<FactoryResourceInfo> adminGetToDelete(Map<String, Object> params);

    @MapKey("code")
    Map<String, FactoryResourceInfo> getChildResourceListMap(String parentCode, String tenantId);

    Integer insertResourcesBatch(List<FactoryResourceInfo> insertList);

    Integer updateResourcesBatch(List<FactoryResourceInfo> updateList);

}
