package com.huawei.gateway.config;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.List;

/**
 * @Auther: cuibeijie
 * @Date: 2019/6/15 17:31
 * @Description: 过滤白名单
 */
@ConfigurationProperties(prefix = "huawei.filter")
@Data
public class FilterProperties {
    private List<String> allowPaths;
}
