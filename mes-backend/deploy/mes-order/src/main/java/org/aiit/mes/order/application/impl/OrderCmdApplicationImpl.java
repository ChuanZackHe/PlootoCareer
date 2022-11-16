package org.aiit.mes.order.application.impl;

import cn.hutool.core.util.ObjectUtil;
import org.aiit.mes.base.customer.domain.dao.service.ICustomerInfoService;
import org.aiit.mes.base.material.domain.dao.service.IBaseMaterialInfoService;
import org.aiit.mes.common.constant.BusinessEnum;
import org.aiit.mes.common.constant.CommonSummaryStatusEnum;
import org.aiit.mes.common.entity.BaseEntity;
import org.aiit.mes.common.eventbus.EventBusUtil;
import org.aiit.mes.common.eventbus.constant.EventOperationEnum;
import org.aiit.mes.common.eventbus.constant.EventTopicEnum;
import org.aiit.mes.common.eventbus.model.BaseEvent;
import org.aiit.mes.common.exception.OperationFailedException;
import org.aiit.mes.common.statemachine.StateMachineUtil;
import org.aiit.mes.common.util.DoubleUtil;
import org.aiit.mes.common.util.IDGeneratorUtil;
import org.aiit.mes.order.application.IOrderCmdApplication;
import org.aiit.mes.order.constant.DeliveryDetailStatusEnum;
import org.aiit.mes.order.constant.OrderDetailStatusEnum;
import org.aiit.mes.order.constant.ResourceCategory;
import org.aiit.mes.order.domain.dao.entity.DeliveryDetailEntity;
import org.aiit.mes.order.domain.dao.entity.DeliverySummaryEntity;
import org.aiit.mes.order.domain.dao.entity.OrderDetailEntity;
import org.aiit.mes.order.domain.dao.entity.OrderSummaryEntity;
import org.aiit.mes.order.domain.dao.service.IDeliveryDetailService;
import org.aiit.mes.order.domain.dao.service.IDeliverySummaryService;
import org.aiit.mes.order.domain.dao.service.IOrderDetailService;
import org.aiit.mes.order.domain.dao.service.IOrderSummaryService;
import org.aiit.mes.order.domain.dto.delivery_detail.DeliveryDetailCloseCmd;
import org.aiit.mes.order.domain.dto.delivery_detail.DeliveryDetailCreateCmd;
import org.aiit.mes.order.domain.dto.delivery_detail.DeliveryDetailRepresent;
import org.aiit.mes.order.domain.dto.delivery_detail.DeliveryDetailUpdateCmd;
import org.aiit.mes.order.domain.dto.delivery_summary.DeliverySummaryCreateCmd;
import org.aiit.mes.order.domain.dto.delivery_summary.DeliverySummaryRepresent;
import org.aiit.mes.order.domain.dto.delivery_summary.DeliverySummaryUpdateCmd;
import org.aiit.mes.order.domain.dto.order_detail.*;
import org.aiit.mes.order.domain.dto.order_summary.OrderSummaryCreateCmd;
import org.aiit.mes.order.domain.dto.order_summary.OrderSummaryRepresent;
import org.aiit.mes.order.domain.dto.order_summary.OrderSummaryUpdateCmd;
import org.aiit.mes.order.handler.delivery.DeliveryDetailEvent;
import org.aiit.mes.order.handler.delivery.DeliverySummaryEvent;
import org.aiit.mes.order.handler.order.OrderDetailEvent;
import org.aiit.mes.order.handler.order.OrderSummaryEvent;
import org.aiit.mes.warehouse.stockout.application.IStockOutCmdApplication;
import org.aiit.mes.warehouse.stockout.domain.dto.StockOutDetailCreateCmd;
import org.aiit.mes.warehouse.stockout.domain.dto.StockOutSummaryCreateCmd;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderCmdApplicationImpl implements IOrderCmdApplication {

    private static final Logger logger = LoggerFactory.getLogger(OrderCmdApplicationImpl.class);

    private EventBusUtil eventBusUtil = EventBusUtil.getInstance();

    @Resource
    private IOrderSummaryService orderSummaryService;

    @Resource
    private IOrderDetailService orderDetailService;

    @Resource
    private IDeliverySummaryService deliverySummaryService;

    @Resource
    private IDeliveryDetailService deliveryDetailService;

    @Resource
    private IStockOutCmdApplication stockOutCmdApplication;

    @Resource
    private IBaseMaterialInfoService materialInfoService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderSummaryRepresent addOrderSummary(OrderSummaryCreateCmd orderSummaryCreateCmd) {
        String documentId = IDGeneratorUtil.generateId(BusinessEnum.DD);
        logger.info("开始添加订单总单{}", documentId);
        OrderSummaryEntity orderSummaryEntity = orderSummaryCreateCmd.toEntity();
        orderSummaryEntity.setDocumentId(documentId);
        //状态机进行主单创建
        OrderSummaryEvent baseEvent = new OrderSummaryEvent(EventTopicEnum.ORDER_SUMMARY, EventOperationEnum.CREATE,
                                                    orderSummaryEntity);
        StateMachineUtil.fire(baseEvent, CommonSummaryStatusEnum.IDLE);
        //状态机进行子单创建
        List<OrderDetailCreateCmd> detailCreateCmds = orderSummaryCreateCmd.getOrderDetailCreateCmds();
        if (CollectionUtils.isNotEmpty(detailCreateCmds)) {
            detailCreateCmds.forEach(t -> {
                t.setDocumentId(documentId);
                addOrderDetail(t);
            });
        }
        logger.info("完成添加订单总单{}", documentId);
        return orderSummaryService.getByDocumentId(documentId).toRepresent();

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderDetailRepresent addOrderDetail(OrderDetailCreateCmd orderDetailCreateCmd) {
        OrderDetailEntity orderDetailEntity = orderDetailCreateCmd.toEntity();
        OrderDetailEvent detailEvent = new OrderDetailEvent(EventTopicEnum.ORDER_DETAIL, EventOperationEnum.CREATE,
                                                     orderDetailEntity);
        StateMachineUtil.fire(detailEvent, OrderDetailStatusEnum.IDLE);
        return orderDetailEntity.toRepresent();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public DeliverySummaryRepresent addDeliverySummary(DeliverySummaryCreateCmd deliverySummaryCreateCmd) {
        String documentId = IDGeneratorUtil.generateId(BusinessEnum.JF);
        logger.info("开始添加交付总单{}", documentId);
        DeliverySummaryEntity deliverySummaryEntity = deliverySummaryCreateCmd.toEntity();
        deliverySummaryEntity.setDocumentId(documentId);
        //状态机进行主单创建
        DeliverySummaryEvent baseEvent = new DeliverySummaryEvent(EventTopicEnum.DELIVERY_SUMMARY, EventOperationEnum.CREATE,
                                                       deliverySummaryEntity);
        StateMachineUtil.fire(baseEvent, CommonSummaryStatusEnum.IDLE);
        //状态机进行子单创建
        List<DeliveryDetailCreateCmd> detailCreateCmds = deliverySummaryCreateCmd.getDeliveryDetailCreateCmds();
        if (CollectionUtils.isNotEmpty(detailCreateCmds)) {
            detailCreateCmds.forEach(t -> {
                t.setDocumentId(documentId);
                addDeliveryDetail(t);
            });
        }
        logger.info("完成添加交付总单{}", documentId);
        return deliverySummaryService.getByDocumentId(documentId).toRepresent();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public DeliveryDetailRepresent addDeliveryDetail(DeliveryDetailCreateCmd deliveryDetailCreateCmd) {
        DeliveryDetailEntity entity = deliveryDetailCreateCmd.toEntity();
        DeliveryDetailEvent createEvent = new DeliveryDetailEvent(EventTopicEnum.DELIVERY_DETAIL, EventOperationEnum.CREATE,
                                                        entity);
        StateMachineUtil.fire(createEvent, DeliveryDetailStatusEnum.IDLE);
        return deliveryDetailService.getById(entity.getId()).toRepresent();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderSummaryRepresent updateOrderSummary(OrderSummaryUpdateCmd orderSummaryUpdateCmd) {
        OrderSummaryEntity summaryEntity = orderSummaryService.getByDocumentId(orderSummaryUpdateCmd.getDocumentId());
        CommonSummaryStatusEnum status = summaryEntity.getStatus();
        if (!ObjectUtil.equal(status, CommonSummaryStatusEnum.NEW) && !ObjectUtil.equal(status,
                                                                                        CommonSummaryStatusEnum.TO_CONFIRM)) {
            throw OperationFailedException.updateFailed(ResourceCategory.ORDER_SUMMARY, "当前状态不支持修改");
        }
        logger.info(String.format("开始修改订单总单%s", orderSummaryUpdateCmd.getDocumentId()));
        OrderSummaryEntity toUpdate = orderSummaryUpdateCmd.toEntity();
        toUpdate.setId(summaryEntity.getId());

        if(orderSummaryService.updateById(toUpdate)){
            eventBusUtil.postAsync(new BaseEvent<>(EventTopicEnum.ORDER_SUMMARY, EventOperationEnum.UPDATE,
                                                   toUpdate));
            logger.info(String.format("修改订单总单%s成功", orderSummaryUpdateCmd.getDocumentId()));
            return toUpdate.toRepresent();
        }else {
            throw OperationFailedException.updateFailed(ResourceCategory.ORDER_SUMMARY, "更新失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderDetailRepresent updateOrderDetail(OrderDetailUpdateCmd orderDetailUpdateCmd) {
        OrderDetailStatusEnum status = orderDetailService.getById(orderDetailUpdateCmd.getId()).getStatus();
        if (!ObjectUtil.equal(status, OrderDetailStatusEnum.NEW)) {
            throw OperationFailedException.updateFailed(ResourceCategory.ORDER_DETAIL, "当前状态不支持修改");
        }
        logger.info(String.format("开始修改订单子单%s", orderDetailUpdateCmd.getId()));
        OrderDetailEntity orderDetailEntity = orderDetailUpdateCmd.toEntity();
        if(orderDetailService.updateById(orderDetailEntity)){
            eventBusUtil.postAsync(new BaseEvent<>(EventTopicEnum.ORDER_DETAIL, EventOperationEnum.UPDATE,
                                                   orderDetailEntity.toRepresent()));
            logger.info(String.format("修改订单子单%s成功", orderDetailUpdateCmd.getId()));
            return orderDetailEntity.toRepresent();
        }else{
            throw OperationFailedException.updateFailed(ResourceCategory.ORDER_DETAIL, "更新失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public DeliverySummaryRepresent updateDeliverySummary(DeliverySummaryUpdateCmd deliverySummaryUpdateCmd) {
        logger.info(String.format("开始修改交付单总单%s", deliverySummaryUpdateCmd.getDocumentId()));
        DeliverySummaryEntity deliverySummaryEntity = deliverySummaryService.getByDocumentId(deliverySummaryUpdateCmd.getDocumentId());
        CommonSummaryStatusEnum status = deliverySummaryEntity.getStatus();
        if (!ObjectUtil.equal(status, CommonSummaryStatusEnum.NEW) && !ObjectUtil.equal(status,
                                                                                        CommonSummaryStatusEnum.TO_CONFIRM)) {
            throw OperationFailedException.updateFailed(ResourceCategory.DELIVERY_SUMMARY, "当前状态不支持修改");
        }
        DeliverySummaryEntity toUpdate = deliverySummaryUpdateCmd.toEntity();
        toUpdate.setId(deliverySummaryEntity.getId());
        deliverySummaryService.updateWithcCheck(toUpdate);
        eventBusUtil.postAsync(new BaseEvent<>(EventTopicEnum.DELIVERY_SUMMARY, EventOperationEnum.UPDATE, toUpdate));
        logger.info(String.format("修改交付单总单%s成功", deliverySummaryUpdateCmd.getDocumentId()));
        return toUpdate.toRepresent();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public DeliveryDetailRepresent updateDeliveryDetail(DeliveryDetailUpdateCmd deliveryDetailUpdateCmd) {
        logger.info(String.format("开始修改交付单子单%s", deliveryDetailUpdateCmd.getId()));
        DeliveryDetailEntity deliveryDetailEntity = deliveryDetailUpdateCmd.toEntity();
        DeliveryDetailEntity dbEntity = deliveryDetailService.getById(deliveryDetailEntity.getId());
        DeliveryDetailStatusEnum status = dbEntity.getStatus();
        if (!ObjectUtil.equal(status, DeliveryDetailStatusEnum.NEW)) {
            throw OperationFailedException.updateFailed(ResourceCategory.DELIVERY_DETAIL, "当前状态不支持修改");
        }
        if(deliveryDetailService.updateById(deliveryDetailEntity)){
            eventBusUtil.postAsync(new BaseEvent<>(EventTopicEnum.DELIVERY_DETAIL, EventOperationEnum.UPDATE,
                                                   deliveryDetailEntity.toRepresent()));
            logger.info(String.format("开始修改交付单子单%s", deliveryDetailUpdateCmd.getId()));
            return deliveryDetailEntity.toRepresent();
        }else{
            throw OperationFailedException.updateFailed(ResourceCategory.DELIVERY_DETAIL, "更新失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteOrderSummary(String orderCode) {
        CommonSummaryStatusEnum status = orderSummaryService.getByDocumentId(orderCode).getStatus();
        if (!ObjectUtil.equal(status, CommonSummaryStatusEnum.NEW) && !ObjectUtil.equal(status,
                                                                                        CommonSummaryStatusEnum.TO_CONFIRM)) {
            throw OperationFailedException.deleteFailed(ResourceCategory.ORDER_SUMMARY, "当前状态不支持删除");
        }
        logger.info(String.format("开始删除订单总单%s", orderCode));
        orderSummaryService.delete(orderCode);
        eventBusUtil.postAsync(new BaseEvent<>(EventTopicEnum.ORDER_SUMMARY, EventOperationEnum.DELETE, orderCode));
        return orderCode;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long deleteOrderDetail(Long id) {
        OrderDetailStatusEnum status = orderDetailService.getById(id).getStatus();
        if (!ObjectUtil.equal(status, OrderDetailStatusEnum.NEW)) {
            throw OperationFailedException.updateFailed(ResourceCategory.ORDER_DETAIL, "当前状态不支持删除");
        }
        logger.info(String.format("开始删除订单子单%s", id));
        orderDetailService.delete(id);
        eventBusUtil.postAsync(new BaseEvent<>(EventTopicEnum.ORDER_DETAIL, EventOperationEnum.DELETE, id));
        return id;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteDeliverySummary(String deliverySummaryCode) {
        DeliverySummaryEntity deliverySummaryEntity = deliverySummaryService.getByDocumentId(deliverySummaryCode);
        if (!ObjectUtil.equal(deliverySummaryEntity.getStatus(), CommonSummaryStatusEnum.NEW) && !ObjectUtil.equal(
                deliverySummaryEntity.getStatus(), CommonSummaryStatusEnum.TO_CONFIRM)) {
            throw OperationFailedException.deleteFailed(ResourceCategory.DELIVERY_SUMMARY, "当前状态不支持删除");
        }
        logger.info(String.format("开始删除交付单总单%s", deliverySummaryCode));
        deliverySummaryService.delete(deliverySummaryCode);
        eventBusUtil.postAsync(
                new BaseEvent<>(EventTopicEnum.DELIVERY_SUMMARY, EventOperationEnum.DELETE, deliverySummaryCode));
        return deliverySummaryCode;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long deleteDeliveryDetail(Long id) {
        DeliveryDetailEntity deliveryDetailEntity = deliveryDetailService.getById(id);
        if (!ObjectUtil.equal(deliveryDetailEntity.getStatus(), DeliveryDetailStatusEnum.NEW)) {
            throw OperationFailedException.deleteFailed(ResourceCategory.DELIVERY_DETAIL, "当前状态不支持删除");
        }
        //如果只剩一个
        List<DeliveryDetailEntity> retailEntities =
                deliveryDetailService.listByDocumentId(deliveryDetailEntity.getDocumentId());
        if(retailEntities.size()==1){
            throw OperationFailedException.deleteFailed(ResourceCategory.DELIVERY_DETAIL, "只剩一个交付单子单，不允许删除，请直接删除交付总单");
        }
        logger.info(String.format("开始删除交付单子单%s", id));
        deliveryDetailService.delete(id);
        eventBusUtil.postAsync(new BaseEvent<>(EventTopicEnum.DELIVERY_DETAIL, EventOperationEnum.DELETE, id));
        return id;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String confirmOrderSummary(String documentId) {
        OrderSummaryEntity orderSummaryEntity = orderSummaryService.getByDocumentId(documentId);
        Assert.notNull(orderSummaryEntity);
        OrderSummaryEvent event = new OrderSummaryEvent(EventTopicEnum.ORDER_SUMMARY, EventOperationEnum.CONFIRM,
                                           orderSummaryEntity);
        StateMachineUtil.fire(event, orderSummaryEntity.getStatus());
        return documentId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String confirmDeliverySummary(String deliveryCode) {
        DeliverySummaryEntity deliverySummaryEntity = deliverySummaryService.getByDocumentId(deliveryCode);
        Assert.notNull(deliverySummaryEntity);
        DeliverySummaryEvent event = new DeliverySummaryEvent(EventTopicEnum.DELIVERY_SUMMARY, EventOperationEnum.CONFIRM,
                                        deliverySummaryEntity);
        StateMachineUtil.fire(event, deliverySummaryEntity.getStatus());
        return deliveryCode;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long closeOrderDetail(OrderDetailCloseCmd orderDetailCloseCmd) {
        OrderDetailEntity orderDetailEntity = orderDetailService.getById(orderDetailCloseCmd.getId());
        orderDetailEntity.setDescription(orderDetailCloseCmd.getDescription());
        Assert.notNull(orderDetailEntity);
        OrderDetailEvent event = new OrderDetailEvent(EventTopicEnum.ORDER_DETAIL, EventOperationEnum.CLOSE, orderDetailEntity);
        StateMachineUtil.fire(event, orderDetailEntity.getStatus());
        return orderDetailEntity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long closeDeliveryDetail(DeliveryDetailCloseCmd deliveryDetailCloseCmd) {
        DeliveryDetailEntity deliveryDetailEntity = deliveryDetailService.getById(deliveryDetailCloseCmd.getId());
        deliveryDetailEntity.setDescription(deliveryDetailCloseCmd.getDescription());
        Assert.notNull(deliveryDetailEntity);
        DeliveryDetailEvent event = new DeliveryDetailEvent(EventTopicEnum.DELIVERY_DETAIL, EventOperationEnum.CLOSE, deliveryDetailEntity);
        StateMachineUtil.fire(event, deliveryDetailEntity.getStatus());
        return deliveryDetailEntity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<Long, Double> getCapacity(CapacityQuery capacityQuery) {
        List<OrderDetailEntity> orderDetailEntities = orderDetailService.listByIds(capacityQuery.getIds());
        Map<Long, Double> result = orderDetailEntities.stream().collect(Collectors.toMap(BaseEntity::getId, t->{
            List<DeliveryDetailEntity> recordLists = deliveryDetailService.listByOrderDetailId(t.getId());
            Double sum = DoubleUtil.sum(recordLists.stream().map(DeliveryDetailEntity::getCount).collect(Collectors.toList()));
            BigDecimal count = BigDecimal.valueOf(t.getCount());
            BigDecimal allocatedCount = BigDecimal.valueOf(Optional.ofNullable(t.getAllocatedCount()).orElse(0.0));
            return count.subtract(allocatedCount).subtract(BigDecimal.valueOf(sum)).doubleValue();
        }));

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createStockOut(StockOutSummaryCreateCmd createCmd) {
        DeliverySummaryEntity summaryEntity = deliverySummaryService.getByDocumentId(createCmd.getDeliveryId());
        OrderSummaryEntity orderSummaryEntity =
                orderSummaryService.getByDocumentId(summaryEntity.getOrderSummaryCode());
        createCmd.setCustomer(orderSummaryEntity.getCustomerName());
        if(CollectionUtils.isNotEmpty(createCmd.getDetailList())){
            List<StockOutDetailCreateCmd> detailList = createCmd.getDetailList();
            detailList.forEach(t->{
                t.setMaterialUnit(materialInfoService.getById(t.getMaterialId()).getUnit());
            });
        }
        stockOutCmdApplication.saveStockOutSummary(createCmd);
    }
}
