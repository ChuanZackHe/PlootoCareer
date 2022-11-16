package org.aiit.mes.order.domain.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aiit.mes.common.entity.BaseEntity;
import org.aiit.mes.common.util.DoubleUtil;
import org.aiit.mes.common.util.PropertyCopyUtil;
import org.aiit.mes.order.constant.OrderDetailStatusEnum;
import org.aiit.mes.order.domain.dto.order_detail.OrderDetailRepresent;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "order_detail")
public class OrderDetailEntity extends BaseEntity<OrderDetailEntity> {

    private String documentId;

    private Long materialId;

    private String materialName;

    private Double price;

    public void setCount(Double count) {
        this.count = DoubleUtil.round(count);
    }

    public void setAllocatedCount(Double allocatedCount) {
        this.allocatedCount = DoubleUtil.round(allocatedCount);
    }

    private Double count;

    private Double allocatedCount;

    private String description;

    private OrderDetailStatusEnum status;

    private String userDefineInfo;

    @Override
    public OrderDetailRepresent toRepresent() {
        return PropertyCopyUtil.copyToClass(this, OrderDetailRepresent.class);
    }
}
