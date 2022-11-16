package org.aiit.mes.order.domain.dao.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.aiit.mes.order.domain.dao.entity.DeliverySummaryEntity;
import org.aiit.mes.order.domain.dto.delivery_summary.DeliverySummaryQuery;

import java.util.List;

public interface IDeliverySummaryService extends IService<DeliverySummaryEntity> {

    void delete(String code);

    void deleteByIds(List<String> codes);

    Page<DeliverySummaryEntity> listDeliveryInfo(
            DeliverySummaryQuery queryCondition);

    DeliverySummaryEntity getByDocumentId(String deliverySummaryCode);

    void saveWitchCheck(DeliverySummaryEntity toSave);

    void updateWithcCheck(DeliverySummaryEntity toUdpate);

    List<DeliverySummaryEntity> listByOrderDocumentIds(List<String> orderDocumentIds);
}
