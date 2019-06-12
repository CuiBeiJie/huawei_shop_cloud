package com.huawei.auth.service;/**
 * @Auther: 25349
 * @Date: 2019/6/12 20:54
 * @Description:
 */

/**
 * @Auther: cuibeijie
 * @Date: 2019/6/12 20:54
 * @Description: 授权接口层
 */
public interface AuthService {
    //登录生成Token
    String login(String username, String password);
}
