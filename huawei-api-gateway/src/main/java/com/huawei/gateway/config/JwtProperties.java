package com.huawei.gateway.config;
import com.huawei.auth.utils.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import javax.annotation.PostConstruct;
import java.security.PublicKey;

/**
 * @Auther: cuibeijie
 * @Date: 2019/6/12 20:29
 * @Description: JWT相关属性
 */
@Slf4j
@Data
@ConfigurationProperties(prefix = "huawei.jwt")
public class JwtProperties {
    private String pubKeyPath;// 公钥
    private PublicKey publicKey; // 公钥
    private String cookieName; //cookie的名称
    //对象一旦实例化完成后，就应该读取公钥和私钥
    @PostConstruct
    public void init() {
        try {
            // 获取公钥
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        } catch (Exception e) {
            log.error("获取公钥失败！", e);
            throw new RuntimeException();
        }
    }
}
