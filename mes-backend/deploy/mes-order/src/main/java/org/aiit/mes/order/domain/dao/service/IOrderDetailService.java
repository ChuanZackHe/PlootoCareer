package org.aiit.mes.order.domain.dao.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.aiit.mes.order.domain.dao.entity.OrderDetailEntity;
import org.aiit.mes.order.domain.dto.order_detail.OrderDetailQuery;

import java.util.List;

public interface IOrderDetailService extends IService<OrderDetailEntity> {

    void saveWithCheck(OrderDetailEntity toSave);

    void updateWithCheck(OrderDetailEntity toUpdate);

    void delete(Long id);

    Page<OrderDetailEntity> listOrderDetailInfo(
            OrderDetailQuery queryCondition);

    List<OrderDetailEntity> listByDocumentId(String documentId);

    /**
     * 乐观锁更新已经规划的数量
     */
    void updateAllocatedCountCAS(OrderDetailEntity orderDetailEntity, Double count);

    boolean isDetailSatisfied(OrderDetailEntity orderDetailEntity);
}
