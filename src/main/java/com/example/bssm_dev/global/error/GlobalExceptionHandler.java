package com.example.bssm_dev.global.error;


import com.example.bssm_dev.global.error.exception.ErrorCode;
import com.example.bssm_dev.global.error.exception.GlobalException;
import com.example.bssm_dev.common.util.HttpUtil;
import io.jsonwebtoken.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ErrorResponse> globalExceptionHanlder(GlobalException e) {
        ErrorCode errorCode = e.getErrorCode();
        int statusCode = errorCode.getStatusCode();
        String errorMessage = errorCode.getErrorMessage();
        log.error("stauts code {}, error message : {}", statusCode, errorMessage);
        ErrorResponse errorResponse = HttpUtil.fail(statusCode, errorMessage);
        return ResponseEntity
                .status(statusCode)
                .body(errorResponse);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponse> ioExceptionHanlder(IOException e) {
        log.error("IOException occurred: {}", e.getMessage(), e);
        ErrorResponse errorResponse = HttpUtil.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), "입출력 오류가 발생했습니다.");
        return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(errorResponse);
    }

}
