package org.aiit.mes.factory.domain.event;

import org.aiit.mes.common.eventbus.constant.EventOperationEnum;
import org.aiit.mes.common.eventbus.constant.EventTopicEnum;
import org.aiit.mes.common.eventbus.model.BaseEvent;
import org.aiit.mes.factory.domain.dto.FactoryResourceStatueChangeCmd;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName FactoryResourceEvent
 * @Description
 * @createTime 2022.01.19 13:52
 */

public class FactoryResourceStatusChangeEvent extends BaseEvent<FactoryResourceStatueChangeCmd> {

    public FactoryResourceStatusChangeEvent(EventOperationEnum eventOperationEnum,
                                            FactoryResourceStatueChangeCmd eventData) {
        super(EventTopicEnum.FACTORY_RESOURCE, eventOperationEnum, eventData);
    }

    public static FactoryResourceStatusChangeEvent releaseEvent(FactoryResourceStatueChangeCmd data) {
        return new FactoryResourceStatusChangeEvent(EventOperationEnum.RELEASE, data);
    }

    public static FactoryResourceStatusChangeEvent occupyEvent(FactoryResourceStatueChangeCmd data) {
        return new FactoryResourceStatusChangeEvent(EventOperationEnum.OCCUPY, data);
    }
}
