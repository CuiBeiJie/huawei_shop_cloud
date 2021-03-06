package com.huawei.order.service;

import com.huawei.order.dto.OrderDto;
import com.huawei.order.enums.PayStateEnum;
import com.huawei.order.pojo.Order;

import java.util.Map;

/**
 * @Auther: cuibeijie
 * @Date: 2019/6/21 21:40
 * @Description: 订单接口层
 */
public interface OrderService {
    //创建订单
    Long createOrder(OrderDto orderDto);
    //根据订单id查询订单详情
    Order queryById(Long orderId);
    //生成微信支付链接
    String createPayUrl(Long orderId);
    //校验回调结果
    void handleNotify(Map<String, String> msg);
    //查询订单状态
    PayStateEnum queryOrderStatus(Long orderId);
}
