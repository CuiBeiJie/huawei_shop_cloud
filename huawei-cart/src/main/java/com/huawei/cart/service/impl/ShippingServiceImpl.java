package com.huawei.cart.service.impl;


import com.huawei.auth.entity.UserInfo;
import com.huawei.cart.interceptor.UserInterCeptor;
import com.huawei.cart.mapper.ShippingMapper;
import com.huawei.cart.pojo.Shipping;
import com.huawei.cart.service.ShippingService;
import com.huawei.common.enums.ExceptionEnums;
import com.huawei.common.exception.SelfException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Auther: cuibeijie
 * @Date: 2019/6/17 23:52
 * @Description: 物流地址接口实现层
 */
@Service
public class ShippingServiceImpl implements ShippingService {
    @Autowired
    private ShippingMapper shippingMapper;
    /**
     * 增加物流地址
     * @param shipping
     */
    public void addShipping(Shipping shipping) {
        // 获取登录用户
        UserInfo loginUser = UserInterCeptor.getLoginUser();
        shipping.setUserId(loginUser.getId());
        shipping.setCreateTime(new Date());
        shipping.setUpdateTime(new Date());
        int num = shippingMapper.insert(shipping);
        if(num != 1){
            throw new SelfException(ExceptionEnums.SHIPPING_SAVE_ERROR);
        }
    }
}
