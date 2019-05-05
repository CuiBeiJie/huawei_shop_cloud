package com.huawei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Auther: cuibeijie
 * @Date: 2019/5/3 23:20
 * @Description:上传微服务的启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
public class UploadServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UploadServiceApplication.class,args);
    }
}
