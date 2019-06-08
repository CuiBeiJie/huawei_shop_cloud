package com.huawei.user.config;/**
 * @Auther: 25349
 * @Date: 2019/6/8 23:53
 * @Description:
 */

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * @Auther: cuibeijie
 * @Date: 2019/6/8 23:53
 * @Description:
 */
@ConfigurationProperties(prefix = "huawei.verifycode")
@Data
public class VerifyCodeProperties {
    private String exchange;
    private String routingkey;
    private  String timeout;
}
