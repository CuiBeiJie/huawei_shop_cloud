package com.huawei.order.client;

import com.huawei.common.entity.Shipping;
import com.huawei.item.param.SpuParam;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @Auther: 25349
 * @Date: 2019/6/22 15:16
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsClientTest {

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private ShippingClient shippingClient;
    @Test
    public void testquerygoods(){
        SpuParam spuParam = goodsClient.querySpuById(2L);
        System.out.println(spuParam);
    }
    @Test
    public void testqueryShip(){
        Shipping shipping = shippingClient.queryShippingById(9);
        System.out.println(shipping);
    }
}