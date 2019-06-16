package com.huawei.cart.controller;

import com.huawei.cart.pojo.Cart;
import com.huawei.cart.service.CartSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: cuibeijie
 * @Date: 2019/6/16 17:01
 * @Description: 购物车逻辑控制层
 */
@RestController
public class CartController {
    @Autowired
    private CartSevice cartSevice;
    /**
     * 新增购物车
     * @param cart
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> addCart(@RequestBody Cart cart){
        cartSevice.addCart(cart);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    /**
     * 查询购物车列表
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<List<Cart>> queryCartList() {
        return ResponseEntity.ok(cartSevice.queryCartList());
    }

    /**
     * 更改购物车中商品数量
     * @param skuId
     * @param num
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateNum(@RequestParam("skuId") Long skuId,
                                          @RequestParam("num") Integer num) {
        cartSevice.updateNum(skuId, num);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 删除购物车中商品
     * @param skuId
     * @return
     */
    @DeleteMapping("{skuId}")
    public ResponseEntity<Void> deleteCart(@PathVariable("skuId") String skuId) {
        cartSevice.deleteCart(skuId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 批量删除购物车中商品
     * @param skuIds
     * @return
     */
    @DeleteMapping("/batchDeleteCart")
    public ResponseEntity<Void> batchDeleteCart(@RequestParam("ids") List<Long> skuIds){
        cartSevice.batchDeleteCart(skuIds);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
