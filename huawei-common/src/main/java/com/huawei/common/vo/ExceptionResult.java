package com.huawei.common.vo;

import com.huawei.common.enums.ExceptionEnums;
import lombok.Data;

@Data
public class ExceptionResult {
    private int status;//状态码
    private String message;//异常消息
    private Long timestamp;//时间戳

    public ExceptionResult(ExceptionEnums enums) {
        this.status = enums.getCode();
        this.message = enums.getMsg();
        this.timestamp = System.currentTimeMillis();
    }
}
