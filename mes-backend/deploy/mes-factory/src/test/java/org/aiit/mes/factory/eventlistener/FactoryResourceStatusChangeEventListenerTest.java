package org.aiit.mes.factory.eventlistener;

import lombok.extern.slf4j.Slf4j;
import org.aiit.mes.common.constant.DataStateEnum;
import org.aiit.mes.common.constant.FactoryResourceTypeEnum;
import org.aiit.mes.common.eventbus.EventBusUtil;
import org.aiit.mes.factory.domain.dao.entity.FactoryResourceInfo;
import org.aiit.mes.factory.domain.dao.service.IFactoryResourceService;
import org.aiit.mes.factory.domain.dto.FactoryResourceStatueChangeCmd;
import org.aiit.mes.factory.domain.event.FactoryResourceStatusChangeEvent;
import org.aiit.mes.factory.domain.repository.FactoryPlotRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;

import static org.aiit.mes.common.constant.DataStateEnum.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author heyu
 * @version 1.0.0
 * @ClassName FactoryResourceStatusChangeEventListenerTest
 * @Description
 * @createTime 2022.01.19 17:30
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@Slf4j
class FactoryResourceStatusChangeEventListenerTest {

    @Resource
    IFactoryResourceService factoryResourceService;

    @Resource
    FactoryPlotRepository factoryPlotRepository;

    private FactoryResourceInfo machineInfo;

    @Test
    void testTriggerEvent() throws InterruptedException {
        // 触发 工厂资源占用事件，到工厂
        assertEquals(ACTIVE, factoryResourceService.getById(machineInfo.getId()).getState());
        EventBusUtil.getInstance().postAsync(
                FactoryResourceStatusChangeEvent.occupyEvent(
                        FactoryResourceStatueChangeCmd.builder()
                                                      .resourceCode(machineInfo.getCode())
                                                      .targetStatus(BUSY)
                                                      .message(String.format("%s:%s", "111111111",
                                                                             "2222222222"))
                                                      .build()));

        Thread.sleep(10);
        assertEquals(BUSY, factoryResourceService.getById(machineInfo.getId()).getState());

        // 触发 工厂资源释放事件，到工厂
        EventBusUtil.getInstance().postAsync(
                FactoryResourceStatusChangeEvent.releaseEvent(
                        FactoryResourceStatueChangeCmd.builder()
                                                      .resourceCode(machineInfo.getCode())
                                                      .targetStatus(IDLE).build()));
        assertEquals(BUSY, factoryResourceService.getById(machineInfo.getId()).getState());
        Thread.sleep(10);

        // 触发 工厂资源占用事件，到工厂
        EventBusUtil.getInstance().postAsync(
                FactoryResourceStatusChangeEvent.occupyEvent(
                        FactoryResourceStatueChangeCmd.builder()
                                                      .resourceCode(machineInfo.getCode())
                                                      .targetStatus(BUSY)
                                                      .message(String.format("%s:%s", "111111111",
                                                                             "2222222222"))
                                                      .build()));
        Thread.sleep(10);
    }

    @BeforeEach
    void setUp() {
        machineInfo = FactoryResourceInfo.builder()
                                         .code("JQ99999999")
                                         .parentCode("ppppp")
                                         .ancestorCode("aaaaaaaaaa")
                                         .name("测试机器1")
                                         .type(FactoryResourceTypeEnum.MACHINE.getType())
                                         .state(DataStateEnum.ACTIVE)
                                         .xRel(15)
                                         .yRel(-5)
                                         .build();
        factoryResourceService.save(machineInfo);
        FactoryResourceInfo ret = factoryPlotRepository.showResource(machineInfo.getCode());
        assertNotNull(ret);
    }

    @AfterEach
    void tearDown() {
        factoryResourceService.removeById(machineInfo.getId());
    }
}