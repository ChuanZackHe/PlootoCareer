package org.aiit.mes.order.domain.dao.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.aiit.mes.order.domain.dao.entity.OrderSummaryEntity;
import org.aiit.mes.order.domain.dto.order_summary.OrderOverviewQuery;
import org.aiit.mes.order.domain.dto.order_summary.OrderSummaryQuery;

public interface IOrderSummaryService extends IService<OrderSummaryEntity> {

    void delete(String code);

    Page<OrderSummaryEntity> listOrderInfo(
            OrderSummaryQuery queryCondition);

    void updateWithCheck(OrderSummaryEntity toUpdate);

    void saveWithCheck(OrderSummaryEntity toSave);

    OrderSummaryEntity getByDocumentId(String documentId);

    Page<OrderSummaryEntity> listOrders(OrderOverviewQuery orderOverviewQuery);
}
