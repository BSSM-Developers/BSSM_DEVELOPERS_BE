package com.example.bssm_dev.proxy.error;

import com.example.bssm_dev.domain.api.dto.response.ProxyErrorResponse;
import com.example.bssm_dev.domain.api.exception.ExternalApiException;
import com.example.bssm_dev.exception.ErrorCode;
import com.example.bssm_dev.exception.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;

@RestControllerAdvice
@Slf4j
public class ProxyExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ProxyErrorResponse> globalExceptionHandler(GlobalException e, ServerWebExchange exchange) {
        ErrorCode errorCode = e.getErrorCode();
        log.warn("[GlobalException] {} {} origin={} token={} - {}: {}",
                exchange.getRequest().getMethod(),
                exchange.getRequest().getPath(),
                exchange.getRequest().getHeaders().getOrigin(),
                maskToken(exchange.getRequest().getHeaders().getFirst("bssm-dev-token")),
                e.getClass().getSimpleName(),
                errorCode.getErrorMessage());
        ProxyErrorResponse response = new ProxyErrorResponse(
                false,
                errorCode.getStatusCode(),
                errorCode.getErrorMessage(),
                null
        );
        return ResponseEntity.status(errorCode.getStatusCode()).body(response);
    }

    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<ProxyErrorResponse> externalApiExceptionHandler(ExternalApiException e, ServerWebExchange exchange) {
        if (e.getUpstreamStatusCode() != null) {
            log.warn("[ExternalApiException] {} {} origin={} token={} - upstream status={} message={}",
                    exchange.getRequest().getMethod(),
                    exchange.getRequest().getPath(),
                    exchange.getRequest().getHeaders().getOrigin(),
                    maskToken(exchange.getRequest().getHeaders().getFirst("bssm-dev-token")),
                    e.getUpstreamStatusCode(),
                    e.getErrorMsg());
            ProxyErrorResponse response = new ProxyErrorResponse(
                true,
                    e.getUpstreamStatusCode(),
                    e.getErrorMsg(),
                    e.getUpstreamBody()
            );
            return ResponseEntity.status(e.getUpstreamStatusCode()).body(response);
        }

        log.error("[ExternalApiException] {} {} origin={} token={} - {}: {}",
                exchange.getRequest().getMethod(),
                exchange.getRequest().getPath(),
                exchange.getRequest().getHeaders().getOrigin(),
                maskToken(exchange.getRequest().getHeaders().getFirst("bssm-dev-token")),
                e.getErrorCode(),
                e.getErrorMsg(), e);
        ProxyErrorResponse response = new ProxyErrorResponse(
                false,
                e.getErrorCode().getStatusCode(),
                e.getErrorCode().getErrorMessage(),
                e.getErrorMsg()
        );
        return ResponseEntity.status(e.getErrorCode().getStatusCode()).body(response);
    }

    private String maskToken(String token) {
        if (token == null || token.isBlank()) return "null";
        if (token.length() <= 6) return "***";
        return token.substring(0, 3) + "***" + token.substring(token.length() - 3);
    }
}
