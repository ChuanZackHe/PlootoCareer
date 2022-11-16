package org.aiit.mes.order.domain.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.aiit.mes.order.domain.dao.entity.OrderDetailEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetailEntity> {

    /**
     * CAS更新已规划的数量
     *
     * @return
     */
    int updateAllocatedCountCAS(OrderCAS orderCAS);
}
