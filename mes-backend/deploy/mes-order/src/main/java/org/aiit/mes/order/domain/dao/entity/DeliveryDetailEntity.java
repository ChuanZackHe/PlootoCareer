package org.aiit.mes.order.domain.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aiit.mes.common.entity.BaseEntity;
import org.aiit.mes.common.util.DoubleUtil;
import org.aiit.mes.common.util.PropertyCopyUtil;
import org.aiit.mes.order.constant.DeliveryDetailStatusEnum;
import org.aiit.mes.order.domain.dto.delivery_detail.DeliveryDetailRepresent;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "order_delivery_detail")
public class DeliveryDetailEntity extends BaseEntity<DeliveryDetailEntity> {

    private Long orderDetailId;

    private String documentId;

    private Long materialId;

    public void setCount(Double count) {
        this.count = DoubleUtil.round(count);
    }

    private Double count;

    private DeliveryDetailStatusEnum status;

    private String materialName;

    private String description;

    private String userDefineInfo;

    @Override
    public DeliveryDetailRepresent toRepresent() {
        DeliveryDetailRepresent deliveryDetailRepresent = PropertyCopyUtil.copyToClass(this,
                                                                                       DeliveryDetailRepresent.class);
//        deliveryDetailRepresent.setStockCount(OrderUtils.stockQueryApplication.listStockSummarys(new
//        LinkedList<Long>(){{add(materialId);}}).get(0).getCount());
        return deliveryDetailRepresent;
    }
}
