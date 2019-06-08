package com.huawei;/**
 * @Auther: 25349
 * @Date: 2019/6/8 21:21
 * @Description:
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;


/**
 * @Auther: 25349
 * @Date: 2019/6/8 21:21
 * @Description:
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.huawei.user.mapper")
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class,args);
    }
}
