package org.aiit.mes.order.handler.delivery;

import org.aiit.mes.common.eventbus.constant.EventOperationEnum;
import org.aiit.mes.common.eventbus.constant.EventTopicEnum;
import org.aiit.mes.common.eventbus.model.BaseEvent;
import org.aiit.mes.order.domain.dao.entity.DeliveryDetailEntity;
import org.aiit.mes.order.domain.dao.entity.OrderDetailEntity;

/**
 * @author ：张少卿
 * @description：TODO
 * @date ：2022/1/11 10:46 上午
 */
public class DeliveryDetailEvent extends BaseEvent<DeliveryDetailEntity> {

    public DeliveryDetailEvent(EventTopicEnum eventTopic, EventOperationEnum operationEnum,
                               DeliveryDetailEntity eventData) {
        super(eventTopic, operationEnum, eventData);
    }
}
