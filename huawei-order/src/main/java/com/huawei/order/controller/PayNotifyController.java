package com.huawei.order.controller;

import com.huawei.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cuibeijie
 * @date 2019/06/23
 */
@RestController
@Slf4j
public class PayNotifyController {

    @Autowired
    private OrderService orderService;

    /**
     * 微信接收的成功回调
     * 微信返回的数据是xml数据，要配置消息转换器转成map接收，商户的应答结果也是xml数据格式
     * @param msg
     * @return
     */
    @PostMapping(value = "/wxpay/notify",produces = "application/xml")
    public ResponseEntity<Map<String,String>> payNotify(@RequestBody Map<String, String> msg) {
        //处理回调结果
        orderService.handleNotify(msg);
        log.info("[支付回调] 接收微信支付回调,结果；{}" ,msg);
        // 没有异常，则返回成功
//        也可以直接返回这种格式
//        String result = "<xml>\n" +
//                "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
//                "  <return_msg><![CDATA[OK]]></return_msg>\n" +
//                "</xml>";
        Map<String,String> result = new HashMap<>();
        result.put("return_code","SUCCESS");
        result.put("return_msg","OK");
        return ResponseEntity.ok(result);

    }
}
