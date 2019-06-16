package com.huawei.cart.config;

import com.huawei.cart.interceptor.UserInterCeptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 将拦截器配置到注册中心
 */
@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private JwtProperties props;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //配置登录拦截器
        registry.addInterceptor(new UserInterCeptor(props)).addPathPatterns("/**");
    }
}
