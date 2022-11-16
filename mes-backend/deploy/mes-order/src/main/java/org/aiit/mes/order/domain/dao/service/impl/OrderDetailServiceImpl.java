package org.aiit.mes.order.domain.dao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.aiit.mes.common.constant.CommonSummaryStatusEnum;
import org.aiit.mes.common.eventbus.constant.EventOperationEnum;
import org.aiit.mes.common.eventbus.constant.EventTopicEnum;
import org.aiit.mes.common.exception.OperationFailedException;
import org.aiit.mes.common.exception.ParamInvalidException;
import org.aiit.mes.common.statemachine.StateMachineUtil;
import org.aiit.mes.common.util.DoubleUtil;
import org.aiit.mes.order.constant.DeliveryDetailStatusEnum;
import org.aiit.mes.order.constant.OrderConstant;
import org.aiit.mes.order.constant.ResourceCategory;
import org.aiit.mes.order.domain.dao.entity.DeliveryDetailEntity;
import org.aiit.mes.order.domain.dao.entity.OrderDetailEntity;
import org.aiit.mes.order.domain.dao.entity.OrderSummaryEntity;
import org.aiit.mes.order.domain.dao.mapper.OrderCAS;
import org.aiit.mes.order.domain.dao.mapper.OrderDetailMapper;
import org.aiit.mes.order.domain.dao.service.IOrderDetailService;
import org.aiit.mes.order.domain.dao.service.IOrderSummaryService;
import org.aiit.mes.order.domain.dto.order_detail.OrderDetailQuery;
import org.aiit.mes.order.handler.order.OrderDetailEvent;
import org.aiit.mes.order.handler.order.OrderSummaryEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.aiit.mes.common.constant.Constants.INSERT_FAIL;
import static org.aiit.mes.common.constant.Constants.UPDATE_FAIL;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetailEntity>
        implements IOrderDetailService {

    private static final Logger logger = LoggerFactory.getLogger(OrderSummaryServiceImpl.class);

    @Resource
    private DeliveryDetailServiceImpl deliveryDetailService;

    @Resource
    private IOrderSummaryService orderSummaryService;

    @Resource
    private OrderDetailMapper orderDetailMapper;

    @Override
    public void saveWithCheck(OrderDetailEntity toSave) {
        if (!this.save(toSave)) {
            throw OperationFailedException.insertFailed(OrderConstant.ORDER_DETAIL, INSERT_FAIL);
        }
    }

    @Override
    public void updateWithCheck(OrderDetailEntity toUpdate) {
        if (!this.updateById(toUpdate)) {
            throw OperationFailedException.insertFailed(OrderConstant.ORDER_DETAIL, UPDATE_FAIL);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Long orderDetailId) {
        logger.info(String.format("开始级联删除订单子单%s", orderDetailId));
        OrderDetailEntity orderDetailEntity = getById(orderDetailId);
        OrderSummaryEntity orderSummaryEntity = orderSummaryService.getByDocumentId(orderDetailEntity.getDocumentId());
        if(!Objects.equals(orderSummaryEntity.getStatus(), CommonSummaryStatusEnum.TO_CONFIRM)){
            throw new ParamInvalidException("订单已经确认，不能在进行删除");
        }

        logger.info("开始删除对应的交接子单");
        List<DeliveryDetailEntity> deliveryDetailEntities = deliveryDetailService.list(
                new QueryWrapper<>(DeliveryDetailEntity.builder().orderDetailId(orderDetailId).build()));
        deliveryDetailService.deleteByIds(
                deliveryDetailEntities.stream().map((x) -> x.getId()).collect(Collectors.toList()));


        logger.info(String.format("开始删除订单子单%s", orderDetailId));
        if (!this.remove(new LambdaQueryWrapper<OrderDetailEntity>().eq(OrderDetailEntity::getId, orderDetailId))) {
            throw OperationFailedException.deleteFailed(ResourceCategory.ORDER_DETAIL, "删除失败");
        }

        OrderSummaryEvent event = new OrderSummaryEvent(EventTopicEnum.ORDER_SUMMARY, EventOperationEnum.UNBIND, orderSummaryEntity);
        StateMachineUtil.fire(event, orderSummaryEntity.getStatus());
    }

    @Override
    public Page<OrderDetailEntity> listOrderDetailInfo(OrderDetailQuery queryCondition) {
        return this.page(queryCondition.toPage(), queryCondition.toLambdaQueryWrapper());
    }

    @Override
    public List<OrderDetailEntity> listByDocumentId(String documentId) {
        return this.list(new LambdaQueryWrapper<OrderDetailEntity>().eq(OrderDetailEntity::getDocumentId, documentId));
    }


    @Override
    public void updateAllocatedCountCAS(OrderDetailEntity orderDetailEntity, Double count) {
        if (DoubleUtil.gt(DoubleUtil.add(orderDetailEntity.getAllocatedCount(), count),
                          orderDetailEntity.getCount())) {
            throw OperationFailedException.updateFailed(ResourceCategory.ORDER_DETAIL, "无法超额分配订单子单");
        }
        double sum = DoubleUtil.add(orderDetailEntity.getAllocatedCount(), count);
        if (orderDetailMapper.updateAllocatedCountCAS(new OrderCAS(orderDetailEntity.getId(),
                                                                   orderDetailEntity.getAllocatedCount(), sum)) != 1) {
            throw OperationFailedException.updateFailed(OrderConstant.ORDER_DETAIL, UPDATE_FAIL);
        }
        OrderDetailEntity orderDetailEntity1 = this.getById(orderDetailEntity.getId());
        if (DoubleUtil.eq(orderDetailEntity1.getAllocatedCount(), orderDetailEntity1.getCount())) {
            OrderDetailEvent event = new OrderDetailEvent(EventTopicEnum.ORDER_DETAIL, EventOperationEnum.PLAN,
                                                          orderDetailEntity1);
            StateMachineUtil.fire(event, orderDetailEntity1.getStatus());
        }
    }

    @Override
    public boolean isDetailSatisfied(OrderDetailEntity orderDetailEntity) {
        //查询该订单子单绑定的交付子单
        List<DeliveryDetailEntity> deliveryDetailEntities =
                deliveryDetailService.listByOrderDetailId(orderDetailEntity.getId());
        Double actualCount = DoubleUtil.sum(deliveryDetailEntities.stream().filter(t->Objects.equals(
                DeliveryDetailStatusEnum.DONE, t.getStatus())).map(DeliveryDetailEntity::getCount).collect(Collectors.toList()));
        return actualCount >= orderDetailEntity.getCount();
    }
}
