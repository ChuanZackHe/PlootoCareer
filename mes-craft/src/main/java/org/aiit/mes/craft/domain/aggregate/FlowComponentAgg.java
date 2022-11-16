package org.aiit.mes.craft.domain.aggregate;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import org.aiit.mes.common.iface.IToRepresent;
import lombok.Getter;
import lombok.Setter;
import org.aiit.mes.common.exception.ParamInvalidException;
import org.aiit.mes.common.iface.IAggregateCRUD;
import org.aiit.mes.common.iface.IValidate;
import org.aiit.mes.craft.domain.dao.entity.CraftComponentEntity;
import org.aiit.mes.craft.domain.dto.CraftComponentRepresent;
import org.aiit.mes.craft.domain.dto.CraftComponentUpdateCmd;
import org.aiit.mes.craft.domain.repository.FlowComponentRepo;
import org.aiit.mes.craft.domain.vo.MaterialInfoV;
import org.aiit.mes.craft.domain.vo.OperationInfoV;
import org.aiit.mes.craft.domain.vo.QcInfoV;

import java.util.Optional;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName FlowComponentAgg
 * @Description
 * @createTime 2021.09.02 09:53
 */
public class FlowComponentAgg implements IToRepresent, IAggregateCRUD {

    private final FlowComponentRepo componentRepo;

    @Getter
    private Long componentId;

    /**
     * 聚合根
     */
    @Getter
    @Setter
    private CraftComponentEntity component;

    @Getter
    private CraftComponentEntity toUpdateComponent;

    public FlowComponentAgg(CraftComponentEntity component, FlowComponentRepo componentRepo) {
        this.component = component;
        this.componentId = component.getId();
        this.componentRepo = componentRepo;
    }

    public static FlowComponentAgg fromDb(CraftComponentEntity component, FlowComponentRepo componentRepo) {
        return new FlowComponentAgg(component, componentRepo);
    }

    public static FlowComponentAgg fromEntity(CraftComponentEntity component, FlowComponentRepo componentRepo) {
        return new FlowComponentAgg(component, componentRepo);
    }

    public void validateToSaveEntity(CraftComponentEntity entity) throws ParamInvalidException {
        // todo 存储之前的数据校验;
        // todo: id有效性校验：roleId 存在、materialId存在、模板id存在、基于模板做流程校验
    }

    @Override
    public CraftComponentRepresent toRepresent() {
        return CraftComponentRepresent.fromEntity(this.component);
    }


    @Override
    public FlowComponentAgg save() {
        validateToSaveEntity(this.getComponent());
        return componentRepo.saveNewComponent(this);
    }

    @Override
    public Long delete() {
        if (componentRepo.deleteComponent(this)) {
            return this.componentId;
        }
        return -1L;
    }

    @Override
    public FlowComponentAgg update() {
        validateToSaveEntity(this.getComponent());
        return componentRepo.updateComponent(this);
    }

    /**
     * 校验要更新的数据是否完整
     *
     * @param updateCmd
     */
    public void loadUpdateCmd(CraftComponentUpdateCmd updateCmd) {
        CopyOptions copyOptions = CopyOptions.create().ignoreNullValue();
        IValidate newInfo = null;

        // 先校验子结构：相关Info
        switch (this.component.getType()) {
            case MATERIAL_TRANSFER:
                newInfo = new MaterialInfoV();
                BeanUtil.copyProperties(this.getComponent().getMaterialInfo(), newInfo);
                BeanUtil.copyProperties(updateCmd.getMaterialInfo(), newInfo, copyOptions);
                break;
            case OPERATE:
                newInfo = new OperationInfoV();
                BeanUtil.copyProperties(this.getComponent().getOpInfo(), newInfo);
                BeanUtil.copyProperties(updateCmd.getOpInfo(), newInfo, copyOptions);
                break;
            case QC:
                newInfo = new QcInfoV();
                BeanUtil.copyProperties(this.getComponent().getQcInfo(), newInfo);
                BeanUtil.copyProperties(updateCmd.getQcInfo(), newInfo, copyOptions);
                break;
            default:
                break;
        }
        // 校验
        Optional.ofNullable(newInfo).map(IValidate::validate);

        // 校验通过，组装待更新的entity
        CraftComponentEntity updateEntity = new CraftComponentEntity();
        BeanUtil.copyProperties(updateCmd, updateEntity, "materialInfo", "opInfo", "qcInfo");
        switch (this.component.getType()) {
            case MATERIAL_TRANSFER:
                updateEntity.setMaterialInfo((MaterialInfoV) newInfo);
                break;
            case OPERATE:
                updateEntity.setOpInfo((OperationInfoV) newInfo);
                break;
            case QC:
                updateEntity.setQcInfo((QcInfoV) newInfo);
                break;
        }
        updateEntity.syncInfo();
        this.toUpdateComponent = updateEntity;
    }
}
