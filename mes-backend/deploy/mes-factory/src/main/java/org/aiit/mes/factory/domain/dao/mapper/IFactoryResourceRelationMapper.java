package org.aiit.mes.factory.domain.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.aiit.mes.factory.domain.dao.entity.FactoryResourceRelation;

import java.util.List;

/**
 * @author lzj
 * @version 1.0.0
 * @ClassName IFactoryResourceRelationMapper.java
 * @Description 关系Mapper
 * @createTime 2021年08月31日 13:52:00
 */
public interface IFactoryResourceRelationMapper extends BaseMapper<FactoryResourceRelation> {

    Integer insertRelationsBatch(List<FactoryResourceRelation> insertList);
}
