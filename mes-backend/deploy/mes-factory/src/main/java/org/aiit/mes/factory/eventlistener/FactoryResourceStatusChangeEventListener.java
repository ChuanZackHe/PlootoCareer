package org.aiit.mes.factory.eventlistener;

import org.aiit.mes.common.eventbus.constant.EventTopicEnum;
import org.aiit.mes.common.eventbus.model.BaseEvent;
import org.aiit.mes.common.eventbus.processor.IEventBusProcessor;
import org.aiit.mes.factory.application.IFactoryApplication;
import org.aiit.mes.factory.domain.dto.FactoryResourceStatueChangeCmd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ：张少卿
 * @description：生产模块监听 入库完成
 * @date ：2022/1/11 10:37 上午
 */
public class FactoryResourceStatusChangeEventListener implements IEventBusProcessor<FactoryResourceStatueChangeCmd> {

    private static final Logger logger = LoggerFactory.getLogger(FactoryResourceStatusChangeEventListener.class);


    private IFactoryApplication factoryApplication;

    public FactoryResourceStatusChangeEventListener(
            IFactoryApplication factoryApplication) {this.factoryApplication = factoryApplication;}

    @Override
    public void asyncProcess(BaseEvent<FactoryResourceStatueChangeCmd> event) {
        logger.info("receive event:{}", event);
        switch (event.getOperationEnum()) {
            case RELEASE:
            case OCCUPY:
                factoryApplication.updateResourceStatus(event.getEventData());
                return;
            default:
                logger.error("event operation miss-match:{}, skip process", event.getOperationEnum());
                return;
        }
    }

    @Override
    public EventTopicEnum getEventTopic() {
        return EventTopicEnum.FACTORY_RESOURCE;
    }
}
