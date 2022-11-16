package org.aiit.mes.order.domain.dao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.aiit.mes.common.eventbus.constant.EventOperationEnum;
import org.aiit.mes.common.eventbus.constant.EventTopicEnum;
import org.aiit.mes.common.exception.OperationFailedException;
import org.aiit.mes.common.statemachine.StateMachineUtil;
import org.aiit.mes.order.constant.OrderConstant;
import org.aiit.mes.order.constant.ResourceCategory;
import org.aiit.mes.order.domain.dao.entity.DeliveryDetailEntity;
import org.aiit.mes.order.domain.dao.entity.DeliverySummaryEntity;
import org.aiit.mes.order.domain.dao.mapper.DeliveryDetailMapper;
import org.aiit.mes.order.domain.dao.service.IDeliveryDetailService;
import org.aiit.mes.order.domain.dao.service.IDeliverySummaryService;
import org.aiit.mes.order.domain.dto.delivery_detail.DeliveryDetailQuery;
import org.aiit.mes.order.handler.delivery.DeliverySummaryEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

import static org.aiit.mes.common.constant.Constants.INSERT_FAIL;
import static org.aiit.mes.common.constant.Constants.UPDATE_FAIL;


@Service
public class DeliveryDetailServiceImpl extends ServiceImpl<DeliveryDetailMapper, DeliveryDetailEntity>
        implements IDeliveryDetailService {

    private static final Logger logger = LoggerFactory.getLogger(DeliverySummaryServiceImpl.class);

    @Resource
    IDeliverySummaryService deliverySummaryService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Long deliveryDetailId) {
        logger.info(String.format("开始删除交接单子单%s", deliveryDetailId));
        DeliveryDetailEntity deliveryDetailEntity = getById(deliveryDetailId);
        if (!this.remove(
                new LambdaQueryWrapper<DeliveryDetailEntity>().eq(DeliveryDetailEntity::getId, deliveryDetailId))) {
            throw OperationFailedException.deleteFailed(ResourceCategory.DELIVERY_SUMMARY, "删除失败");
        }
        DeliverySummaryEntity deliverySummaryEntity = deliverySummaryService.getByDocumentId(
                deliveryDetailEntity.getDocumentId());
        DeliverySummaryEvent event = new DeliverySummaryEvent(EventTopicEnum.DELIVERY_SUMMARY, EventOperationEnum.UNBIND,
                                                   deliverySummaryEntity);
        StateMachineUtil.fire(event, deliverySummaryEntity.getStatus());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteByIds(List<Long> ids) {
        for (Long id : ids) {
            delete(id);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Page<DeliveryDetailEntity> listDeliveryDetailInfo(DeliveryDetailQuery queryCondition) {
        return this.page(queryCondition.toPage(), queryCondition.toLambdaQueryWrapper());
    }

    @Override
    public List<DeliveryDetailEntity> listByDocumentId(String documentId) {
        return list(new LambdaQueryWrapper<DeliveryDetailEntity>().eq(DeliveryDetailEntity::getDocumentId, documentId));
    }

    @Override
    public void saveWithCheck(DeliveryDetailEntity toSave) {
        if (!this.save(toSave)) {
            throw OperationFailedException.insertFailed(OrderConstant.DELIVERY_DETAIL, INSERT_FAIL);
        }
    }

    @Override
    public void updateWithCheck(DeliveryDetailEntity toUpdate) {
        if (!this.updateById(toUpdate)) {
            throw OperationFailedException.updateFailed(OrderConstant.DELIVERY_DETAIL, UPDATE_FAIL);
        }
    }

    @Override
    public List<DeliveryDetailEntity> listByOrderDetailId(Long orderDetailId) {
        return list(new LambdaQueryWrapper<DeliveryDetailEntity>().eq(DeliveryDetailEntity::getOrderDetailId,
                                                                      orderDetailId));
    }
}
