package com.huawei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @program: huaweishop
 * @description: 启动类
 * @author: cuibeijie
 * @create: 2019-05-25 18:47
 */
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class PageApplication {
    public static void main(String[] args) {
        SpringApplication.run(PageApplication.class,args);
    }
}
