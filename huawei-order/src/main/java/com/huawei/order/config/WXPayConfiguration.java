package com.huawei.order.config;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: cuibeijie
 * @Date: 2019/6/23 16:26
 * @Description: WXPay配置类
 */
@Configuration
public class WXPayConfiguration {
    @Bean
    @ConfigurationProperties(prefix = "huawei.pay")
    public PayConfig payConfig(){
        return new PayConfig();
    }
    @Bean
    public WXPay wxPay(PayConfig payConfig){
        return new WXPay(payConfig, WXPayConstants.SignType.HMACSHA256);
    }
}
