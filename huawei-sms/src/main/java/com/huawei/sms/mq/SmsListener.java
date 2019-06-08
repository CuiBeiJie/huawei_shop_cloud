package com.huawei.sms.mq;
import com.aliyuncs.exceptions.ClientException;
import com.huawei.common.utils.JsonUtils;
import com.huawei.sms.config.SmsProperties;
import com.huawei.sms.utils.SmsUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * @program: huaweishop
 * @description: 短信消息监听器
 * @author: cuibeijie
 * @create: 2019-06-08 01:52
 */
@Component
@EnableConfigurationProperties(SmsProperties.class)
public class SmsListener {
    @Autowired
    private SmsProperties prop;
    @Autowired
    private SmsUtils smsUtils;
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "sms.verify.code.queue", durable = "true"),
            exchange = @Exchange(value = "huawei.sms.exchange",
                    ignoreDeclarationExceptions = "true"),
            key = {"sms.verify.code"}))
    public void listenSms(Map<String, String> msg)  {
        if(CollectionUtils.isEmpty(msg)){
            return;
        }
       String phone = msg.remove("phone");
       if(StringUtils.isBlank(phone)){
           return;
       }
        System.out.println("===msg==="+JsonUtils.serialize(msg));
       //处理消息
        smsUtils.sendSms(phone,prop.getSignName(),prop.getVerifyCodeTemplate(), JsonUtils.serialize(msg));
    }
}
