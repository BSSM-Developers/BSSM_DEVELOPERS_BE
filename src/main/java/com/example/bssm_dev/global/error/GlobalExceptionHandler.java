package com.example.bssm_dev.global.error;


import com.example.bssm_dev.domain.api.dto.response.ApiErrorResponse;
import com.example.bssm_dev.domain.api.dto.response.ProxyErrorResponse;
import com.example.bssm_dev.domain.api.exception.ExternalApiException;
import com.example.bssm_dev.global.error.exception.ErrorCode;
import com.example.bssm_dev.global.error.exception.GlobalException;
import com.example.bssm_dev.common.util.HttpUtil;
import io.jsonwebtoken.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<?> externalApiExceptionHanlder(ExternalApiException e) {
        if (e.getUpstreamStatusCode() != null) {
            log.error("External API error passthrough status={}, body={}", e.getUpstreamStatusCode(), e.getUpstreamBody());
            ProxyErrorResponse response = new ProxyErrorResponse(
                    true,
                    e.getUpstreamStatusCode(),
                    e.getErrorMsg(),
                    e.getUpstreamBody()
            );
            return ResponseEntity
                    .status(e.getUpstreamStatusCode())
                    .body(response);
        }

        ErrorCode errorCode = e.getErrorCode();
        int statusCode = errorCode.getStatusCode();
        String errorMessage = errorCode.getErrorMessage();
        String cause = e.getErrorMsg();

        log.error("stauts code {}, error message : {}", statusCode, errorMessage);
        log.error("cause : {}", cause);

        ProxyErrorResponse errorResponse = new ProxyErrorResponse(
                false,
                statusCode,
                errorMessage,
                cause
        );
        return ResponseEntity
                .status(statusCode)
                .body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getAllErrors().stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("입력값이 올바르지 않습니다.");
        
        log.error("Validation failed: {}", errorMessage);
        ErrorResponse errorResponse = HttpUtil.fail(HttpStatus.BAD_REQUEST.value(), errorMessage);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
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

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> illegalArgumentExceptionHandler(IllegalArgumentException e) {
        log.error("IllegalArgumentException occurred: {}", e.getMessage(), e);
        ErrorResponse errorResponse = HttpUtil.fail(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

}
