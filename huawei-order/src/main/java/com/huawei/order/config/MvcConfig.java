package com.huawei.order.config;
import com.huawei.order.interceptor.UserInterCeptor;
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
        //拦截器如果要配拦截路径一定要匹配经过网关之后到达controller层的request.getRequestURI()，这样才能生效
        registry.addInterceptor(new UserInterCeptor(props)).addPathPatterns("/order/**");
    }

}
