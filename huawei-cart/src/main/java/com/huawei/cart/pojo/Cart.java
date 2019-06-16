package com.huawei.cart.pojo;

import lombok.Data;

/**
 * @Auther: cuibeijie
 * @Date: 2019/6/16 17:00
 * @Description: 购物车实体类
 */
@Data
public class Cart {
    //商品id
    private Long skuId;

    //商品标题
    private String title;

    //购买数量
    private Integer num;

    //商品图片
    private String image;

    //加入购物车时商品的价格
    private Long price;

    //商品的规格参数
    private String ownSpec;
}
