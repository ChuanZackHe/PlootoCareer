package org.aiit.mes.order.constant;

public enum OrderDetailStatusEnum {
    IDLE("空状态"),

    NEW("新建"),

    TO_PLAN("可规划"),

    PLANED("规划完成"),

    DONE("已完成"),

    CLOSED("已关闭");

    private String description;

    OrderDetailStatusEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}
