package org.aiit.mes.factory.domain.dao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.aiit.mes.base.resourcegroup.domain.dao.service.IResourceGroupInfoService;
import org.aiit.mes.base.resourcegroup.domain.entity.ResourceGroupEntity;
import org.aiit.mes.common.exception.OperationFailedException;
import org.aiit.mes.common.exception.ParamInvalidException;
import org.aiit.mes.common.exception.ResourceNotFoundException;
import org.aiit.mes.factory.domain.dao.entity.FactoryResourceGroupRelation;
import org.aiit.mes.factory.domain.dao.entity.FactoryResourceInfo;
import org.aiit.mes.factory.domain.dao.entity.FactoryResourceRelation;
import org.aiit.mes.factory.domain.dao.mapper.IFactoryResourceGroupRelationMapper;
import org.aiit.mes.factory.domain.dao.service.IFactoryResourceGroupRelationService;
import org.aiit.mes.factory.domain.dao.service.IFactoryResourceService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.aiit.mes.common.constant.Constants.*;
import static org.aiit.mes.common.constant.FactoryResourceTypeEnum.MACHINE;
import static org.aiit.mes.common.constant.FactoryResourceTypeEnum.STATION;

/**
 * @author 张少卿
 * @Description 工厂资源和资源组关系服务
 * @createTime 2021年09月16日 11:16:00
 */
@Service
public class FactoryResourceGroupRelationServiceImpl
        extends ServiceImpl<IFactoryResourceGroupRelationMapper, FactoryResourceGroupRelation> implements
        IFactoryResourceGroupRelationService {

    @Autowired
    IFactoryResourceService factoryResourceService;

    @Autowired
    IResourceGroupInfoService groupInfoService;

    @Override
    public List<FactoryResourceGroupRelation> listResourceGroupRelations(List<FactoryResourceInfo> childResources) {
        if (CollectionUtils.isEmpty(childResources)) {
            return new ArrayList<>();
        }
        List<String> codes =
                childResources.stream().map(FactoryResourceInfo::getCode).distinct().collect(Collectors.toList());
        return this.list(new QueryWrapper<FactoryResourceGroupRelation>().in("code", codes));
    }

    @Override
    public Boolean processResourceGroupRelations(List<FactoryResourceGroupRelation> frontRelations) {
        if(!Objects.equals((int) frontRelations.stream().map(FactoryResourceGroupRelation::getCode).distinct().count(), frontRelations.size())){
            throw new ParamInvalidException("工厂资源的code不能相同");
        }
        //先把库里的对应的租户的数据拿出来
        List<FactoryResourceGroupRelation> dataBaseRelations = this.list();
        List<FactoryResourceGroupRelation> toDeleteRelations = null;
        List<FactoryResourceGroupRelation> toInsertRelations = new ArrayList<>();
        //校验工厂信息的存在性 和 只有 工位和机器属于某个资源组
        checkFacInfoExsit(frontRelations);
        //校验工具组信息的存在性
        checkToolGroupExsit(frontRelations);
        if (CollectionUtils.isEmpty(frontRelations)) {
            toDeleteRelations = dataBaseRelations;
        }
        else {
            Map<String, FactoryResourceGroupRelation> dbResourceGroupRelationMap = dataBaseRelations.stream().collect(
                    Collectors.toMap(FactoryResourceGroupRelation::generateKey,
                                     Function.identity()));
            for (FactoryResourceGroupRelation frontRelation : frontRelations) {
                String key = frontRelation.generateKey();
                if (!dbResourceGroupRelationMap.containsKey(key)) {
                    //不包含，则对数据库进行插入
                    toInsertRelations.add(frontRelation);
                }
                else {
                    //包含，则不处理，从DB map中清除，剩下的就是需要删的数据库记录
                    dbResourceGroupRelationMap.remove(key);
                }
            }
            toDeleteRelations = new ArrayList<>(dbResourceGroupRelationMap.values());
        }
        if (CollectionUtils.isNotEmpty(toDeleteRelations)) {
            if (!removeByIds(
                    toDeleteRelations.stream().map(FactoryResourceGroupRelation::getId).collect(Collectors.toList()))) {
                throw OperationFailedException.deleteFailed(FAC_RESOURCE_GROUP_RELATION, DELETE_FAIL);
            }
        }
        if (CollectionUtils.isNotEmpty(toInsertRelations)) {
            if (!saveBatch(toInsertRelations)) {
                throw OperationFailedException.insertFailed(FAC_RESOURCE_GROUP_RELATION, INSERT_FAIL);
            }
        }
        return true;
    }

    @Override
    public List<FactoryResourceGroupRelation> listByGroupName(String name) {
        LambdaQueryWrapper<FactoryResourceGroupRelation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FactoryResourceGroupRelation::getResourceGroupName, name);
        return this.list(queryWrapper);
    }

    private void checkFacInfoExsit(List<FactoryResourceGroupRelation> frontRelations) {
        if (CollectionUtils.isEmpty(frontRelations)) {
            return;
        }
        List<FactoryResourceInfo> dbFactoryInfos = factoryResourceService.list();
        Map<String, Integer> facIdTypeMap =
                dbFactoryInfos.stream().collect(Collectors.toMap(FactoryResourceInfo::getCode,
                                                                 FactoryResourceInfo::getType));
        frontRelations.forEach(t -> {
            //校验数据库是否存在这个工厂资源
            if (!facIdTypeMap.containsKey(t.getCode())) {
                throw ResourceNotFoundException.newExp(FAC_RESOURCE_INFO, t.getCode());
            }
            //校验工厂资源是否能被绑定
            if (!Objects.equals(facIdTypeMap.get(t.getCode()), STATION.getType())
                    && !Objects.equals(facIdTypeMap.get(t.getCode()), MACHINE.getType())) {
                throw new ParamInvalidException("绑定的工厂资源不属于工位或者机器");
            }
        });
    }

    private void checkToolGroupExsit(List<FactoryResourceGroupRelation> frontRelations) {
        if (CollectionUtils.isEmpty(frontRelations)) {
            return;
        }
        Set<Long> resourceGroups = groupInfoService.list().stream().map(ResourceGroupEntity::getId).collect(
                Collectors.toSet());
        frontRelations.forEach(t -> {
            if (!resourceGroups.contains(t.getResourceGroupId())) {
                throw ResourceNotFoundException.newExp(FAC_RESOURCE_INFO, t);
            }
        });
    }
}