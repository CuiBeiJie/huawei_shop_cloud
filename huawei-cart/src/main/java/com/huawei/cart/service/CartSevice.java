package com.huawei.cart.service;

import com.huawei.cart.pojo.Cart;

import java.util.List;

/**
 * @Auther: cuibeijie
 * @Date: 2019/6/16 17:06
 * @Description: 购物车接口层
 */
public interface CartSevice {
    //添加购物车
    void addCart(Cart cart);
    //查询购物车列表
    List<Cart> queryCartList();
    //更改购物车中商品数量
    void updateNum(Long skuId, Integer num);
    //删除购物车中商品
    void deleteCart(String skuId);
    //批量删除购物车中商品
    void batchDeleteCart(List<Long> skuIds);
}
