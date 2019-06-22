package com.huawei.order.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @Auther: cuibeijie
 * @Date: 2019/6/22 16:07
 * @Description:
 */
@Configuration
@Slf4j
public class FeignConfiguration  implements RequestInterceptor {

    /**
     * Called for every request. Add data using methods on the supplied {@link RequestTemplate}.
     *
     * @param requestTemplate
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            HttpServletRequest request = attrs.getRequest();
            // 如果在Cookie内通过如下方式取
            Cookie[] cookies = request.getCookies();
            requestTemplate.header("aaa","test");
            if (cookies != null && cookies.length > 0) {
                for (Cookie cookie : cookies) {
                    log.info("FeignHeadConfiguration", "获取Cookie成功！");
                    requestTemplate.header(cookie.getName(), cookie.getValue());
                }
            } else {
                log.warn("FeignHeadConfiguration", "获取Cookie失败！");
            }
            //如果放在header内通过如下方式取
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String name = headerNames.nextElement();
                    String value = request.getHeader(name);
                    /**
                     * 遍历请求头里面的属性字段，将jsessionid添加到新的请求头中转发到下游服务
                     * */
                    if ("jsessionid".equalsIgnoreCase(name)) {
                        log.debug("添加自定义请求头key:" + name + ",value:" + value);
                        requestTemplate.header(name, value);
                    } else {
                        log.debug("FeignHeadConfiguration", "非自定义请求头key:" + name + ",value:" + value + "不需要添加!");
                    }
                }
            } else {
                log.warn("FeignHeadConfiguration", "获取请求头失败！");
            }
        }
    }
}
