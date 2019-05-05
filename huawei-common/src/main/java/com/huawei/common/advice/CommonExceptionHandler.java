package com.huawei.common.advice;

import com.huawei.common.enums.ExceptionEnums;
import com.huawei.common.exception.SelfException;
import com.huawei.common.vo.ExceptionResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommonExceptionHandler {
    @ExceptionHandler(SelfException.class)
    public ResponseEntity<ExceptionResult> handelException(SelfException e){
        ExceptionEnums enums = e.getExceptionEnums();
        return ResponseEntity.status(enums.getCode()).body(new ExceptionResult(enums));
    }
}
