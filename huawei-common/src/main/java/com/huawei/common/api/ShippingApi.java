package com.huawei.common.api;

import com.huawei.common.entity.Shipping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @Auther: cuibeijie
 * @Date: 2019/6/21 23:23
 * @Description: 物流相关的api接口,供其他微服务调用
 */
public interface ShippingApi {
    /**
     * 查询登录用户下的所有物流地址
     * @return
     */
    @GetMapping("queryShipLists")
    List<Shipping> queryShipLists();

    /**
     * 根据id查询物流地址
     * @param id
     * @return
     */
    @GetMapping("shipping/{id}")
    Shipping queryShippingById(@PathVariable("id") Integer id);
}
