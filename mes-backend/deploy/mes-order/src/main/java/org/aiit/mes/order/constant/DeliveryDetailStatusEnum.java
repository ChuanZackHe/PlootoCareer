package org.aiit.mes.order.constant;

public enum DeliveryDetailStatusEnum {
    IDLE("空状态"),

    NEW("新建"),

    PROCESSING("进行中"),

    CLOSED("已关闭"),

    DONE("已完成");

    private String description;

    DeliveryDetailStatusEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}
