package org.aiit.mes.order.application;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.aiit.mes.order.domain.dao.entity.DeliveryDetailEntity;
import org.aiit.mes.order.domain.dao.entity.DeliverySummaryEntity;
import org.aiit.mes.order.domain.dao.entity.OrderDetailEntity;
import org.aiit.mes.order.domain.dao.entity.OrderSummaryEntity;
import org.aiit.mes.order.domain.dto.delivery_detail.DeliveryDetailQuery;
import org.aiit.mes.order.domain.dto.delivery_detail.DeliveryDetailRepresent;
import org.aiit.mes.order.domain.dto.delivery_summary.DeliverySummaryQuery;
import org.aiit.mes.order.domain.dto.order_detail.OrderDetailQuery;
import org.aiit.mes.order.domain.dto.order_detail.OrderDetailRepresent;
import org.aiit.mes.order.domain.dto.order_summary.OrderOverviewQuery;
import org.aiit.mes.order.domain.dto.order_summary.OrderOverviewRepresent;
import org.aiit.mes.order.domain.dto.order_summary.OrderSummaryQuery;

public interface IOrderQueryApplication {

    Page<OrderSummaryEntity> listOrderInfo(
            OrderSummaryQuery queryCondition);

    Page<OrderDetailRepresent> listOrderDetailInfo(
            OrderDetailQuery queryCondition);

    Page<DeliverySummaryEntity> listDeliveryInfo(
            DeliverySummaryQuery queryCondition);

    Page<DeliveryDetailRepresent> listDeliveryDetailInfo(
            DeliveryDetailQuery queryCondition);

    OrderSummaryEntity showOrder(String documentId);

    DeliverySummaryEntity showDeliveryInfo(String documentId);

    Page<OrderOverviewRepresent> listOrderOverview(OrderOverviewQuery orderOverviewQuery);
}
