package org.aiit.mes.craft.domain.crafttree;

import org.aiit.mes.common.graph.Node;
import org.aiit.mes.common.primitive.UId;
import org.aiit.mes.craft.domain.dao.entity.CraftFlowNodeEntity;
import org.aiit.mes.craft.domain.primitive.MaterialId;
import org.aiit.mes.craft.domain.vo.MaterialInfoV;
import org.aiit.mes.craft.domain.vo.OperationInfoV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Optional;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName
 * @Description 工艺流程节点的node化描述
 * @createTime 2021.12.16 10:01
 */
public class CraftFlowTreeNode extends Node<CraftFlowNodeEntity> {

    private static final Logger logger = LoggerFactory.getLogger(CraftFlowTreeNode.class);


    public MaterialId getMaterialId() {
        return materialId;
    }

    public UId getCraftStepId() {
        return craftStepId;
    }

    private UId craftStepId;

    private MaterialId materialId;

    /**
     * 针对物料node特化，
     * 同步更新nodeid为物料id
     *
     * @param data
     */
    public CraftFlowTreeNode(CraftFlowNodeEntity data) {
        super(data);
        updateId(data);
    }

    /**
     * 自动提取 stepId、materialId
     *
     * @param data
     */
    private void updateId(CraftFlowNodeEntity data) {
        Optional.ofNullable(data).map(CraftFlowNodeEntity::getId).ifPresent(id -> this.craftStepId =
                new UId(id));
        Optional.ofNullable(data).map(CraftFlowNodeEntity::getMaterialId).ifPresent(id -> this.materialId =
                new MaterialId(id));
    }

    /**
     * 针对物料node特化，
     * 同步更新nodeid为物料id
     *
     * @param data
     */
    @Override
    public void setData(CraftFlowNodeEntity data) {
        super.setData(data);
        updateId(data);
    }

    public void multiply(Double x) {
        Optional.ofNullable(this.getData()).ifPresent(entity -> entity.multiply(x));
    }

    /**
     * 计算本节点相对目标节点的方法倍数：
     * 分子或分母任意一个为null 或 0.0，则返回 1.0
     *
     * @param node
     * @return this.mat.num / node.mat.num
     */
    public Double calculateMultiple(CraftFlowTreeNode node) {
        Double numerator = Optional.ofNullable(this.getData()).map(CraftFlowNodeEntity::getMaterialInfo)
                                   .map(MaterialInfoV::getNum)
                                   .orElse(0.0);
        Double denominator = Optional.ofNullable(node).map(CraftFlowTreeNode::getData)
                                     .map(CraftFlowNodeEntity::getMaterialInfo).map(MaterialInfoV::getNum)
                                     .orElse(0.0);
        logger.info(" n/d:{}/{}", numerator, denominator);
        if (numerator * denominator == 0.0) {
            return 1.0;
        }
        return numerator / denominator;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CraftFlowTreeNode{");
        sb.append("craftStepId=").append(craftStepId);
        Optional.ofNullable(getData()).map(CraftFlowNodeEntity::getType).ifPresent(t -> sb.append(", type=").append(t));
        Optional.ofNullable(getMaterialId()).ifPresent(i -> sb.append(", materialId=").append(i));
        Optional.ofNullable(getData()).map(CraftFlowNodeEntity::getOpInfo).map(OperationInfoV::getOpType).ifPresent(
                i -> sb.append(", opType=").append(i));
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        CraftFlowTreeNode that = (CraftFlowTreeNode) o;
        return Objects.equals(craftStepId, that.craftStepId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), craftStepId);
    }
}
