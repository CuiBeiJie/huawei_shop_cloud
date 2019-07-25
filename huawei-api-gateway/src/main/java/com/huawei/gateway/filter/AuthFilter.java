package com.huawei.gateway.filter;
import com.huawei.auth.utils.JwtUtils;
import com.huawei.common.utils.CookieUtils;
import com.huawei.gateway.config.FilterProperties;
import com.huawei.gateway.config.JwtProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: cuibeijie
 * @Date: 2019/6/12 23:53
 * @Description: 登录授权过滤器
 */
@Component
@EnableConfigurationProperties({JwtProperties.class, FilterProperties.class})
@Slf4j
public class AuthFilter extends ZuulFilter {
    @Autowired
    private JwtProperties prop;
    @Autowired
    private FilterProperties filterProp;
    /**
     * to classify a filter by type. Standard types in Zuul are "pre" for pre-routing filtering,
     * "route" for routing to an origin, "post" for post-routing filters, "error" for error handling.
     * We also support a "static" type for static responses see  StaticResponseFilter.
     * Any filterType made be created or added and run by calling FilterProcessor.runFilters(type)
     *
     * @return A String representing that type
     */
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE; //过滤器类型,前置过滤器
    }

    /**
     * filterOrder() must also be defined for a filter. Filters may have the same  filterOrder if precedence is not
     * important for a filter. filterOrders do not need to be sequential.
     *
     * @return the int order of a filter
     */
    @Override
    public int filterOrder() {
        return FilterConstants.DEBUG_FILTER_ORDER - 1;//官方的过滤器顺序前面
    }

    /**
     * a "true" return from this method means that the run() method should be invoked
     *
     * @return true if the run() method should be invoked. false will not invoke the run() method
     */
    @Override
    public boolean shouldFilter() {
        // 获取上下文
        RequestContext ctx = RequestContext.getCurrentContext();
        // 获取request
        HttpServletRequest req = ctx.getRequest();
        // 获取路径
        String requestURI = req.getRequestURI();
        // 判断白名单
        return !isAllowPath(requestURI);
    }

    /**
     * 判断请求URI是不是白名单中的URI
     *
     * @param requestURI
     * @return
     */
    private Boolean isAllowPath(String requestURI) {
        for (String allowPath : filterProp.getAllowPaths()) {
            if (requestURI.startsWith(allowPath)) {
                //允许
               return true;
            }
        }
        return false;
    }

    /**
     * if shouldFilter() is true, this method will be invoked. this method is the core method of a ZuulFilter
     *
     * @return Some arbitrary artifact may be returned. Current implementation ignores it.
     * @throws ZuulException if an error occurs during execution.
     */
    @Override
    public Object run() throws ZuulException {
        //获取上下文
        RequestContext ctx = RequestContext.getCurrentContext();
        //获取request
        HttpServletRequest request = ctx.getRequest();
        //获取Cookie中的token
        String token = CookieUtils.getCookieValue(request, prop.getCookieName());
        //从Token获取解析用户信息
        try {
            JwtUtils.getUserInfo(prop.getPublicKey(), token);
            //解析成功，什么都不做，放行
            //todo 如果做权限管理的话，在这做权限检验
        } catch (Exception e) {
            //检验出现异常，返回403
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(403);
            log.error("非法访问，未登录，地址：{}", request.getRemoteHost(), e);
        }
        return null;
    }
}
