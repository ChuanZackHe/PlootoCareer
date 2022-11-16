package org.aiit.mes.order.domain.dto.delivery_detail;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.aiit.mes.common.page.BasePage;
import org.aiit.mes.order.constant.DeliveryDetailStatusEnum;
import org.aiit.mes.order.domain.dao.entity.DeliveryDetailEntity;
import org.hibernate.validator.constraints.Length;

import java.sql.Date;
import java.util.Optional;

@Data
@ApiModel(description = "交付子单查询dto")
public class DeliveryDetailQuery extends BasePage<DeliveryDetailEntity> {

    @ApiModelProperty(value = "交付总单编码", example = "104A")
    @Length(max = 255)
    @JsonProperty("document_id")
    private String documentId;

    @ApiModelProperty(value = "状态", example = "运行中")
    private DeliveryDetailStatusEnum status;

    @Override
    public LambdaQueryWrapper<DeliveryDetailEntity> toLambdaQueryWrapper() {
        LambdaQueryWrapper<DeliveryDetailEntity> wrapper = super.toLambdaQueryWrapper();
        Optional.ofNullable(this.getDocumentId()).ifPresent(
                materialId -> wrapper.eq(DeliveryDetailEntity::getDocumentId, documentId));
        Optional.ofNullable(this.getStatus()).ifPresent(status -> wrapper.eq(DeliveryDetailEntity::getStatus, status));
        return wrapper;
    }
}
