package org.aiit.mes.order.domain.dao.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.aiit.mes.order.domain.dao.entity.DeliveryDetailEntity;
import org.aiit.mes.order.domain.dto.delivery_detail.DeliveryDetailQuery;

import java.util.List;

public interface IDeliveryDetailService extends IService<DeliveryDetailEntity> {

    void delete(Long id);

    void deleteByIds(List<Long> ids);

    Page<DeliveryDetailEntity> listDeliveryDetailInfo(
            DeliveryDetailQuery queryCondition);

    List<DeliveryDetailEntity> listByDocumentId(String documentId);

    void saveWithCheck(DeliveryDetailEntity toSave);

    void updateWithCheck(DeliveryDetailEntity toUpdate);

    List<DeliveryDetailEntity> listByOrderDetailId(Long orderDetailId);
}
