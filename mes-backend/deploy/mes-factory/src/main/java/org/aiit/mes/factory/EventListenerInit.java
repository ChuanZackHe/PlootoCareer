package org.aiit.mes.factory;

import org.aiit.mes.common.eventbus.EventBusUtil;
import org.aiit.mes.factory.application.IFactoryApplication;
import org.aiit.mes.factory.eventlistener.FactoryResourceStatusChangeEventListener;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName EventListenrInit
 * @Description
 * @createTime 2022.01.19 17:23
 */
@Component
public class EventListenerInit implements InitializingBean {

    @Resource
    IFactoryApplication factoryApplication;

    @Override
    public void afterPropertiesSet() throws Exception {
        EventBusUtil.getInstance().register(new FactoryResourceStatusChangeEventListener(factoryApplication));
    }
}
