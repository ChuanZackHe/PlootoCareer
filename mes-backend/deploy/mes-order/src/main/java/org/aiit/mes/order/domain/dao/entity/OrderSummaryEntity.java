package org.aiit.mes.order.domain.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aiit.mes.common.constant.CommonSummaryStatusEnum;
import org.aiit.mes.common.entity.BaseEntity;
import org.aiit.mes.common.util.PropertyCopyUtil;
import org.aiit.mes.order.domain.dto.order_summary.OrderOverviewRepresent;
import org.aiit.mes.order.domain.dto.order_summary.OrderSummaryRepresent;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "order_summary")
public class OrderSummaryEntity extends BaseEntity<OrderSummaryEntity> {

    private String documentId;

    private Long customerId;

    private String customerName;

    private Double price;

    private Date closingDate;

    private CommonSummaryStatusEnum status;

    private String description;

    @Override
    public OrderSummaryRepresent toRepresent() {
        return PropertyCopyUtil.copyToClass(this, OrderSummaryRepresent.class);
    }

    public OrderOverviewRepresent toOverview(){
        return PropertyCopyUtil.copyToClass(this, OrderOverviewRepresent.class);
    }
}
