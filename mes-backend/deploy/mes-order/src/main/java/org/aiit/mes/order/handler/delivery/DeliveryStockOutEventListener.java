package org.aiit.mes.order.handler.delivery;

import org.aiit.mes.common.eventbus.constant.EventOperationEnum;
import org.aiit.mes.common.eventbus.constant.EventTopicEnum;
import org.aiit.mes.common.eventbus.model.BaseEvent;
import org.aiit.mes.common.eventbus.processor.IEventBusProcessor;
import org.aiit.mes.common.statemachine.StateMachineUtil;
import org.aiit.mes.order.domain.dao.entity.DeliverySummaryEntity;
import org.aiit.mes.order.domain.dao.service.IDeliverySummaryService;
import org.aiit.mes.warehouse.stockout.constant.StockOutTypeEnum;
import org.aiit.mes.warehouse.stockout.domain.dao.entity.StockOutSummaryEntity;

import java.util.Objects;

/**
 * @author ：张少卿
 * @description：交付单监听 出库完成的事件
 * @date ：2021/12/29 4:42 下午
 */
public class DeliveryStockOutEventListener implements IEventBusProcessor {

    private IDeliverySummaryService deliverySummaryService;

    public DeliveryStockOutEventListener(
            IDeliverySummaryService deliverySummaryService) {
        this.deliverySummaryService = deliverySummaryService;
    }

    @Override
    public void asyncProcess(BaseEvent event) {
        EventOperationEnum operationEnum = event.getOperationEnum();
        //只监听出库完成的操作
        if (Objects.equals(operationEnum, EventOperationEnum.DONE)) {
            StockOutSummaryEntity summaryEntity = (StockOutSummaryEntity) event.getEventData();
            //如果是交付出库才行
            if (Objects.equals(summaryEntity.getType(), StockOutTypeEnum.DELIVERY)) {
                //获取交付单的ID
                String documentId = summaryEntity.getDeliveryId();
                DeliverySummaryEntity deliverySummaryEntity = deliverySummaryService.getByDocumentId(documentId);
                DeliverySummaryEvent baseEvent = new DeliverySummaryEvent(EventTopicEnum.DELIVERY_SUMMARY, EventOperationEnum.DONE, deliverySummaryEntity);
                StateMachineUtil.fire(baseEvent, deliverySummaryEntity.getStatus());
            }
        }
    }

    @Override
    public EventTopicEnum getEventTopic() {
        return EventTopicEnum.STOCK_OUT_SUMMARY;
    }
}
