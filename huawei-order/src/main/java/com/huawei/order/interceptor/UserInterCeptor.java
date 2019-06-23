package com.huawei.order.interceptor;

import com.huawei.auth.entity.UserInfo;
import com.huawei.auth.utils.JwtUtils;
import com.huawei.common.utils.CookieUtils;
import com.huawei.order.config.JwtProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: cuibeijie
 * @Date: 2019/6/19 15:54
 * @Description: 登录拦截器获取用户信息
 */

@Slf4j
public class UserInterCeptor implements HandlerInterceptor {

    private JwtProperties props;

    public UserInterCeptor() {
        super();
    }
    public UserInterCeptor(JwtProperties props){
        this.props = props;
    }

    //定义一个线程域，存放登录的对象
    private static final ThreadLocal<UserInfo> t1 = new ThreadLocal<>();
    //前置拦截
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       String url = request.getRequestURI();
       System.out.println("====requesturl =====" + url);
        //获取cookie中的tooken
        String token = CookieUtils.getCookieValue(request, props.getCookieName());
        if (StringUtils.isBlank(token)) {
            //用户未登录,返回401，拦截
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        try{
            //解析tooken
            UserInfo userInfo = JwtUtils.getUserInfo(props.getPublicKey(), token);
            //放入线程域中
            t1.set(userInfo);
            //放行
            return true;
        }catch (Exception e){
            //抛出异常，未登录
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            log.info("[购物车服务] 解析用户身份失败",e);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //过滤器完成后，从线程域中删除用户信息
        t1.remove();
    }
    /**
     * 获取登陆用户
     * @return
     */
    public static UserInfo getLoginUser() {
        return t1.get();
    }
}
