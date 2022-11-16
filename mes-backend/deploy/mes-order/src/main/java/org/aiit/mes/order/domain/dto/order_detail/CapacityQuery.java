package org.aiit.mes.order.domain.dto.order_detail;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author ：张少卿
 * @description：TODO
 * @date ：2022/1/10 5:38 下午
 */
@Data
@ApiModel(description = "订单子单可以规划的数量查询")
@Builder
public class CapacityQuery {

    @ApiModelProperty(value = "id列表")
    List<Long> ids;

}
