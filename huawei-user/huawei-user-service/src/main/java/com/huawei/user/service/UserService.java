package com.huawei.user.service;/**
 * @Auther: 25349
 * @Date: 2019/6/8 22:15
 * @Description:
 */

/**
 * @Auther: ccuibeijie
 * @Date: 2019/6/8 22:15
 * @Description:用户中心接口层
 */
public interface UserService {
    //检验数据唯一性
    Boolean checkData(String data, Integer type);
    //发送短信验证码
    String sendVerifyCode(String phone);
}
