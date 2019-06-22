package com.huawei.order.client;

import com.huawei.common.api.ShippingApi;
import com.huawei.common.entity.Shipping;
import com.huawei.order.config.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Auther: cuibeijie
 * @Date: 2019/6/21 22:48
 * @Description: 物流client
 */
@FeignClient(value = "cart-service",configuration = FeignConfiguration.class)
public interface ShippingClient {

    @GetMapping("shipping/{id}")
    Shipping queryShippingById(@PathVariable("id") Integer id);
}
