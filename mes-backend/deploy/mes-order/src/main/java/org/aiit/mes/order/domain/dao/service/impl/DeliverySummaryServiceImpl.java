package org.aiit.mes.order.domain.dao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.aiit.mes.common.exception.OperationFailedException;
import org.aiit.mes.common.exception.ResourceNotFoundException;
import org.aiit.mes.order.constant.OrderConstant;
import org.aiit.mes.order.constant.ResourceCategory;
import org.aiit.mes.order.domain.dao.entity.DeliveryDetailEntity;
import org.aiit.mes.order.domain.dao.entity.DeliverySummaryEntity;
import org.aiit.mes.order.domain.dao.mapper.DeliverySummaryMapper;
import org.aiit.mes.order.domain.dao.service.IDeliverySummaryService;
import org.aiit.mes.order.domain.dto.delivery_summary.DeliverySummaryQuery;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.aiit.mes.common.constant.Constants.INSERT_FAIL;
import static org.aiit.mes.common.constant.Constants.UPDATE_FAIL;

@Service
public class DeliverySummaryServiceImpl extends ServiceImpl<DeliverySummaryMapper, DeliverySummaryEntity>
        implements IDeliverySummaryService {

    private static final Logger logger = LoggerFactory.getLogger(DeliverySummaryServiceImpl.class);

    @Resource
    DeliveryDetailServiceImpl deliveryDetailService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(String deliverySummaryCode) {
        logger.info(String.format("开始级联删除交接单总单%s", deliverySummaryCode));

        logger.info("开始删除对应的交接子单");
        List<DeliveryDetailEntity> deliveryDetailEntities = deliveryDetailService.list(
                new QueryWrapper<>(DeliveryDetailEntity.builder().documentId(deliverySummaryCode).build()));
        deliveryDetailService.deleteByIds(
                deliveryDetailEntities.stream().map((x) -> x.getId()).collect(Collectors.toList()));

        logger.info(String.format("开始删除交接单总单%s", deliveryDetailEntities));
        if (!this.remove(new LambdaQueryWrapper<DeliverySummaryEntity>().eq(DeliverySummaryEntity::getDocumentId,
                                                                            deliverySummaryCode))) {
            throw OperationFailedException.deleteFailed(ResourceCategory.DELIVERY_SUMMARY, "删除失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteByIds(List<String> codes) {
        for (String curr : codes) {
            delete(curr);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Page<DeliverySummaryEntity> listDeliveryInfo(DeliverySummaryQuery queryCondition) {
        return this.page(queryCondition.toPage(), queryCondition.toLambdaQueryWrapper());
    }

    @Override
    public DeliverySummaryEntity getByDocumentId(String documentId) {
        QueryWrapper<DeliverySummaryEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("document_id", documentId);
        DeliverySummaryEntity summaryEntity = this.getOne(queryWrapper);
        if (Objects.isNull(summaryEntity)) {
            throw ResourceNotFoundException.newExp(OrderConstant.DELIVERY_SUMMARY, documentId);
        }
        return summaryEntity;
    }

    @Override
    public void saveWitchCheck(DeliverySummaryEntity toSave) {
        if (!this.save(toSave)) {
            throw OperationFailedException.insertFailed(OrderConstant.DELIVERY_SUMMARY, INSERT_FAIL);
        }
    }

    @Override
    public void updateWithcCheck(DeliverySummaryEntity toUdpate) {
        if (!this.updateById(toUdpate)) {
            throw OperationFailedException.updateFailed(OrderConstant.DELIVERY_SUMMARY, UPDATE_FAIL);
        }
    }

    @Override
    public List<DeliverySummaryEntity> listByOrderDocumentIds(List<String> orderDocumentIds) {
        if(CollectionUtils.isEmpty(orderDocumentIds)){
            return new ArrayList<>();
        }
        LambdaQueryWrapper<DeliverySummaryEntity> summaryEntityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        summaryEntityLambdaQueryWrapper.in(DeliverySummaryEntity::getOrderSummaryCode, orderDocumentIds);
        return this.list(summaryEntityLambdaQueryWrapper);
    }
}
