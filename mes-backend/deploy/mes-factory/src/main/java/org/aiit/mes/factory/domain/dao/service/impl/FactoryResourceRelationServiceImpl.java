package org.aiit.mes.factory.domain.dao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.aiit.mes.common.constant.Constants;
import org.aiit.mes.common.constant.FactoryResourceTypeEnum;
import org.aiit.mes.factory.domain.dao.entity.FactoryResourceInfo;
import org.aiit.mes.factory.domain.dao.entity.FactoryResourceRelation;
import org.aiit.mes.factory.domain.dao.entity.RelationDBSyncEntity;
import org.aiit.mes.factory.domain.dao.mapper.IFactoryResourceRelationMapper;
import org.aiit.mes.factory.domain.dao.service.IFactoryResourceRelationService;
import org.aiit.mes.factory.domain.dao.service.IFactoryResourceService;
import org.aiit.mes.factory.domain.vo.FactoryRelationPair;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author lzj
 * @version 1.0.0
 * @ClassName FactoryResourceRelationServiceImpl.java
 * @Description 工厂概况-关系Service实现类
 * @createTime 2021年09月08日 15:02:00
 */
@Service
public class FactoryResourceRelationServiceImpl
        extends ServiceImpl<IFactoryResourceRelationMapper, FactoryResourceRelation>
        implements IFactoryResourceRelationService {

    @Autowired
    private IFactoryResourceService resourceService;

    @Resource
    private IFactoryResourceRelationMapper relationMapper;

    @Override
    public List<FactoryResourceRelation> getResourceRelations(List<FactoryResourceInfo> resources,
                                                              String tenantId) {
        if (Objects.isNull(resources) || resources.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> codes = resources.stream()
                                      .map(FactoryResourceInfo::getCode)
                                      .collect(Collectors.toList());
        return this.list(new QueryWrapper<FactoryResourceRelation>().in("pre_code", codes)
                                                                    .in("after_code", codes)
                                                                    .eq(Constants.TENANT_ID, tenantId));
    }

    //按照[增，删]的顺序返回要操作的列表
    @Override
    public Boolean processRelations(List<FactoryResourceRelation> frontRelations,
                                    String tenantId, String userName,
                                    String parentCode) {

        RelationDBSyncEntity dbSyncEntity = new RelationDBSyncEntity();
        List<FactoryResourceRelation> insertRelations = dbSyncEntity.getInsertResources();
        List<FactoryResourceRelation> deleteRelations = dbSyncEntity.getDeleteResources();
        Boolean ret = true;

        int parentType = resourceService.getParentType(parentCode);
        if (parentType != FactoryResourceTypeEnum.WORKSHOP.getType()) {
            if (!frontRelations.isEmpty()) {
                throw new IllegalArgumentException("关系定义非法：父资源不为" + FactoryResourceTypeEnum.WORKSHOP.getDesc() +
                                                           "类型时定义先后关系");
            }
        }

        List<FactoryResourceRelation> dataBaseRelations =
                relationMapper.selectList(new QueryWrapper<FactoryResourceRelation>().eq("tenant_id", tenantId));
        // 如果前台传过来的为空，那就把数据库的删掉
        if (CollectionUtils.isEmpty(frontRelations)) {
            dbSyncEntity.setDeleteResources(dataBaseRelations);
            return true;
        }

        Map<FactoryRelationPair, FactoryResourceRelation> frontRelationMap =
                frontRelations.stream()
                              .collect(Collectors.toMap(FactoryResourceRelation::toMapKey, Function.identity()));

        Map<FactoryRelationPair, FactoryResourceRelation> dataBaseRelationMap = dataBaseRelations.stream()
                                                                                                 .collect(
                                                                                                         Collectors.toMap(
                                                                                                                 FactoryResourceRelation::toMapKey,
                                                                                                                 Function.identity()));
        //从前台开始遍历，前台一定有
        for (Map.Entry<FactoryRelationPair, FactoryResourceRelation> e : frontRelationMap.entrySet()) {
            FactoryRelationPair curPair = e.getKey();
            FactoryResourceRelation curRelation = e.getValue();

            //后台没有，且前台的没有id,则插入
            if (!dataBaseRelationMap.containsKey(curPair) && (Objects.isNull(curRelation.getId()) ||
                    curRelation.getId() == 0)) {
                if (curRelation.getCreateUsername() == null || curRelation.getUpdateUsername() == null) {
                    curRelation.setCreateUsername(userName);
                    curRelation.setUpdateUsername(userName);
                }
                insertRelations.add(curRelation);
            }
        }
        //再通过后台遍历一遍，如果后台有，前台没有，则删除
        for (Map.Entry<FactoryRelationPair, FactoryResourceRelation> e : dataBaseRelationMap.entrySet()) {
            FactoryRelationPair curPair = e.getKey();
            FactoryResourceRelation curRelation = e.getValue();

            //如果后台有，前台没有，则删除
            if (!frontRelationMap.containsKey(curPair)) {
                deleteRelations.add(curRelation);
            }
        }

        if (!insertRelations.isEmpty()) {
            ret = relationMapper.insertRelationsBatch(insertRelations) > 0;
        }

        if (!deleteRelations.isEmpty()) {
            ret &= relationMapper.delete(new QueryWrapper<FactoryResourceRelation>().in("id",
                                                                                        deleteRelations.stream()
                                                                                                       .map(FactoryResourceRelation::getId)
                                                                                                       .collect(
                                                                                                               Collectors.toList()))) ==
                    deleteRelations.size();
        }

        return ret;
    }

    private void deleteAllChildRelations(String tenantId, String parentCode) {
        relationMapper.delete(new QueryWrapper<FactoryResourceRelation>().eq("tenant_id", tenantId)
                                                                         .eq("parent_code", parentCode));
    }

    @Override
    public Boolean createRelation(FactoryResourceRelation relation) {
        return relation.insert();
    }

    @Override
    public Boolean deleteRelation(FactoryResourceRelation relation) {
        return relation.deleteById();
    }

}
