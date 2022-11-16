package org.aiit.mes.craft.domain.util;

import org.aiit.mes.common.constant.CraftOperationTypeEnum;
import org.aiit.mes.common.constant.CraftStepTypeEnum;
import org.aiit.mes.common.exception.ParamInvalidException;
import org.aiit.mes.common.graph.Node;
import org.aiit.mes.common.primitive.Coordinate;
import org.aiit.mes.common.primitive.UId;
import org.aiit.mes.common.util.CommonUtils;
import org.aiit.mes.craft.domain.aggregate.FlowAgg;
import org.aiit.mes.craft.domain.crafttree.CraftFlowTreeMap;
import org.aiit.mes.craft.domain.crafttree.CraftFlowTreeNode;
import org.aiit.mes.craft.domain.dao.entity.CraftFlowNodeEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

import static org.aiit.mes.common.constant.Constants.X_BIAS;
import static org.aiit.mes.common.constant.Constants.Y_BIAS;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName FlowGraphUtil
 * @Description
 * @createTime 2022.01.14 10:09
 */
public class FlowGraphUtil {

    private static final Coordinate ANCHOR = Coordinate.newCor(200L, 80L);

    private static final Logger logger = LoggerFactory.getLogger(FlowGraphUtil.class);

    public static void verifyTreeGraph(CraftFlowTreeMap tree) {
        if (CollectionUtils.isEmpty(tree.getAllInputNodeList())) {
            throw new ParamInvalidException("未定义物料输入节点");
        }
        if (CollectionUtils.isEmpty(tree.getAllOutputNodeList())) {
            throw new ParamInvalidException("未定义物料输出节点");
        }
        verifyInputHasNoParent(tree);

        verifyOutputHasNoSon(tree);

        // TODO: 2022/1/14 当前限制：只允许单个产出
        verifySingleOutput(tree);

        verifySingleInput(tree);

        verifyAllNodesConnected(tree);

        verifyDeadLoop(tree);

        verifyInputOutputUnique(tree);
    }

    private static void verifyInputOutputUnique(CraftFlowTreeMap tree) {
        HashSet<Long> usedMaterialIdSet = new HashSet<>();
        try {
            for (CraftFlowTreeNode node : tree.getAllStepsMap().values()) {
                if (CraftStepTypeEnum.MATERIAL_TRANSFER.equals(node.getData().getType())) {
                    // node携带的物料id没有重复
                    Assert.isTrue(!usedMaterialIdSet.contains(node.getMaterialId().getId()),
                                  String.format("存在重复物料：节点[%s]的物料[%s]", node.getData().getName(),
                                                node.getData().getMaterialInfo().getName()));
                    usedMaterialIdSet.add(node.getMaterialId().getId());
                }
            }
        }
        catch (Exception e) {
            logger.error("material id duplicated");
            throw new ParamInvalidException(e.getMessage());
        }

    }

    public static void verifyFlowGraph(FlowAgg flowAgg) {
        // 空：没有异常
        if (Objects.isNull(flowAgg) || CollectionUtils.isEmpty(flowAgg.getNodes())) {
            return;
        }
        CraftFlowTreeMap tree = getCraftFlowTreeMap(flowAgg);

        verifyTreeGraph(tree);
    }

    public static void calculateFlowNodeCoordinate(FlowAgg flowAgg) {
        if (Objects.isNull(flowAgg) || CollectionUtils.isEmpty(flowAgg.getNodes())) {
            logger.info("skip calculate coordinates");
            return;
        }
        CraftFlowTreeMap tree = getCraftFlowTreeMap(flowAgg);

        try {
            LinkedBlockingDeque<Coordinate> allCordsQueue = new LinkedBlockingDeque<>();
            LinkedBlockingDeque<Coordinate> layerQueue = new LinkedBlockingDeque<>();
            HashMap<Coordinate, CraftFlowTreeNode> corNodeMap = new HashMap<>();
            for (CraftFlowTreeNode node : tree.getAllOutputNodeList()) {
                Coordinate c = Coordinate.newCor(0L, 0L);
                node.getData().setCoordinate(c);
                corNodeMap.put(c, node);
                layerQueue.add(c);
                allCordsQueue.add(c);
            }

            ArrayList<Coordinate> coordinateList = new ArrayList<>();
            while (true) {
                // 单层循环
                while (!layerQueue.isEmpty()) {
                    Coordinate coordinate = layerQueue.pop();
                    if (coordinate.isFake()) {
                        Coordinate c = Coordinate.copy(coordinate).addY(Y_BIAS).markFake();
                        coordinateList.add(c);
                        continue;
                    }
                    CraftFlowTreeNode sonNode = corNodeMap.get(coordinate);
                    if (sonNode.hasParents()) {
                        // 有父节点，继承本节点 x y+150 新建cor
                        // 同时建立cor -> 节点的索引
                        for (Node n : sonNode.getParents()) {
                            Coordinate c = Coordinate.copy(coordinate).addY(Y_BIAS);
                            CraftFlowNodeEntity e = (CraftFlowNodeEntity) n.getData();
                            e.setCoordinate(c);
                            corNodeMap.put(c, (CraftFlowTreeNode) n);
                            coordinateList.add(c);
                        }
                        continue;
                    }
                    // 没有父节点，继承fake 坐标, 标记fake节点
                    Coordinate c = Coordinate.copy(coordinate).addY(Y_BIAS).markFake();
                    coordinateList.add(c);
                }
                // 如果parent 层全部为fake节点，表示遍历结束， 否则parent层作为当前层继续遍历
                if (coordinateList.stream().filter(Coordinate::isReal).count() == 0) {
                    break;
                }
                layerQueue.addAll(coordinateList);
                // 层分隔
                allCordsQueue.add(Coordinate.BLANK);
                allCordsQueue.addAll(coordinateList);
                coordinateList.clear();
            }
            logger.info("walk finish, start calculate x axis");
            Long x = X_BIAS;
            while (!allCordsQueue.isEmpty()) {
                Coordinate c = allCordsQueue.pollLast();
                if (Coordinate.BLANK.equals(c)) {
                    // 遇到分割，重置x
                    x = X_BIAS;
                    continue;
                }

                // 占位坐标，或实际坐标但没有父节点
                if (c.isFake() || (c.isReal() && !corNodeMap.get(c).hasParents())) {
                    c.addX(x - X_BIAS);
                    x -= X_BIAS;
                    continue;
                }

                // 实际坐标，有父节点，则本坐标的x=父坐标x的平均值
                double cx = corNodeMap.get(c).getParents().stream().map(
                                              n -> (CraftFlowNodeEntity) n.getData()).map(CraftFlowNodeEntity::getCoordinate)
                                      .mapToLong(Coordinate::getX).average().getAsDouble();
                c.setX((long) cx);
                // 刷新本层的下一个bias左边， 为c的x向左BIAS个像素
                x = c.getX();
                x -= X_BIAS;
            }
            logger.info("finish calculate x axis");
            // 特殊：前端适配计算坐标，Y为反向
            long maxY = tree.getAllInputNodeList().stream().map(CraftFlowTreeNode::getData).map(
                    CraftFlowNodeEntity::getCoordinate).mapToLong(Coordinate::getY).max().getAsLong();
            long minX = tree.getAllInputNodeList().stream().map(CraftFlowTreeNode::getData).map(
                    CraftFlowNodeEntity::getCoordinate).mapToLong(Coordinate::getX).min().getAsLong();
            Coordinate bias = Coordinate.newCor(minX, -maxY).calculateBias(ANCHOR);

            tree.getAllStepsMap().values().stream().map(CraftFlowTreeNode::getData).map(
                        CraftFlowNodeEntity::getCoordinate)
                .forEach(c -> {
                    // 特殊：前端适配展示，计算坐标，Y为反向
                    c.revertY();
                    c.move(bias);
                });
        }
        catch (Exception e) {
            logger.error("calculate coordinate failed:{}", CommonUtils.getStackTrace(e));
        }
    }


    private static CraftFlowTreeMap getCraftFlowTreeMap(FlowAgg flowAgg) {
        List<CraftFlowTreeNode> nodes = flowAgg.getNodes().stream().map(CraftFlowTreeNode::new).collect(
                Collectors.toList());
        CraftFlowTreeMap tree = new CraftFlowTreeMap(nodes);
        tree.reconnectNodeByRelations(flowAgg.getRelations().getNodeRelations());
        return tree;
    }

    private static void verifySingleOutput(CraftFlowTreeMap tree) {
        List<String> ret = tree.getAllStepsMap().values().stream()
                               .filter(n -> n.hasSons())
                               .filter(n -> n.getSons().size() > 1)
                               .map(n -> n.getData().getName()).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(ret)) {
            throw new ParamInvalidException(String.format("节点存在多个输出：%s", ret));
        }
    }

    private static void verifySingleInput(CraftFlowTreeMap tree) {
        List<String> ret = tree.getAllStepsMap().values().stream()
                               .filter(n -> n.hasParents())
                               .filter(n -> n.getParents().size() > 1)
                               .filter(n -> !Objects.equals(CraftStepTypeEnum.OPERATE, n.getData().getType())
                                       || !Objects.equals(CraftOperationTypeEnum.ASSEMBLE,
                                                          n.getData().getOpInfo().getOpType()))
                               .map(n -> n.getData().getName()).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(ret)) {
            throw new ParamInvalidException(String.format("节点存在多个输入：%s", ret));
        }
    }

    private static void verifyDeadLoop(CraftFlowTreeMap tree) {
        // TODO: 2022/1/14 验证不存在死循环
    }

    private static void verifyAllNodesConnected(CraftFlowTreeMap tree) {
        // 验证：不存在孤立节点， 从下往上遍历
        HashMap<UId, CraftFlowTreeNode> walkedNodeMap = new HashMap<>();
        HashMap<UId, CraftFlowTreeNode> notWalkedNodeMap = new HashMap<>(tree.getAllStepsMap());
        recursiveWalk(tree.getAllOutputNodeList().get(0), walkedNodeMap, notWalkedNodeMap);

        if (!CollectionUtils.isEmpty(notWalkedNodeMap)) {
            throw new ParamInvalidException(String.format("存在孤立节点：%s",
                                                          notWalkedNodeMap.values().stream()
                                                                          .map(CraftFlowTreeNode::getData)
                                                                          .map(CraftFlowNodeEntity::getName)
                                                                          .collect(Collectors.toList())));
        }
        logger.info("all nodes are connected");
    }

    private static void recursiveWalk(CraftFlowTreeNode startNode,
                                      HashMap<UId, CraftFlowTreeNode> walkedNodeMap,
                                      HashMap<UId, CraftFlowTreeNode> notWalkedNodeMap) {
        if (Objects.isNull(startNode)) {
            return;
        }

        logger.info("recursive walking node {}", startNode.getCraftStepId());

        if (walkedNodeMap.containsKey(startNode.getCraftStepId())) {
            logger.info("node {} already walked, skip recurse", startNode.getCraftStepId());
            return;
        }

        walkedNodeMap.put(startNode.getCraftStepId(), startNode);
        notWalkedNodeMap.remove(startNode.getCraftStepId());

        List<Node> parents = new ArrayList<>(startNode.getParents());
        List<Node> sons = new ArrayList<>(startNode.getSons());
        startNode.unlinkParentsAndSons();

        parents.stream().forEach(n -> recursiveWalk((CraftFlowTreeNode) n, walkedNodeMap, notWalkedNodeMap));
        sons.stream().forEach(n -> recursiveWalk((CraftFlowTreeNode) n, walkedNodeMap, notWalkedNodeMap));
    }

    private static void recursiveWalkParent(CraftFlowTreeNode startNode,
                                            HashMap<UId, CraftFlowTreeNode> walkedNodeMap,
                                            HashMap<UId, CraftFlowTreeNode> notWalkedNodeMap) {
        if (Objects.isNull(startNode)) {
            return;
        }

        if (walkedNodeMap.containsKey(startNode.getCraftStepId())) {
            return;
        }

        if (CollectionUtils.isEmpty(notWalkedNodeMap)) {
            return;
        }
        walkedNodeMap.put(startNode.getCraftStepId(), startNode);
        notWalkedNodeMap.remove(startNode.getCraftStepId());

        if (startNode.hasParents()) {
            startNode.getParents().forEach(n -> recursiveWalkParent((CraftFlowTreeNode) n, walkedNodeMap,
                                                                    notWalkedNodeMap));
        }
    }

    private static void verifyInputHasNoParent(CraftFlowTreeMap tree) {
        // 验证：输入物料节点为开始节点，不包含parents
        List<String> inputParents = tree.getAllInputNodeList().stream().filter(
                n -> !CollectionUtils.isEmpty(n.getParents())).map(n -> n.getData().getName()).collect(
                Collectors.toList());
        if (!CollectionUtils.isEmpty(inputParents)) {
            throw new ParamInvalidException(String.format("物料输入节点%s不允许有父节点", inputParents));
        }
        logger.info("input nodes passed verification");
    }


    private static void verifyOutputHasNoSon(CraftFlowTreeMap tree) {
        // 验证：输出物料节点为结束节点，不包含sons
        List<String> outputSons = tree.getAllOutputNodeList().stream().filter(
                n -> !CollectionUtils.isEmpty(n.getSons())).map(n -> n.getData().getName()).collect(
                Collectors.toList());
        if (!CollectionUtils.isEmpty(outputSons)) {
            throw new ParamInvalidException(String.format("物料输出节点%s不允许有子节点", outputSons));
        }
        logger.info("output nodes passed verification");
    }
}
