package com.huawei.common.exception;

import com.huawei.common.enums.ExceptionEnums;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SelfException extends  RuntimeException{
    private ExceptionEnums exceptionEnums;

}
