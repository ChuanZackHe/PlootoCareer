package org.aiit.mes.order.domain.dao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.aiit.mes.common.exception.OperationFailedException;
import org.aiit.mes.common.exception.ResourceNotFoundException;
import org.aiit.mes.order.constant.OrderConstant;
import org.aiit.mes.order.constant.ResourceCategory;
import org.aiit.mes.order.domain.dao.entity.DeliverySummaryEntity;
import org.aiit.mes.order.domain.dao.entity.OrderDetailEntity;
import org.aiit.mes.order.domain.dao.entity.OrderSummaryEntity;
import org.aiit.mes.order.domain.dao.mapper.OrderSummaryMapper;
import org.aiit.mes.order.domain.dao.service.IOrderDetailService;
import org.aiit.mes.order.domain.dao.service.IOrderSummaryService;
import org.aiit.mes.order.domain.dto.order_summary.OrderOverviewQuery;
import org.aiit.mes.order.domain.dto.order_summary.OrderSummaryQuery;
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
public class OrderSummaryServiceImpl extends ServiceImpl<OrderSummaryMapper, OrderSummaryEntity>
        implements IOrderSummaryService {

    private static final Logger logger = LoggerFactory.getLogger(OrderSummaryServiceImpl.class);

    @Resource
    DeliverySummaryServiceImpl deliverySummaryService;

    @Resource
    IOrderDetailService orderDetailService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(String orderSummaryCode) {
        logger.info(String.format("开始级联删除订单总单%s", orderSummaryCode));
        logger.info("开始删除关联的订单子单");
        List<OrderDetailEntity> orderDetailEntities = orderDetailService.list(
                new LambdaQueryWrapper<OrderDetailEntity>().eq(OrderDetailEntity::getDocumentId,
                                                               orderSummaryCode));
        orderDetailService.removeByIds(orderDetailEntities.stream().map((x) -> x.getId()).collect(Collectors.toList()));

        logger.info("开始删除关联的交付单总单");
        List<DeliverySummaryEntity> deliverySummaryEntities = deliverySummaryService.list(
                new LambdaQueryWrapper<DeliverySummaryEntity>().eq(DeliverySummaryEntity::getOrderSummaryCode,
                                                                   orderSummaryCode));
        deliverySummaryService.deleteByIds(
                deliverySummaryEntities.stream().map((x) -> x.getDocumentId()).collect(Collectors.toList()));

        logger.info(String.format("开始删除订单总单%s", orderSummaryCode));
        if (!this.remove(
                new LambdaQueryWrapper<OrderSummaryEntity>().eq(OrderSummaryEntity::getDocumentId, orderSummaryCode))) {
            throw OperationFailedException.deleteFailed(ResourceCategory.ORDER_SUMMARY, "删除失败");
        }
    }

    @Override
    public Page<OrderSummaryEntity> listOrderInfo(OrderSummaryQuery queryCondition) {
        return this.page(queryCondition.toPage(), queryCondition.toLambdaQueryWrapper());
    }

    @Override
    public void updateWithCheck(OrderSummaryEntity toUpdate) {
        if (!this.updateById(toUpdate)) {
            throw OperationFailedException.updateFailed(OrderConstant.ORDER_SUMMARY, UPDATE_FAIL);
        }
    }

    @Override
    public void saveWithCheck(OrderSummaryEntity toSave) {
        if (!this.save(toSave)) {
            throw OperationFailedException.insertFailed(OrderConstant.ORDER_SUMMARY, INSERT_FAIL);
        }
    }

    @Override
    public OrderSummaryEntity getByDocumentId(String documentId) {
        QueryWrapper<OrderSummaryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(OrderSummaryEntity::getDocumentId, documentId);
        OrderSummaryEntity result = this.getOne(queryWrapper);
        if (Objects.isNull(result)) {
            throw ResourceNotFoundException.newExp(OrderConstant.ORDER_SUMMARY, documentId);
        }
        return result;
    }

    @Override
    public Page<OrderSummaryEntity> listOrders(OrderOverviewQuery orderOverviewQuery) {
        return this.page(orderOverviewQuery.toPage(), orderOverviewQuery.toLambdaQueryWrapper());
    }


}
