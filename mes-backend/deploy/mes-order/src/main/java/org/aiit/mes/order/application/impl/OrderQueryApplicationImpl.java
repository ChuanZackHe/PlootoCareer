package org.aiit.mes.order.application.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.aiit.mes.base.material.domain.dao.service.IBaseMaterialInfoService;
import org.aiit.mes.base.material.domain.entity.BaseMaterialEntity;
import org.aiit.mes.common.entity.BaseEntity;
import org.aiit.mes.common.util.DoubleUtil;
import org.aiit.mes.common.util.PageConverter;
import org.aiit.mes.order.application.IOrderQueryApplication;
import org.aiit.mes.order.constant.DeliveryDetailStatusEnum;
import org.aiit.mes.order.domain.dao.entity.DeliveryDetailEntity;
import org.aiit.mes.order.domain.dao.entity.DeliverySummaryEntity;
import org.aiit.mes.order.domain.dao.entity.OrderDetailEntity;
import org.aiit.mes.order.domain.dao.entity.OrderSummaryEntity;
import org.aiit.mes.order.domain.dao.service.IDeliveryDetailService;
import org.aiit.mes.order.domain.dao.service.IDeliverySummaryService;
import org.aiit.mes.order.domain.dao.service.IOrderDetailService;
import org.aiit.mes.order.domain.dao.service.IOrderSummaryService;
import org.aiit.mes.order.domain.dto.delivery_detail.DeliveryDetailQuery;
import org.aiit.mes.order.domain.dto.delivery_detail.DeliveryDetailRepresent;
import org.aiit.mes.order.domain.dto.delivery_summary.DeliverySummaryQuery;
import org.aiit.mes.order.domain.dto.order_detail.OrderDetailQuery;
import org.aiit.mes.order.domain.dto.order_detail.OrderDetailRepresent;
import org.aiit.mes.order.domain.dto.order_summary.OrderOverviewQuery;
import org.aiit.mes.order.domain.dto.order_summary.OrderOverviewRepresent;
import org.aiit.mes.order.domain.dto.order_summary.OrderSummaryQuery;
import org.aiit.mes.warehouse.stock.domain.dao.entity.StockSummaryEntity;
import org.aiit.mes.warehouse.stock.domain.dao.service.IStockSummaryService;
import org.aiit.mes.warehouse.stockout.domain.dao.service.IStockOutDetailService;
import org.aiit.mes.warehouse.stockout.domain.dao.service.IStockOutSummaryService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrderQueryApplicationImpl implements IOrderQueryApplication {

    @Resource
    IOrderSummaryService orderSummaryService;

    @Resource
    IOrderDetailService orderDetailService;

    @Resource
    IDeliverySummaryService deliverySummaryService;

    @Resource
    IDeliveryDetailService deliveryDetailService;

    @Resource
    IBaseMaterialInfoService materialInfoService;

    @Resource
    IStockOutSummaryService stockOutSummaryService;

    @Resource
    IStockSummaryService stockSummaryService;

    @Override
    public Page<OrderSummaryEntity> listOrderInfo(
            OrderSummaryQuery queryCondition) {
        return orderSummaryService.listOrderInfo(queryCondition);
    }

    @Override
    public Page<OrderDetailRepresent> listOrderDetailInfo(
            OrderDetailQuery queryCondition) {
        PageConverter<OrderDetailEntity, OrderDetailRepresent> pageConverter = new PageConverter<>();
        Page<OrderDetailRepresent> representPage = pageConverter.convertPageEntityToRepresent(orderDetailService.listOrderDetailInfo(queryCondition));
        //对每一个订单子单，需要找到这个物料所没有确认的所有交付子单，扣除这些数量
        List<OrderDetailRepresent> records = representPage.getRecords();

        for(OrderDetailRepresent represent:records){
            List<DeliveryDetailEntity> recordLists = deliveryDetailService.listByOrderDetailId(represent.getId());
            Double sum =
                    DoubleUtil.sum(recordLists.stream().map(DeliveryDetailEntity::getCount).collect(Collectors.toList()));
            BigDecimal count = BigDecimal.valueOf(represent.getCount());
            represent.setCapacity(count.subtract(BigDecimal.valueOf(sum)).doubleValue());
        }
        return representPage;
    }

    @Override
    public Page<DeliverySummaryEntity> listDeliveryInfo(
            DeliverySummaryQuery queryCondition) {
        return deliverySummaryService.listDeliveryInfo(queryCondition);
    }

    @Override
    public Page<DeliveryDetailRepresent> listDeliveryDetailInfo(
            DeliveryDetailQuery queryCondition) {
        Page<DeliveryDetailEntity> deliveryDetailEntityPage =
                deliveryDetailService.listDeliveryDetailInfo(queryCondition);
        PageConverter<DeliveryDetailEntity, DeliveryDetailRepresent> converter = new PageConverter<>();
        Page<DeliveryDetailRepresent> representPage = converter.convertPageEntityToRepresent(deliveryDetailEntityPage);
        List<Long> materialId = representPage.getRecords().stream().map(DeliveryDetailRepresent::getMaterialId).collect(
                Collectors.toList());
        //TODO NPE风险
        Map<Long, BaseMaterialEntity> materialEntityMap = CollectionUtils.isNotEmpty(materialId)?
                materialInfoService.listByIds(materialId).stream().collect(Collectors.toMap(BaseEntity::getId,
                                                                                            Function.identity())):
                new HashMap<>();
        Map<Long, Double> satisfiedMap = stockOutSummaryService.getSatisfiedMaterialCount(queryCondition.getDocumentId());

        Map<Long, Double> stockMap =
                stockSummaryService.listByMaterialIds(representPage.getRecords().stream().map(DeliveryDetailRepresent::getMaterialId).collect(
                        Collectors.toList())).stream().collect(Collectors.toMap(StockSummaryEntity::getMaterialId,
                                                                                StockSummaryEntity::getCount));
        representPage.getRecords().forEach(record->{
            record.setMaterialType(materialEntityMap.get(record.getMaterialId()).getType().getDesc());
            record.setStockOutCount(satisfiedMap.getOrDefault(record.getMaterialId(), 0.0));
            record.setStockCount(stockMap.getOrDefault(record.getMaterialId(), 0.0));
            record.setSatisfied(record.getCount()<=record.getStockCount());
        });

        return representPage;
    }

    @Override
    public OrderSummaryEntity showOrder(String documentId) {
        return orderSummaryService.getByDocumentId(documentId);
    }

    @Override
    public DeliverySummaryEntity showDeliveryInfo(String documentId) {
        return deliverySummaryService.getByDocumentId(documentId);
    }

    @Override
    public Page<OrderOverviewRepresent> listOrderOverview(OrderOverviewQuery orderOverviewQuery) {
        //分页查询所有订单，正序排序
        Page<OrderSummaryEntity> orderSummaryEntityPage = orderSummaryService.listOrders(orderOverviewQuery);
        Page<OrderOverviewRepresent> overviewRepresentPage =
                Page.of(orderSummaryEntityPage.getCurrent(),orderSummaryEntityPage.getSize(),
                        orderSummaryEntityPage.getTotal());
        overviewRepresentPage.setRecords(orderSummaryEntityPage.getRecords().stream().map(OrderSummaryEntity::toOverview).collect(
                Collectors.toList()));
        //根据这些订单，查询交付单的信息
        List<DeliverySummaryEntity> deliverySummaryEntities =
                deliverySummaryService.listByOrderDocumentIds(overviewRepresentPage.getRecords().stream().map(OrderOverviewRepresent::getDocumentId).collect(
                Collectors.toList()));
        Map<String, List<DeliverySummaryEntity>> orderDeliveryMap =
                deliverySummaryEntities.stream().collect(Collectors.groupingBy(DeliverySummaryEntity::getOrderSummaryCode));
        //填充交付单的信息
        overviewRepresentPage.getRecords().forEach(t->{
            if(orderDeliveryMap.containsKey(t.getDocumentId())){
                t.setDeliverySummaryRepresentList(orderDeliveryMap.get(t.getDocumentId()).stream().map(DeliverySummaryEntity::toRepresent).collect(Collectors.toList()));
            }
        });
        return overviewRepresentPage;
    }
}
