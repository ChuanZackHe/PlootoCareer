package org.aiit.mes.craft.application;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.aiit.mes.base.material.domain.entity.BaseMaterialEntity;
import org.aiit.mes.common.primitive.LongId;
import org.aiit.mes.craft.domain.aggregate.FlowAgg;
import org.aiit.mes.craft.domain.crafttree.CraftFlowTreeMap;
import org.aiit.mes.craft.domain.dao.entity.CraftComponentEntity;
import org.aiit.mes.craft.domain.dao.entity.CraftFlowEntity;
import org.aiit.mes.craft.domain.dto.AnalyzeBomForProduceQuery;
import org.aiit.mes.craft.domain.dto.CraftComponentQuery;
import org.aiit.mes.craft.domain.dto.CraftFlowQuery;
import org.aiit.mes.craft.domain.vo.CraftBomTreeRepresentV;
import org.aiit.mes.warehouse.stock.domain.dao.entity.StockSummaryEntity;

import java.util.List;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName ICraftApplication
 * @Description
 * @createTime 2021.09.02 17:53
 */
public interface ICraftQueryApplication {

    /**
     * 批量查询组件
     *
     * @param query
     * @return
     */
    public Page<CraftComponentEntity> pageListComponents(CraftComponentQuery query);

    public Page<CraftFlowEntity> pageListFlowSummary(CraftFlowQuery query);

    public FlowAgg showFlowById(LongId flowId);

    /**
     * 根据流程id、物料id、物料数量查询该流程对应的物料输入输出树，会自动向上查询并组装 输入为【线上流转-输入】的子树
     *
     * @param
     * @return
     */
    public CraftFlowTreeMap queryRawMaterialTree(AnalyzeBomForProduceQuery query);

    /**
     * 进行生产用的需求分析, 查询指定target物料的生产所需原材料，库存数量是否满足生产需要
     *
     * @param query
     * @return
     */
    public CraftBomTreeRepresentV analyzeBomForProduce(AnalyzeBomForProduceQuery query);

    List<BaseMaterialEntity> queryMaterialInfosByIds(List<Long> materialIds);

    List<StockSummaryEntity> queryStockInfoByMaterialIds(List<Long> materialIds);

    /**
     * 生产模块使用：查询完整的生产工艺树
     *
     * @param query
     * @return
     */
    public CraftFlowTreeMap queryFullProduceTree(AnalyzeBomForProduceQuery query);
}
