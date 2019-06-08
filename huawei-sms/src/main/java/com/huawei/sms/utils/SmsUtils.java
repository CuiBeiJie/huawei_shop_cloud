package com.huawei.sms.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.huawei.sms.config.SmsProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @program: huaweishop
 * @description: 短信工具类
 * @author: cuibeijie
 * @create: 2019-06-08 01:23
 */
@Slf4j
@EnableConfigurationProperties(SmsProperties.class)
@Component
public class SmsUtils {

    @Autowired
    private SmsProperties prop;

    @Autowired
    private StringRedisTemplate redisTemplate;
    //产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";
    //短信前缀
    private static final String KEY_PREFIX="sms:phone:";
    //短信发送最小间隔
    private static final long SMS_SEND_MIN_INTERVAL_IN_MILLS = 60000;

    public SendSmsResponse sendSms(String phoneNumber, String signName, String temPlateCode, String templateParam) {
        String key = KEY_PREFIX + phoneNumber;
        //对手机号码发送短信进行限流
        String lastTime = redisTemplate.opsForValue().get(key);
        if(StringUtils.isNotBlank(lastTime)){
            //一分钟内只允许对一个手机号发送一次短信，防止触发阿里云限流
            if (System.currentTimeMillis() - Long.valueOf(lastTime) < SMS_SEND_MIN_INTERVAL_IN_MILLS) {
                log.info("[短信服务] 短信验证码发送频率过高，被拦截，请稍后再发，手机号码：{}",phoneNumber);
                return null;
            }
        }
        try {
            //可自助调整超时时间
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");

            //初始化acsClient,暂不支持region化
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", prop.getAccessKeyId(), prop.getAccessKeySecret());
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
            IAcsClient acsClient = new DefaultAcsClient(profile);

            //组装请求对象-具体描述见控制台-文档部分内容
            SendSmsRequest request = new SendSmsRequest();
            request.setMethod(MethodType.POST);
            //必填:待发送手机号
            request.setPhoneNumbers(phoneNumber);
            //必填:短信签名-可在短信控制台中找到
            request.setSignName(signName);
            //必填:短信模板-可在短信控制台中找到
            request.setTemplateCode(temPlateCode);
            //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            request.setTemplateParam(templateParam);

            //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
            //request.setSmsUpExtendCode("90997");

            //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
            request.setOutId("123456");

            //hint 此处可能会抛出异常，注意catch
            SendSmsResponse resp = acsClient.getAcsResponse(request);
            if (!"OK".equals(resp.getCode())) {
                log.info("[短信服务] 发送失败,phoneNumber:{},原因:{}", phoneNumber, resp.getMessage());
                return null;
            }
            log.info("[短信服务] 验证码发送成功，手机号码：{}",phoneNumber);
            //短信发送成功后，写入redis
            redisTemplate.opsForValue().set(key,String.valueOf(System.currentTimeMillis()),1, TimeUnit.MINUTES);
            return resp;
        } catch (Exception e) {
            log.error("[短信服务] 发送异常,phoneNumber:{},原因:{}", phoneNumber, e.getMessage());
            return null;
        }
    }
}
