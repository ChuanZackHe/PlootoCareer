package org.aiit.mes.craft.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.aiit.mes.base.material.domain.entity.BaseMaterialEntity;
import org.aiit.mes.craft.domain.crafttree.CraftFlowTreeMap;
import org.aiit.mes.craft.domain.crafttree.CraftFlowTreeNode;
import org.aiit.mes.warehouse.stock.domain.dao.entity.StockSummaryEntity;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName CraftBomTreeRepresent
 * @Description
 * @createTime 2021.12.24 16:11
 */

@Data
@ApiModel(value = "需求分析树展示对象")
public class CraftBomTreeRepresentV {

    @ApiModelProperty(value = "所需原材料(input)库存是否全部满足制造需要的数量", example = "true")
    @JsonProperty(value = "is_stock_satisfied")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isStockSatisfied;

    @ApiModelProperty(value = "需要的目标物料信息")
    private CraftBomTreeNodeRepresentV target;

    @ApiModelProperty(value = "生产依赖的物料输入清单", example = "[]")
    private List<CraftBomTreeNodeRepresentV> inputs;

    @ApiModelProperty(value = "生产出的物料输出清单", example = "[]")
    private List<CraftBomTreeNodeRepresentV> outputs;

    public CraftBomTreeRepresentV(CraftBomTreeNodeRepresentV target,
                                  List<CraftBomTreeNodeRepresentV> inputs,
                                  List<CraftBomTreeNodeRepresentV> outputs) {
        this.target = target;
        this.inputs = inputs;
        this.outputs = outputs;
        if (CollectionUtils.isEmpty(outputs)) {
            this.isStockSatisfied = false;
            return;
        }

        this.isStockSatisfied =
                inputs.stream().map(CraftBomTreeNodeRepresentV::getIsSatisfied)
                      .filter(BooleanUtils::isNotTrue).count() == 0;
    }

    public static CraftBomTreeRepresentV convertFrom(CraftFlowTreeMap treeMap,
                                                     List<BaseMaterialEntity> materialList,
                                                     List<StockSummaryEntity> stockList,
                                                     Boolean compareStock) {
        // 物料id-物料信息map
        HashMap<Long, BaseMaterialEntity> materialMap = new HashMap<>();
        materialList.stream().forEach(m -> materialMap.put(m.getId(), m));

        // 物料id-库存数量map
        HashMap<Long, Double> stockMap =
                (HashMap<Long, Double>) stockList.stream().collect(Collectors.toMap(StockSummaryEntity::getMaterialId,
                                                                                    StockSummaryEntity::getCount));
        // 转化后的物料-id： 展示node map
        HashMap<Long, CraftBomTreeNodeRepresentV> inputBomNodeMap = new HashMap<>();
        HashMap<Long, CraftBomTreeNodeRepresentV> outputBomNodeMap = new HashMap<>();
        ArrayList<CraftBomTreeNodeRepresentV> inputs = new ArrayList<>();
        ArrayList<CraftBomTreeNodeRepresentV> outputs = new ArrayList<>();

        // bom的输入，需要和库存做比较
        // 根据query的开关 compareStock做控制，true=对比真实库存，false=不对比库存
        treeMap.getAllInputNodeList().stream().forEach(
                flowTreeNode -> convertNode(flowTreeNode, inputBomNodeMap,
                                            materialMap.get(flowTreeNode.getMaterialId().getId()),
                                            BooleanUtils.isTrue(compareStock) ? Optional.ofNullable(
                                                                                                stockMap.get(flowTreeNode.getMaterialId().getId()))
                                                                                        .orElse(0.0) :
                                                    flowTreeNode.getData().getMaterialNum(),
                                            inputs));

        // bom的产出，无需和库存做比较
        treeMap.getAllOutputNodeList().stream().forEach(
                flowTreeNode -> convertNode(flowTreeNode, outputBomNodeMap,
                                            materialMap.get(flowTreeNode.getMaterialId().getId()),
                                            null,
                                            outputs));

        // 得到目标生产物料的node
        CraftBomTreeNodeRepresentV targetNode = outputBomNodeMap.get(treeMap.getEntryNode().getMaterialId().getId());

        return new CraftBomTreeRepresentV(targetNode, inputs, outputs);
    }

    public static void convertNode(CraftFlowTreeNode flowTreeNode,
                                   HashMap<Long, CraftBomTreeNodeRepresentV> bomNodeMap,
                                   BaseMaterialEntity materialEntity,
                                   Double stockNum,
                                   List<CraftBomTreeNodeRepresentV> targetList) {
        //  如果物料id对应的node已存在，直接累加需要的数量
        CraftBomTreeNodeRepresentV representV = bomNodeMap.get(flowTreeNode.getMaterialId().getId());
        if (Objects.nonNull(representV)) {
            representV.addRequiredNum(flowTreeNode.getData().getMaterialNum());
            return;
        }

        // 如果物料id对应的node不存在，初始化为node并添加到list和map（作为下次索引
        representV = CraftBomTreeNodeRepresentV.convertFrom(materialEntity, stockNum,
                                                            flowTreeNode.getData().getMaterialNum());
        targetList.add(representV);
        bomNodeMap.put(representV.getId(), representV);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CraftBomTreeRepresentV{");
        sb.append("isStockSatisfied=").append(isStockSatisfied);
        sb.append(", target=").append(target);
        sb.append(", inputs=").append(inputs);
        sb.append(", outputs=").append(outputs);
        sb.append('}');
        return sb.toString();
    }
}
