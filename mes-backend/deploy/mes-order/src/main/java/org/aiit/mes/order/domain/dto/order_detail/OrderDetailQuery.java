package org.aiit.mes.order.domain.dto.order_detail;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.aiit.mes.common.page.BasePage;
import org.aiit.mes.order.domain.dao.entity.OrderDetailEntity;
import org.hibernate.validator.constraints.Length;

import java.util.Optional;

@Data
@ApiModel(description = "订单子单查询dto")
@Builder
public class OrderDetailQuery extends BasePage<OrderDetailEntity> {

    @ApiModelProperty(value = "订单总单编码", example = "104A")
    @Length(max = 255)
    @JsonProperty("document_id")
    private String documentId;

    @ApiModelProperty(value = "状态", example = "运行中")
    @Length(max = 64)
    private String status;

    @ApiModelProperty(value = "物料名称", example = "阀门")
    @Length(max = 64)
    @JsonProperty("material_name")
    private String materialName;

    @Override
    public LambdaQueryWrapper<OrderDetailEntity> toLambdaQueryWrapper() {
        LambdaQueryWrapper<OrderDetailEntity> wrapper = super.toLambdaQueryWrapper();
        Optional.ofNullable(this.getStatus()).ifPresent(status -> wrapper.eq(OrderDetailEntity::getStatus, status));
        Optional.ofNullable(this.getMaterialName()).ifPresent(
                materialName -> wrapper.like(OrderDetailEntity::getMaterialName, materialName));
        Optional.ofNullable(this.getDocumentId()).ifPresent(
                documentId -> wrapper.eq(OrderDetailEntity::getDocumentId, documentId));
        return wrapper;
    }
}
