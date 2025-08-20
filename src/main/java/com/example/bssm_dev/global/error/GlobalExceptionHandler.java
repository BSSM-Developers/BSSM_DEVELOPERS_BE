package com.example.bssm_dev.global.error;


import com.example.bssm_dev.global.error.exception.ErrorCode;
import com.example.bssm_dev.global.error.exception.GlobalException;
import com.example.bssm_dev.global.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public ErrorResponse globalExceptionHanlder(GlobalException e) {
        ErrorCode errorCode = e.getErrorCode();
        int statusCode = errorCode.getStatusCode();
        String errorMessage = errorCode.getErrorMessage();
        log.error("stauts code {}, error message : {}", statusCode, errorMessage);
        ErrorResponse errorResponse = HttpUtil.fail(statusCode, errorMessage);
        return errorResponse;
    }

}
