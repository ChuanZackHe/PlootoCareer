package org.aiit.mes.craft.domain.crafttree;

import org.aiit.mes.common.constant.CraftStepTypeEnum;
import org.aiit.mes.common.exception.ParamInvalidException;
import org.aiit.mes.common.iface.IToRepresent;
import org.aiit.mes.common.primitive.UId;
import org.aiit.mes.common.util.DoubleUtil;
import org.aiit.mes.craft.domain.dao.entity.CraftFlowNodeEntity;
import org.aiit.mes.craft.domain.primitive.MaterialId;
import org.aiit.mes.craft.domain.vo.FlowNodeRelationV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static org.aiit.mes.common.constant.MaterialTransferEnum.OUTPUT_TYPE_LIST;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName CraftFlowTreeMap
 * @Description 描述工艺流程的树map
 * @createTime 2021.12.17 09:36
 */
public class CraftFlowTreeMap implements IToRepresent {

    private static final Logger logger = LoggerFactory.getLogger(CraftFlowTreeMap.class);

    public CraftFlowTreeMap(Collection<CraftFlowTreeNode> nodes) {
        if (!CollectionUtils.isEmpty(nodes)) {
            this.addNodeList(nodes);
        }
    }

    public CraftFlowTreeMap(Collection<CraftFlowTreeNode> nodes, MaterialId entryNodeMaterialId) {
        this(nodes);
        this.entryNode = nodes.stream().filter(n -> OUTPUT_TYPE_LIST.contains(n.getData().getTransferType()))
                              .filter(n -> Objects.equals(entryNodeMaterialId, n.getMaterialId())).findAny()
                              .orElse(null);
        Optional.ofNullable(entryNode).orElseThrow(() -> new ParamInvalidException("输入物料id无法匹配到对应的物料节点"));
    }

    public CraftFlowTreeNode getEntryNode() {
        return entryNode;
    }

    /**
     * 入口node，作为本map的标识，设置为目标 【物料入库】 节点
     */
    private CraftFlowTreeNode entryNode;

    public HashMap<UId, CraftFlowTreeNode> getAllStepsMap() {
        return allStepsMap;
    }

    private HashMap<UId, CraftFlowTreeNode> allStepsMap = new HashMap<>(16);

    /**
     * 产出物料（入库）, type = STOCK_IN
     */
    private final HashMap<UId, CraftFlowTreeNode> outputMap = new HashMap<>(8);

    /**
     * 输入物料（出库）, type = STOCK_OUT
     */
    private final HashMap<UId, CraftFlowTreeNode> inputMap = new HashMap<>(8);

    /**
     * 临时输入物料（不经过出库）, type = ON_THE_SCENE_IN 线上流转
     */
    private final HashMap<UId, CraftFlowTreeNode> tempInputMap = new HashMap<>(8);

    /**
     * 临时输出物料（不经过入库）, type = ON_THE_SCENE 线上流转
     */
    private final HashMap<UId, CraftFlowTreeNode> tempOutputMap = new HashMap<>(8);


    public HashMap<UId, CraftFlowTreeNode> getOutputMap() {
        return outputMap;
    }

    public HashMap<UId, CraftFlowTreeNode> getInputMap() {
        return inputMap;
    }

    public HashMap<UId, CraftFlowTreeNode> getTempInputMap() {
        return tempInputMap;
    }

    public HashMap<UId, CraftFlowTreeNode> getTempOutputMap() {
        return tempOutputMap;
    }

    public void addNodeList(Collection<CraftFlowTreeNode> nodes) {
        if (CollectionUtils.isEmpty(nodes)) {
            logger.info("empty nodelist, skip add");
            return;
        }
        nodes.forEach(this::addSingleNode);
    }

    /**
     * 移除节点，不删除父子关系：父子关系使用node.unlink方法解除
     *
     * @param craftStepNode
     */
    public void removeSingleNode(CraftFlowTreeNode craftStepNode) {
        if (Objects.isNull(craftStepNode)) {
            logger.warn("null node received, skip remove");
            return;
        }
        if (!allStepsMap.containsKey(craftStepNode.getCraftStepId())) {
            logger.warn("node[{}] not contained in tree, skip remove", craftStepNode.getCraftStepId());
            return;
        }
        this.allStepsMap.remove(craftStepNode.getCraftStepId());
        this.tempInputMap.remove(craftStepNode.getCraftStepId());
        this.tempOutputMap.remove(craftStepNode.getCraftStepId());
        this.inputMap.remove(craftStepNode.getCraftStepId());
        this.outputMap.remove(craftStepNode.getCraftStepId());
        // 解除父子关系
        craftStepNode.unlinkParentsAndSons();

        logger.info("remove node[{}] finish", craftStepNode.getCraftStepId());
    }

    public void addSingleNode(CraftFlowTreeNode craftStepNode) {
        if (Objects.isNull(craftStepNode)) {
            logger.info("null node received, skip add");
            return;
        }
        allStepsMap.put(craftStepNode.getCraftStepId(), craftStepNode);
        logger.info("add node:{}", craftStepNode);
        if (!CraftStepTypeEnum.MATERIAL_TRANSFER.equals(craftStepNode.getData().getType())) {
            // 只针对物料流转节点进行 输入/输出分类
            return;
        }
        switch (craftStepNode.getData().getTransferType()) {
            case STOCK_IN:
                addNodeToMap(outputMap, craftStepNode);
                break;
            case STOCK_OUT:
                addNodeToMap(inputMap, craftStepNode);
                break;
            case ON_THE_SCENE:
                addNodeToMap(tempOutputMap, craftStepNode);
                break;
            case ON_THE_SCENE_IN:
                addNodeToMap(tempInputMap, craftStepNode);
                break;
            default:
                break;
        }
    }

    private void addNodeToMap(HashMap<UId, CraftFlowTreeNode> targetMap, CraftFlowTreeNode node) {
        targetMap.put(node.getCraftStepId(), node);
        logger.info("add material node {} to map, type:{}, material:{}", node.getCraftStepId(),
                    node.getData().getMaterialInfo().getTransferType(), node.getMaterialId());
    }

    public void reconnectNodeByRelations(List<FlowNodeRelationV> nodeRelations) {
        if (CollectionUtils.isEmpty(allStepsMap)) {
            logger.info("no step node recorded, skip reconnect nodes");
            return;
        }
        if (CollectionUtils.isEmpty(nodeRelations)) {
            logger.info("no node relations recorded, skip reconnect nodes");
            return;
        }
        nodeRelations.forEach(this::reconnectNodeByRelation);
    }

    private void reconnectNodeByRelation(FlowNodeRelationV relationV) {
        CraftFlowTreeNode parent = allStepsMap.get(new UId(relationV.getPreNode()));
        CraftFlowTreeNode son = allStepsMap.get(new UId(relationV.getNextNode()));
        Optional.ofNullable(parent).ifPresent(p -> {
            logger.info("connect node parent[{}] with son[{}]", parent.getCraftStepId(), son.getCraftStepId());
            p.addSon(son);
        });
    }

    public boolean isEmpty() {
        return CollectionUtils.isEmpty(this.allStepsMap);
    }

    public boolean needMergeSubTree() {
        return !CollectionUtils.isEmpty(this.tempInputMap);
    }


    /**
     * 将树的每个node记录的物料num（如果存在）按照 x 倍放大
     * 用于连接两颗子树前，同步两颗树的物料数量。
     *
     * @param x
     */
    public void multiplyMaterialNumBy(Double x) {
        logger.info("multiply tree by num:{}", x);
        this.allStepsMap.values().forEach(node -> node.multiply(x));
    }

    public void syncMaterialNum(Double requiredNum) {
        if (Objects.isNull(requiredNum)) {
            logger.info("required num is null, skip calculate");
            return;
        }
        Double denominator = Optional.ofNullable(entryNode).map(CraftFlowTreeNode::getData).map(
                CraftFlowNodeEntity::getMaterialNum).orElse(1.0);
        Double x = DoubleUtil.divideWithHighPrecision(requiredNum, denominator);
        logger.info("calculate: {}/{} = {}", requiredNum, denominator, x);
        this.multiplyMaterialNumBy(x);
    }

    /**
     * 将子树的节点合并到本树的节点，用于合并存在线上流转关系的两棵树。
     * 适用于 基于 单个node的拼接
     *  todo：
     *  适配 单个树，有多个匹配的拼接
     * <p>
     *  规则：
     *  1. 如果本树的tempInputMap为空。则无需对 【线上流转】类型的输入物料进行查找合并
     *  2.子树的entryNode为入口（线上流转输出节点），找到主树tempInput中的 临时输入node ：
     *      2.0 计算乘数，同步主树、子树的数量。
     *      2.1 子树.entryNode的parents，连接到主树 临时输入node 的sons
     *      2.2 从子树中删除 子树.entryNode, 对子树.entryNode执行unlink父子
     *      2.3 从主树中删除 临时输入node， 对临时输入node执行unlink父子
     *      2.4 将子树的所有节点添加到主树（putAll 5个map）
     * <p>
     *  限制：
     *      1. maintree中只允许存在单个待合并的【线上流转-输入】类型物料节点
     *
     * @param subTree
     */
    public void mergeSubTreeMap(CraftFlowTreeMap subTree) {
        if (Objects.isNull(subTree) || subTree.isEmpty()) {
            logger.info("subtree is empty, skip merge");
            return;
        }
        if (CollectionUtils.isEmpty(this.tempInputMap)) {
            logger.info("main tree has no temp input, skip merge");
            return;
        }
        // 从主树的临时输入中找到和子树entryNode物料id相同的临时输入
        // 如果命中本树的临时输入,则:
        // 1. 同步两棵树的物料数量，
        // 2. 从本树临时输入干掉该node，
        // 3. 将子树的正式输入输出添加到本树。
        CraftFlowTreeNode subNode = subTree.getEntryNode();
        logger.info("to merge subtree entry node:{}", subNode);
        MaterialId mergeMatId = subNode.getMaterialId();
        CraftFlowTreeNode mainNode = this.tempInputMap.values().stream()
                                                      .filter(node -> Objects.equals(mergeMatId,
                                                                                     node.getMaterialId()))
                                                      .findFirst().get();
        logger.info("to merge main tree node:{}", mainNode);
        // 同步子树的物料数量
        Double multiple = mainNode.calculateMultiple(subNode);
        subTree.multiplyMaterialNumBy(multiple);

        // 中继关系：subNode 的parent，连接到 mainNode的sons
        subNode.getParents().forEach(p -> p.addSons(mainNode.getSons()));

        this.removeSingleNode(mainNode);
        subTree.removeSingleNode(subNode);

        // subtree的节点同步到main tree
        subTree.getAllStepsMap().values().forEach(this::addSingleNode);
    }

    @Override
    public Object toRepresent() {
        return null;
    }

    /**
     * 临时输出（线上流转）+正式产出（生产入库）
     */
    public List<CraftFlowTreeNode> getAllOutputNodeList() {
        ArrayList<CraftFlowTreeNode> ret = new ArrayList<>();
        ret.addAll(getOutputMap().values().stream().collect(Collectors.toList()));
        ret.addAll(getTempOutputMap().values().stream().collect(Collectors.toList()));
        return ret;
    }

    /**
     * 临时输入（线上流转）+正式输入（领料出库）
     */
    public List<CraftFlowTreeNode> getAllInputNodeList() {
        ArrayList<CraftFlowTreeNode> ret = new ArrayList<>();
        ret.addAll(getInputMap().values().stream().collect(Collectors.toList()));
        ret.addAll(getTempInputMap().values().stream().collect(Collectors.toList()));
        return ret;
    }
}
