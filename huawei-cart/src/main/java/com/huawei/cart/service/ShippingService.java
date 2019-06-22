package com.huawei.cart.service;


import com.huawei.cart.pojo.Shipping;

import java.util.List;

/**
 * @Auther: cuibeijie
 * @Date: 2019/6/17 23:51
 * @Description: 物流地址接口层
 */
public interface ShippingService {
    //增加物流地址
    void addShipping(Shipping shipping);
    //查询物流列表
    List<Shipping> queryShipLists();
    //根据id查询物流
    Shipping queryShippingById(Integer id);
}
