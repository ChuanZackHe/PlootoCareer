package org.aiit.mes.order.domain.dao.mapper;

/**
 * @author ：张少卿
 * @description：TODO
 * @date ：2021/12/29 4:17 下午
 */
public class OrderCAS {

    /**
     * 记录的ID
     */
    private Long id;

    /**
     * 上一次的数量
     */
    private Double lastCount;

    /**
     * 更新的数量
     */
    private Double count;

    public OrderCAS(Long id, Double lastCount, Double count) {
        this.id = id;
        this.lastCount = lastCount;
        this.count = count;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLastCount() {
        return lastCount;
    }

    public void setLastCount(Double lastCount) {
        this.lastCount = lastCount;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }
}
