package com.huawei.sms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @program: huaweishop
 * @description: 短信属性配置类
 * @author: cuibeijie
 * @create: 2019-06-08 01:05
 */
@Data
@ConfigurationProperties(prefix = "huawei.sms")
public class SmsProperties {
    private String accessKeyId;

    private String accessKeySecret;

    private String signName;

    private String verifyCodeTemplate;
}
