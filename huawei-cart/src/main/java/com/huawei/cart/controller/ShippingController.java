package com.huawei.cart.controller;


import com.huawei.cart.pojo.Shipping;
import com.huawei.cart.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Auther: cuibeijie
 * @Date: 2019/6/17 23:49
 * @Description: 物流地址逻辑控制层
 */
@RestController
public class ShippingController {
    @Autowired
    private ShippingService shippingService;

    /**
     * 增加物流地址
     * @param shipping
     * @return
     */
    @PostMapping("addShipping")
    public ResponseEntity<Void> addShipping(Shipping shipping){
        shippingService.addShipping(shipping);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 查询登录用户下的所有物流地址
     * @return
     */
    @GetMapping("queryShipLists")
    public ResponseEntity<List<Shipping>> queryShipLists(){
        return ResponseEntity.ok(shippingService.queryShipLists());
    }

    /**
     * 根据id查询物流地址
     * @param id
     * @return
     */
    @GetMapping("shipping/{id}")
    public ResponseEntity<Shipping> queryShippingById(@PathVariable("id") Integer id){
        System.out.println("根据id查询物流地址==" + id);
        return ResponseEntity.ok(shippingService.queryShippingById(id));
    }
}
