package com.huawei.order.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "huawei.worker")
@Data
public class IdWorkerProperties {

    private long workerId;// 当前机器id

    private long datacenterId;// 序列号

}