package org.aiit.mes.factory.domain.dao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.aiit.mes.base.resourcegroup.domain.dao.service.IResourceGroupInfoService;
import org.aiit.mes.base.resourcegroup.domain.entity.ResourceGroupEntity;
import org.aiit.mes.common.constant.Constants;
import org.aiit.mes.common.constant.FactoryResourceTypeEnum;
import org.aiit.mes.common.exception.HasChildException;
import org.aiit.mes.common.exception.ParamInvalidException;
import org.aiit.mes.common.exception.ResourceNotFoundException;
import org.aiit.mes.common.util.MapObjectUtil;
import org.aiit.mes.common.util.ObjectCompareUtil;
import org.aiit.mes.factory.domain.dao.entity.FactoryResourceGroupRelation;
import org.aiit.mes.factory.domain.dao.entity.FactoryResourceInfo;
import org.aiit.mes.factory.domain.dao.entity.FactoryResourceRelation;
import org.aiit.mes.factory.domain.dao.entity.ResourceDBSyncEntity;
import org.aiit.mes.factory.domain.dao.mapper.IFactoryResourceMapper;
import org.aiit.mes.factory.domain.dao.service.IFactoryResourceGroupRelationService;
import org.aiit.mes.factory.domain.dao.service.IFactoryResourceService;
import org.aiit.mes.factory.domain.dto.ListResourceFilterDto;
import org.aiit.mes.factory.domain.dto.ResourceDetailDto;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName FactoryResourceService
 * @Description
 * @createTime 2021.08.10 14:36
 */
@Service
public class FactoryResourceServiceImpl extends ServiceImpl<IFactoryResourceMapper, FactoryResourceInfo>
        implements IFactoryResourceService {

    @Resource
    private IFactoryResourceMapper factoryResourceMapper;

    @Resource
    private IFactoryResourceGroupRelationService groupRelationService;

    @Override
    public Page<FactoryResourceInfo> listResourceByTenantId(String tenantId, ListResourceFilterDto queryCondition) {
        QueryWrapper<FactoryResourceInfo> queryWrapper = new QueryWrapper<>();
        Map<String, Object> params;
        if (Objects.isNull(queryCondition)) {
            params = new HashMap<>();
        }
        else {
            params = MapObjectUtil.object2Map(queryCondition);
        }
        params.put("tenant_id", tenantId);
        params.remove("pageSize");
        params.remove("pageNum");

        Page<FactoryResourceInfo> page = new Page<>(queryCondition.getPageNum(), queryCondition.getPageSize());

        Page<FactoryResourceInfo> resPage = factoryResourceMapper.selectPage(page, queryWrapper.allEq(params, false));

        return resPage;
    }

    @Override
    public Page<FactoryResourceInfo> listChildResourcePages(Map<String, Object> params, int pageNo, int pageSize) {
        FactoryResourceInfo info = new FactoryResourceInfo();
        Page<FactoryResourceInfo> page = new Page<>(pageNo, pageSize);
        QueryWrapper<FactoryResourceInfo> resourceInfoQueryWrapper = new QueryWrapper<>();

        Page<FactoryResourceInfo> childPages = info.selectPage(page, resourceInfoQueryWrapper.allEq(params, false));
        return childPages;
    }

    @Override
    public List<FactoryResourceInfo> listChildResource(String parentCode, String tenantId) {
        return this.list(new QueryWrapper<FactoryResourceInfo>().eq("parent_code", parentCode)
                                                                .eq(Constants.TENANT_ID, tenantId));
    }

    //????????????????????????
    private Boolean hasChildResource(String resourceCode) {
        QueryWrapper<FactoryResourceInfo> resourceInfoQueryWrapper = new QueryWrapper<>();
        Integer count = factoryResourceMapper.selectCount(resourceInfoQueryWrapper.eq("parent_code", resourceCode));
        return count > 0;
    }

    @Override
    public ResourceDetailDto showResourceDetail(String resourceCode) {
        FactoryResourceInfo info = new FactoryResourceInfo();
        info.setCode(resourceCode);

        FactoryResourceInfo resultInfo = info.selectOne(
                new QueryWrapper<FactoryResourceInfo>().eq("code", resourceCode));
        if (Objects.isNull(resultInfo)) {
            throw new ResourceNotFoundException("??????????????????", resourceCode);
        }

        return resultInfo.toDetail();
    }

    @Override
    public Boolean updateResourceDetail(ResourceDetailDto resourceInfo) {
        return resourceInfo.toEntity()
                           .updateById();
    }

    @Override
    public Boolean deleteResourceDetail(String resourceCode) {
        Boolean hasChild = hasChildResource(resourceCode);
        if (hasChild) {
            throw new HasChildException(resourceCode);
        }

        FactoryResourceInfo infoToDelete = new FactoryResourceInfo();
        infoToDelete.setCode(resourceCode);
        return infoToDelete.delete(new QueryWrapper<FactoryResourceInfo>().eq("code", resourceCode));
    }

    @Override
    public Boolean createResource(FactoryResourceInfo resourceInfo) {
        return resourceInfo.insert();
    }

    @Override
    public Integer getParentType(String parentCode) {
        if (Objects.equals(parentCode, "EMPTY")) {
            return FactoryResourceTypeEnum.FACTORY.getType();
        }
        return Optional.ofNullable(factoryResourceMapper.selectOne(new QueryWrapper<FactoryResourceInfo>().eq("code",
                                                                                                              parentCode)))
                       .map(FactoryResourceInfo::getType).orElseThrow(
                        () -> ResourceNotFoundException.newExp("???????????????code", parentCode));
    }

    @Override
    public List<FactoryResourceInfo> listByResourceGroupName(String name) {
        //???????????????????????????
        List<FactoryResourceGroupRelation> resourceRelations = groupRelationService.listByGroupName(name);
        if(CollectionUtils.isEmpty(resourceRelations)){
            return new ArrayList<>();
        }
        List<String> resourceCodes = resourceRelations.stream().map(FactoryResourceGroupRelation::getCode).collect(
                Collectors.toList());
        return this.listByResourceCodes(resourceCodes);
    }

    @Override
    public List<FactoryResourceInfo> listByResourceCodes(List<String> codes) {
        if(CollectionUtils.isEmpty(codes)){
            return new ArrayList<>();
        }
        LambdaQueryWrapper<FactoryResourceInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(FactoryResourceInfo::getCode, codes);
        return this.list(queryWrapper);
    }

    /**
     * ?????????????????????????????????????????????????????????
     */
    @Override
    public Boolean processResources(List<FactoryResourceInfo> frontResourceInfos,
                                    String tenantId, String userName,
                                    String parentCode) {
        ResourceDBSyncEntity dbSyncEntity = new ResourceDBSyncEntity();
        List<FactoryResourceInfo> insertResources = dbSyncEntity.getInsertResources();
        List<FactoryResourceInfo> deleteResources = dbSyncEntity.getDeleteResources();
        List<FactoryResourceInfo> updateResources = dbSyncEntity.getUpdateResources();

        boolean ret = true;
        for (FactoryResourceInfo detail : frontResourceInfos) {
            if (Objects.nonNull(detail.getParentCode())
                    && !Objects.equals(detail.getParentCode(), parentCode)) {
                throw new IllegalArgumentException("?????????????????????" + detail.getCode() + "??????????????????" + parentCode);
            }
        }

        List<FactoryResourceInfo> databaseResourceInfos = factoryResourceMapper.selectList(
                new QueryWrapper<FactoryResourceInfo>()
                        .eq("tenant_id", tenantId)
                        .eq("parent_code",
                            parentCode));

        if (frontResourceInfos.isEmpty()) {
            dbSyncEntity.setDeleteResources(databaseResourceInfos);
        }
        if(Objects.equals(frontResourceInfos.stream().map(FactoryResourceInfo::getCode).count(), frontResourceInfos.size())){
            throw new ParamInvalidException("???????????????code ???????????????");
        }

        Map<String, FactoryResourceInfo> frontResourceMap =
                frontResourceInfos.stream()
                                  .collect(Collectors.toMap(FactoryResourceInfo::getCode, Function.identity()));

        Map<String, FactoryResourceInfo> dataBaseResourceMap = databaseResourceInfos.stream()
                                                                                    .collect(Collectors.toMap(
                                                                                            FactoryResourceInfo::getCode,
                                                                                            Function.identity()));

        //???????????????????????????????????????
        for (Map.Entry<String, FactoryResourceInfo> e : frontResourceMap.entrySet()) {
            String curCode = e.getKey();
            FactoryResourceInfo curResource = e.getValue();
            //?????????????????????
            if (dataBaseResourceMap.containsKey(curCode)) {
                FactoryResourceInfo dataBaseObject = dataBaseResourceMap.get(curCode);
                // ????????????????????????????????????
                if (!ObjectCompareUtil.compareObject(curResource, dataBaseObject, Arrays.asList("id",
                                                                                                "createUsername",
                                                                                                "createTime",
                                                                                                "updateUsername",
                                                                                                "updateTime"))) {
                    curResource.setUpdateUsername(userName);
                    updateResources.add(curResource);
                }
            }
            //?????????????????????????????????id,?????????
            // hutools???BeansUtil.copyProperties??????id???null???bean???id?????????0???????????????????????????
            else if (!dataBaseResourceMap.containsKey(curCode) &&
                    (Objects.isNull(curResource.getId()) || curResource.getId() == 0)) {
                if (curResource.getCreateUsername() == null || curResource.getUpdateUsername() == null) {
                    curResource.setCreateUsername(userName);
                    curResource.setUpdateUsername(userName);
                }
                insertResources.add(curResource);
            }
        }
        //????????????????????????????????????????????????????????????????????????
        for (Map.Entry<String, FactoryResourceInfo> e : dataBaseResourceMap.entrySet()) {
            String curCode = e.getKey();
            FactoryResourceInfo curResource = e.getValue();
            if (!frontResourceMap.containsKey(curCode)) {
                deleteResources.add(curResource);
            }
        }

        if (!insertResources.isEmpty()) {
            ret &= factoryResourceMapper.insertResourcesBatch(insertResources) == insertResources.size();
        }

        if (!updateResources.isEmpty()) {
            ret &= factoryResourceMapper.updateResourcesBatch(updateResources) == updateResources.size();
        }
        if (!deleteResources.isEmpty()) {
            ret &= factoryResourceMapper.delete(new QueryWrapper<FactoryResourceInfo>().in("id",
                                                                                           deleteResources.stream()
                                                                                                          .map(FactoryResourceInfo::getId)
                                                                                                          .collect(
                                                                                                                  Collectors.toList()))) ==
                    deleteResources.size();
        }

        return ret;
    }
}
