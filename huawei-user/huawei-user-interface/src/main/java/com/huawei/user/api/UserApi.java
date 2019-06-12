package com.huawei.user.api;

import com.huawei.user.pojo.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Auther: cuibeijie
 * @Date: 2019/6/12 21:50
 * @Description: 用户对外提供接口层
 */
public interface UserApi {
    @GetMapping("query")
    User queryUserByUserNameAndPassWord(@RequestParam("username") String username,
                                        @RequestParam("password") String password);
}
