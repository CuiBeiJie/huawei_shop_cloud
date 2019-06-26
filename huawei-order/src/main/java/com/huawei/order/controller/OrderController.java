package com.huawei.order.controller;
import com.huawei.order.dto.OrderDto;
import com.huawei.order.pojo.Order;
import com.huawei.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Auther: cuibeijie
 * @Date: 2019/6/21 21:24
 * @Description: 订单逻辑控制层
 */
@RestController
@RequestMapping("order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    /**
     * 创建订单
     * @param orderDto
     * @return
     */
    @PostMapping("createOrder")
    public ResponseEntity<Long> createOrder(@RequestBody @Valid OrderDto orderDto){
        //创建订单
         return ResponseEntity.ok(orderService.createOrder(orderDto));
    }

    /**
     * 根据订单ID查询订单详情
     *
     * @param orderId
     * @return
     */
    @GetMapping("{id}")
    public ResponseEntity<Order> queryOrderById(@PathVariable("id") Long orderId) {
        return ResponseEntity.ok(orderService.queryById(orderId));
    }

    /**
     * 生成微信支付链接
     *
     * @param orderId
     * @return
     */
    @GetMapping("url/{id}")
    public ResponseEntity<String> createPayUrl(@PathVariable("id") Long orderId) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.createPayUrl(orderId));
    }
    @GetMapping("state/{id}")
    public ResponseEntity<Integer> queryOrderState(@PathVariable("id") Long orderId){
        return ResponseEntity.ok(orderService.queryOrderStatus(orderId).getValue());
    }
}
