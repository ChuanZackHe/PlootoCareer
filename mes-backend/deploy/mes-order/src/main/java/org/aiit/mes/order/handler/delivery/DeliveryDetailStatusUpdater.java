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
            throw new ParamInvalidException("?????????????????????"+deliveryDetailEntity.getOrderDetailId());
        }
        switch (operationEnum) {
            case CREATE:
                logger.info("???????????????????????????");
                if (deliveryDetailEntity.getCount() <= 0) {
                    throw OperationFailedException.insertFailed(ResourceCategory.DELIVERY_DETAIL, "??????????????????????????????");
                }
                DeliverySummaryEntity deliverySummaryEntity = summaryService.getByDocumentId(
                        deliveryDetailEntity.getDocumentId());
                if (!ObjectUtil.equal(deliverySummaryEntity.getStatus(), CommonSummaryStatusEnum.NEW) &&
                        !ObjectUtil.equal(deliverySummaryEntity.getStatus(), CommonSummaryStatusEnum.TO_CONFIRM)) {
                    throw OperationFailedException.insertFailed(ResourceCategory.DELIVERY_DETAIL, "?????????????????????????????????");
                }

                deliveryDetailEntity.setUserDefineInfo(orderDetail.getUserDefineInfo());
                deliveryDetailEntity.setStatus(nextStatus);
                detailService.saveWithCheck(deliveryDetailEntity);
                DeliverySummaryEvent bindEvent = new DeliverySummaryEvent(EventTopicEnum.DELIVERY_SUMMARY, EventOperationEnum.BIND,
                                                    deliverySummaryEntity);
                StateMachineUtil.fire(bindEvent, deliverySummaryEntity.getStatus());
                logger.info("???????????????????????????");
                break;
            case CONFIRM:
                logger.info("???????????????????????????{}", deliveryDetailEntity.getId());
                //????????????????????????????????????????????????????????????
                //???????????????????????????
                OrderDetailEntity orderDetailEntity = orderDetailSevice.getById(
                        deliveryDetailEntity.getOrderDetailId());
                BigDecimal result =
                        BigDecimal.valueOf(orderDetailEntity.getCount()).subtract(BigDecimal.valueOf(orderDetailEntity.getAllocatedCount()).subtract(BigDecimal.valueOf(deliveryDetailEntity.getCount())));
                Assert.isTrue(result.doubleValue()>=0, "???????????????????????????????????????????????????");
                deliveryDetailEntity.setStatus(nextStatus);
                detailService.updateWithCheck(deliveryDetailEntity);
                logger.info("?????????????????????{}??????", deliveryDetailEntity.getId());
                //????????????????????????CAS??????????????????
                orderDetailSevice.updateAllocatedCountCAS(orderDetailEntity, deliveryDetailEntity.getCount());
                break;
            case CLOSE:
                logger.info("???????????????????????????{}", deliveryDetailEntity.getId());
                //????????????????????????????????????-?????????????????????????????????????????????

                //????????????
                deliveryDetailEntity.setStatus(nextStatus);
                detailService.updateWithCheck(deliveryDetailEntity);
                //CAS??????
                orderDetailSevice.updateAllocatedCountCAS(orderDetail, -deliveryDetailEntity.getCount());
                OrderDetailEvent event1 = new OrderDetailEvent(EventTopicEnum.ORDER_DETAIL, EventOperationEnum.UNPLAN,
                                                               orderDetail);
                StateMachineUtil.fire(event1, orderDetail.getStatus());
                logger.info("?????????????????????{}??????", deliveryDetailEntity.getId());
                break;
            case DONE:
                logger.info("???????????????????????????{}", deliveryDetailEntity.getId());
                deliveryDetailEntity.setStatus(nextStatus);
                detailService.updateWithCheck(deliveryDetailEntity);

                //????????????????????????
                OrderDetailEvent orderDetailEvent = new OrderDetailEvent(EventTopicEnum.ORDER_DETAIL,
                                                                         EventOperationEnum.DONE,
                                                                         orderDetail);
                StateMachineUtil.fire(orderDetailEvent, orderDetail.getStatus());
                logger.info("???????????????{}?????????", deliveryDetailEntity.getId());
            default:
                break;
        }
    }
}
