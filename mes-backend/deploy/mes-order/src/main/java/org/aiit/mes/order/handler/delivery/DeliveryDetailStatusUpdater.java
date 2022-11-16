package org.aiit.mes.order.handler.delivery;

import cn.hutool.core.util.ObjectUtil;
import org.aiit.mes.common.constant.CommonSummaryStatusEnum;
import org.aiit.mes.common.eventbus.constant.EventOperationEnum;
import org.aiit.mes.common.eventbus.constant.EventTopicEnum;
import org.aiit.mes.common.eventbus.model.BaseEvent;
import org.aiit.mes.common.exception.OperationFailedException;
import org.aiit.mes.common.exception.ParamInvalidException;
import org.aiit.mes.common.statemachine.StateMachineHandler;
import org.aiit.mes.common.statemachine.StateMachineUtil;
import org.aiit.mes.order.application.IOrderCmdApplication;
import org.aiit.mes.order.constant.DeliveryDetailStatusEnum;
import org.aiit.mes.order.constant.ResourceCategory;
import org.aiit.mes.order.domain.dao.entity.DeliveryDetailEntity;
import org.aiit.mes.order.domain.dao.entity.DeliverySummaryEntity;
import org.aiit.mes.order.domain.dao.entity.OrderDetailEntity;
import org.aiit.mes.order.domain.dao.service.IDeliveryDetailService;
import org.aiit.mes.order.domain.dao.service.IDeliverySummaryService;
import org.aiit.mes.order.domain.dao.service.IOrderDetailService;
import org.aiit.mes.order.handler.order.OrderDetailEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Objects;


public class DeliveryDetailStatusUpdater implements StateMachineHandler<DeliveryDetailStatusEnum, DeliveryDetailEvent> {

    private static final Logger logger = LoggerFactory.getLogger(DeliveryDetailEntity.class);

    @Resource
    IOrderCmdApplication orderCmdApplication;

    private IDeliveryDetailService detailService;

    private IDeliverySummaryService summaryService;

    private IOrderDetailService orderDetailSevice;

    public DeliveryDetailStatusUpdater(IDeliverySummaryService deliverySummaryService,
                                       IDeliveryDetailService deliveryDetailService,
                                       IOrderDetailService orderDetailService) {
        this.summaryService = deliverySummaryService;
        this.detailService = deliveryDetailService;
        this.orderDetailSevice = orderDetailService;
    }

    @Override
    public void handle(DeliveryDetailEvent event, DeliveryDetailStatusEnum currentStatus, DeliveryDetailStatusEnum nextStatus) {
        DeliveryDetailEntity deliveryDetailEntity = event.getEventData();
        EventOperationEnum operationEnum = event.getOperationEnum();
        OrderDetailEntity orderDetail = orderDetailSevice.getById(deliveryDetailEntity.getOrderDetailId());
        if(Objects.isNull(orderDetail)){
            throw new ParamInvalidException("订单子单不存在"+deliveryDetailEntity.getOrderDetailId());
        }
        switch (operationEnum) {
            case CREATE:
                logger.info("开始新增交付单子单");
                if (deliveryDetailEntity.getCount() <= 0) {
                    throw OperationFailedException.insertFailed(ResourceCategory.DELIVERY_DETAIL, "需求物料数量需为正数");
                }
                DeliverySummaryEntity deliverySummaryEntity = summaryService.getByDocumentId(
                        deliveryDetailEntity.getDocumentId());
                if (!ObjectUtil.equal(deliverySummaryEntity.getStatus(), CommonSummaryStatusEnum.NEW) &&
                        !ObjectUtil.equal(deliverySummaryEntity.getStatus(), CommonSummaryStatusEnum.TO_CONFIRM)) {
                    throw OperationFailedException.insertFailed(ResourceCategory.DELIVERY_DETAIL, "对应交接单总单状态错误");
                }

                deliveryDetailEntity.setUserDefineInfo(orderDetail.getUserDefineInfo());
                deliveryDetailEntity.setStatus(nextStatus);
                detailService.saveWithCheck(deliveryDetailEntity);
                DeliverySummaryEvent bindEvent = new DeliverySummaryEvent(EventTopicEnum.DELIVERY_SUMMARY, EventOperationEnum.BIND,
                                                    deliverySummaryEntity);
                StateMachineUtil.fire(bindEvent, deliverySummaryEntity.getStatus());
                logger.info("新增交接单子单完成");
                break;
            case CONFIRM:
                logger.info("开始确认交接单子单{}", deliveryDetailEntity.getId());
                //查看当前子交付单对应物料是否还能被规划？
                //查询对应的订单子单
                OrderDetailEntity orderDetailEntity = orderDetailSevice.getById(
                        deliveryDetailEntity.getOrderDetailId());
                BigDecimal result =
                        BigDecimal.valueOf(orderDetailEntity.getCount()).subtract(BigDecimal.valueOf(orderDetailEntity.getAllocatedCount()).subtract(BigDecimal.valueOf(deliveryDetailEntity.getCount())));
                Assert.isTrue(result.doubleValue()>=0, "规划数量超出订单需求数量，确认失败");
                deliveryDetailEntity.setStatus(nextStatus);
                detailService.updateWithCheck(deliveryDetailEntity);
                logger.info("确认交接单子单{}完成", deliveryDetailEntity.getId());
                //增加已规划数量，CAS进行增加操作
                orderDetailSevice.updateAllocatedCountCAS(orderDetailEntity, deliveryDetailEntity.getCount());
                break;
            case CLOSE:
                logger.info("开始关闭交接单子单{}", deliveryDetailEntity.getId());
                //已规划数量返还，变化状态-》已完成，不触发相应子订单完成

                //更新状态
                deliveryDetailEntity.setStatus(nextStatus);
                detailService.updateWithCheck(deliveryDetailEntity);
                //CAS更新
                orderDetailSevice.updateAllocatedCountCAS(orderDetail, -deliveryDetailEntity.getCount());
                OrderDetailEvent event1 = new OrderDetailEvent(EventTopicEnum.ORDER_DETAIL, EventOperationEnum.UNPLAN,
                                                               orderDetail);
                StateMachineUtil.fire(event1, orderDetail.getStatus());
                logger.info("关闭交接单子单{}完成", deliveryDetailEntity.getId());
                break;
            case DONE:
                logger.info("开始完成交接单子单{}", deliveryDetailEntity.getId());
                deliveryDetailEntity.setStatus(nextStatus);
                detailService.updateWithCheck(deliveryDetailEntity);

                //推动订单子单完成
                OrderDetailEvent orderDetailEvent = new OrderDetailEvent(EventTopicEnum.ORDER_DETAIL,
                                                                         EventOperationEnum.DONE,
                                                                         orderDetail);
                StateMachineUtil.fire(orderDetailEvent, orderDetail.getStatus());
                logger.info("交接单子单{}已完成", deliveryDetailEntity.getId());
            default:
                break;
        }
    }
}
