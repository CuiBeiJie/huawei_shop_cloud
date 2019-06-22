package com.huawei.order.enums;

/**
 * @Auther: cuibeijie
 * @Date: 2019/6/22 00:36
 * @Description: 订单状态的枚举值
 */
public enum OrderStatusEnum {
    UN_PAY(1, "初始化，未付款"),
    PAY_UP(2, "已付款，未发货"),
    DELIVERED(3, "已发货，未确认"),
    SUCCESS(4, "已确认,未评价"),
    CLOSED(5, "已关闭"),
    RATED(6, "已评价，交易结束")
    ;
    private Integer code;
    private String desc;

    OrderStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public int value(){
        return this.code;
    }
}
