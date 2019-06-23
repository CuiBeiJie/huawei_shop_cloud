package com.huawei.order.utils;
import com.github.wxpay.sdk.WXPay;
import static com.github.wxpay.sdk.WXPayConstants.*;
import com.github.wxpay.sdk.WXPayUtil;
import com.huawei.common.enums.ExceptionEnums;
import com.huawei.common.exception.SelfException;
import com.huawei.order.config.PayConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: cuibeijie
 * @Date: 2019/6/23 16:33
 * @Description: 微信支付助手
 */
@Component
@Slf4j
public class PayHelper {
    @Autowired
    private PayConfig payConfig;
    @Autowired
    private WXPay wxPay;
    /**
     * 生成支付二维码
     * @param orderId
     * @param description
     * @param totalPay
     * @return
     */
    public String createPayUrl(Long orderId, String description, Long totalPay){
        try {
            HashMap<String, String> reqData = new HashMap<>();
            //描述
            reqData.put("body", description);
            //订单号
            reqData.put("out_trade_no", orderId.toString());
            //货币（默认就是人民币）
            //data.put("fee_type", "CNY");
            //总金额
            reqData.put("total_fee", totalPay.toString());
            //调用微信支付的终端ip
            reqData.put("spbill_create_ip", "127.0.0.1");
            //回调地址
            reqData.put("notify_url", payConfig.getNotifyUrl());
            //交易类型为扫码支付
            reqData.put("trade_type", "NATIVE");

            //发起请求并获取响应结果
            Map<String, String> result = wxPay.unifiedOrder(reqData);
            //判断通信和业务标识
            isSuccess(result);
            //检验签名
            isSignatureValid(result);
            //下单成功，获取支付连接
            String url = result.get("code_url");
            return url;
        } catch (Exception e) {
            log.error(" 【微信下单】 创建预交易订单异常失败,订单id:{}",orderId,e);
            return null;
        }
    }

    public void isSuccess(Map<String, String> result) {
        //通信失败
        if (FAIL.equals(result.get("return_code"))) {
            log.error("【微信下单】与微信通信失败，失败信息：{}", result.get("return_msg"));
        }

        //下单失败
        if (FAIL.equals(result.get("result_code"))) {
            log.error("【微信下单】创建预交易订单失败，错误码：{}，错误信息：{}",
                    result.get("err_code"), result.get("err_code_des"));
        }
    }

    /**
     * 检验签名
     * @param result
     */
    public void isSignatureValid(Map<String, String> result) {
        try {
            boolean boo1 = WXPayUtil.isSignatureValid(result, payConfig.getKey(), SignType.HMACSHA256);
            boolean boo2 = WXPayUtil.isSignatureValid(result, payConfig.getKey(), SignType.MD5);

            if (!boo1 && !boo2) {
                throw new SelfException(ExceptionEnums.WX_PAY_SIGN_INVALID);
            }
        } catch (Exception e) {
            log.error("【微信支付】检验签名失败，数据：{}", result);
            throw new SelfException(ExceptionEnums.WX_PAY_SIGN_INVALID);
        }
    }
}
