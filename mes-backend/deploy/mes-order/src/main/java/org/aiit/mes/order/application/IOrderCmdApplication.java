package org.aiit.mes.order.application;

import org.aiit.mes.order.domain.dto.delivery_detail.DeliveryDetailCloseCmd;
import org.aiit.mes.order.domain.dto.delivery_detail.DeliveryDetailCreateCmd;
import org.aiit.mes.order.domain.dto.delivery_detail.DeliveryDetailRepresent;
import org.aiit.mes.order.domain.dto.delivery_detail.DeliveryDetailUpdateCmd;
import org.aiit.mes.order.domain.dto.delivery_summary.DeliverySummaryCreateCmd;
import org.aiit.mes.order.domain.dto.delivery_summary.DeliverySummaryRepresent;
import org.aiit.mes.order.domain.dto.delivery_summary.DeliverySummaryUpdateCmd;
import org.aiit.mes.order.domain.dto.order_detail.*;
import org.aiit.mes.order.domain.dto.order_summary.OrderSummaryCreateCmd;
import org.aiit.mes.order.domain.dto.order_summary.OrderSummaryRepresent;
import org.aiit.mes.order.domain.dto.order_summary.OrderSummaryUpdateCmd;
import org.aiit.mes.warehouse.stockout.domain.dto.StockOutSummaryCreateCmd;

import java.util.Map;

public interface IOrderCmdApplication {

    OrderSummaryRepresent addOrderSummary(OrderSummaryCreateCmd orderSummaryCreateCmd);

    OrderDetailRepresent addOrderDetail(OrderDetailCreateCmd orderDetailCreateCmd);

    DeliverySummaryRepresent addDeliverySummary(DeliverySummaryCreateCmd deliverySummaryCreateCmd);

    DeliveryDetailRepresent addDeliveryDetail(DeliveryDetailCreateCmd deliveryDetailCreateCmd);

    OrderSummaryRepresent updateOrderSummary(OrderSummaryUpdateCmd orderSummaryCreateCmd);

    OrderDetailRepresent updateOrderDetail(OrderDetailUpdateCmd orderDetailUpdateCmd);

    DeliverySummaryRepresent updateDeliverySummary(DeliverySummaryUpdateCmd deliverySummaryUpdateCmd);

    DeliveryDetailRepresent updateDeliveryDetail(DeliveryDetailUpdateCmd deliveryDetailUpdateCmd);

    String deleteOrderSummary(String orderCode);

    Long deleteOrderDetail(Long detailId);

    String deleteDeliverySummary(String deliveryCode);

    Long deleteDeliveryDetail(Long detailId);

    String confirmOrderSummary(String orderCode);

    String confirmDeliverySummary(String deliveryCode);

    Long closeOrderDetail(OrderDetailCloseCmd detailCloseCmd);

    Long closeDeliveryDetail(DeliveryDetailCloseCmd deliveryDetailCloseCmd);

    Map<Long, Double> getCapacity(CapacityQuery capacityQuery);

    void createStockOut(StockOutSummaryCreateCmd createCmd);
}
