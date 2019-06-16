package com.huawei.cart.service.impl;
import com.huawei.auth.entity.UserInfo;
import com.huawei.cart.interceptor.UserInterCeptor;
import com.huawei.cart.pojo.Cart;
import com.huawei.cart.service.CartSevice;
import com.huawei.common.enums.ExceptionEnums;
import com.huawei.common.exception.SelfException;
import com.huawei.common.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: cuibeijie
 * @Date: 2019/6/16 17:07
 * @Description: 购物车接口实现层
 */
@Service
public class CartServiceImpl implements CartSevice {
    private static final String KEY_PREFIX = "huawei:cart:uid:";
    @Autowired
    private StringRedisTemplate redisTemplate;
    /**
     * 添加购物车到redis中
     * @param cart
     */
    public void addCart(Cart cart) {
        //获取登录用户
        UserInfo loginUser = UserInterCeptor.getLoginUser();
        //生成key
        String key = KEY_PREFIX + loginUser.getId();
        //获取商品ID
        String hashKey = cart.getSkuId().toString();
        //获取数量
        Integer num = cart.getNum();
        //获取hash操作的对象
        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(key);
        if (hashOps.hasKey(hashKey)) {
            //Redis中有该商品，修改数量
            String  jsonCart= hashOps.get(hashKey).toString();
            cart = JsonUtils.parse(jsonCart, Cart.class);
            cart.setNum(cart.getNum() + num);
       }
        //存入Redis中
        hashOps.put(hashKey, JsonUtils.serialize(cart));
    }

    /**
     * 查询购物车列表
     * @return
     */
    public List<Cart> queryCartList() {
        //获取登录用户
        UserInfo loginUser = UserInterCeptor.getLoginUser();

        //获取该用户Redis中的key
        String key = KEY_PREFIX + loginUser.getId();

        if (!redisTemplate.hasKey(key)) {
            //Redis中没有给用户信息
            return null;
        }
        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(key);

        List<Object> carts = hashOps.values();

        if (CollectionUtils.isEmpty(carts)) {
            //购物车中无数据
            return null;
        }
        return carts.stream().map(s -> JsonUtils.parse(s.toString(), Cart.class)).collect(Collectors.toList());
    }

    /**
     * 更改购物车中商品数量
     * @param skuId
     * @param num
     */
    public void updateNum(Long skuId, Integer num) {
        //获取登录用户
        UserInfo loginUser = UserInterCeptor.getLoginUser();
        //生成key
        String key = KEY_PREFIX + loginUser.getId();
        //hashKey
        String hashKey = skuId.toString();
        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(key);
        if (!hashOps.hasKey(hashKey)) {
            //购物车中该商品不存在
            throw new SelfException(ExceptionEnums.CART_NOT_FOUND);
        }
        //查询购物车商品
        Cart cart = JsonUtils.parse(hashOps.get(hashKey).toString(), Cart.class);
        //修改数量
        cart.setNum(num);
        //写回redis
        hashOps.put(hashKey,JsonUtils.serialize(cart));
    }

    /**
     * 删除购物车中商品
     * @param skuId
     */
    public void deleteCart(String skuId) {
        // 获取登录用户
        UserInfo loginUser = UserInterCeptor.getLoginUser();
        String key = KEY_PREFIX + loginUser.getId();
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        hashOps.delete(skuId.toString());
    }

}
