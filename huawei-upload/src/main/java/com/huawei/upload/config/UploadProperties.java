package com.huawei.upload.config;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.List;

/**
 * @Auther: cuibeijie
 * @Date: 2019/5/4 16:38
 * @Description:
 */
@Data
@ConfigurationProperties(prefix = "huawei.upload")
public class UploadProperties {
    private String baseUrl;
    private List<String> allowTypes;
}
