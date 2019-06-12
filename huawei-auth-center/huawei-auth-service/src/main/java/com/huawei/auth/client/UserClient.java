package com.huawei.auth.client;

import com.huawei.user.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @Auther: cuibeijie
 * @Date: 2019/6/12 21:56
 * @Description:
 */
@FeignClient(value = "user-service")
public interface UserClient extends UserApi {
}
