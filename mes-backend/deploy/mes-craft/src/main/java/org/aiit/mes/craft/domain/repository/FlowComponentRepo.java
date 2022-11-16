package org.aiit.mes.craft.domain.repository;

import org.aiit.mes.common.exception.ResourceNotFoundException;
import org.aiit.mes.craft.domain.aggregate.FlowComponentAgg;
import org.aiit.mes.craft.domain.dao.service.ICraftComponentService;
import org.aiit.mes.craft.domain.dto.CraftComponentCreateCmd;
import org.aiit.mes.system.util.ShiroUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName FlowComponentRepo
 * @Description
 * @createTime 2021.09.08 10:41
 */
@Repository
public class FlowComponentRepo {

    @Resource
    private ICraftComponentService craftComponentService;

    public FlowComponentAgg getComponent(Long id) throws ResourceNotFoundException {
        return Optional.ofNullable(craftComponentService.getById(id))
                       // 内存过滤租户 TODO： 自动过滤租户
                       .filter(o -> StringUtils.equals(o.getTenantId(), ShiroUtils.getUserTenantId()))
                       .map(component -> FlowComponentAgg.fromDb(component, this))
                       .orElseThrow(() -> ResourceNotFoundException.newExp(
                               "component", id));

    }

    public boolean deleteComponent(FlowComponentAgg componentAgg) {
        return craftComponentService.removeById(componentAgg.getComponentId());
    }

    public FlowComponentAgg createCmdToAgg(CraftComponentCreateCmd createCmd) {
        return Optional.ofNullable(createCmd)
                       .map(CraftComponentCreateCmd::toEntity)
                       .map(entity -> FlowComponentAgg.fromEntity(entity, this))
                       .get();
    }

    public FlowComponentAgg saveNewComponent(FlowComponentAgg agg) {
        if (!craftComponentService.save(agg.getComponent())) {
            throw new RuntimeException("save component failed");
        }
        return getComponent(agg.getComponent().getId());
    }

    /**
     * 将聚合中的 toUpdateComponent 持久化
     *
     * @param agg
     * @return
     */
    public FlowComponentAgg updateComponent(FlowComponentAgg agg) {
        if (!craftComponentService.updateById(agg.getToUpdateComponent())) {
            throw new RuntimeException("update component failed");
        }
        return getComponent(agg.getComponentId());
    }

}
