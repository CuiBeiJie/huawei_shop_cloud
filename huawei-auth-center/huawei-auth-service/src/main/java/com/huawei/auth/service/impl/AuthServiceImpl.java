package com.huawei.auth.service.impl;
import com.huawei.auth.client.UserClient;
import com.huawei.auth.config.JwtProperties;
import com.huawei.auth.entity.UserInfo;
import com.huawei.auth.service.AuthService;
import com.huawei.auth.utils.JwtUtils;
import com.huawei.common.enums.ExceptionEnums;
import com.huawei.common.exception.SelfException;
import com.huawei.user.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;


/**
 * @Auther: cuibeijie
 * @Date: 2019/6/12 20:55
 * @Description: 授权接口实现层
 */
@Service
@EnableConfigurationProperties(JwtProperties.class)
@Slf4j
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserClient userClient;
    @Autowired
    private JwtProperties prop;
    /**
     * 生成Token
     * @param username
     * @param password
     * @return
     */
    public String login(String username, String password) {
        try {
            //校验用户名和密码
            User user = userClient.queryUserByUserNameAndPassWord(username, password);
            if (user == null) {
                throw new SelfException(ExceptionEnums.INVALID_USERNAME_PASSWORD);
            }
            //生成Token
            String token = JwtUtils.generateToken(new UserInfo(user.getId(), username), prop.getPrivateKey(), prop.getExpire());
            return token;
        }catch (Exception e){
            log.error("[授权中心] 生成Token失败，用户名称:{}",username,e);
            throw new SelfException(ExceptionEnums.CREATE_TOKEN_ERROR);
        }

    }
}
