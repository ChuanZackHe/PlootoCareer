package org.aiit.mes.order.domain.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aiit.mes.common.constant.CommonSummaryStatusEnum;
import org.aiit.mes.common.entity.BaseEntity;
import org.aiit.mes.common.util.PropertyCopyUtil;
import org.aiit.mes.order.domain.dto.delivery_summary.DeliverySummaryRepresent;

import java.sql.Date;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "order_delivery_summary")
public class DeliverySummaryEntity extends BaseEntity<DeliverySummaryEntity> {

    private String documentId;

    private CommonSummaryStatusEnum status;

    private Date deliveryDate;

    private String orderSummaryCode;

    private String description;

    @Override
    public DeliverySummaryRepresent toRepresent() {
        return PropertyCopyUtil.copyToClass(this, DeliverySummaryRepresent.class);
    }
}
