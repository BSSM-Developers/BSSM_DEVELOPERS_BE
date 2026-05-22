package com.example.bssm_dev.proxy.error;

import com.example.bssm_dev.domain.api.dto.response.ProxyErrorResponse;
import com.example.bssm_dev.domain.api.exception.ExternalApiException;
import com.example.bssm_dev.domain.api.util.TokenMasker;
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
    public ResponseEntity<ProxyErrorResponse> handleGlobalException(GlobalException e, ServerWebExchange exchange) {
        ErrorCode errorCode = e.getErrorCode();
        log.warn("[GlobalException] {} {} origin={} token={} - {}: {}",
                exchange.getRequest().getMethod(),
                exchange.getRequest().getPath(),
                exchange.getRequest().getHeaders().getOrigin(),
                TokenMasker.mask(exchange.getRequest().getHeaders().getFirst("bssm-dev-token")),
                e.getClass().getSimpleName(),
                errorCode.getErrorMessage());
        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(new ProxyErrorResponse(false, errorCode.getStatusCode(), errorCode.getErrorMessage(), null));
    }

    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<ProxyErrorResponse> handleExternalApiException(ExternalApiException e, ServerWebExchange exchange) {
        String token = TokenMasker.mask(exchange.getRequest().getHeaders().getFirst("bssm-dev-token"));
        if (e.getUpstreamStatusCode() != null) {
            log.warn("[ExternalApiException] {} {} origin={} token={} - upstream status={} message={}",
                    exchange.getRequest().getMethod(),
                    exchange.getRequest().getPath(),
                    exchange.getRequest().getHeaders().getOrigin(),
                    token, e.getUpstreamStatusCode(), e.getErrorMsg());
            return ResponseEntity
                    .status(e.getUpstreamStatusCode())
                    .body(new ProxyErrorResponse(true, e.getUpstreamStatusCode(), e.getErrorMsg(), e.getUpstreamBody()));
        }
        log.error("[ExternalApiException] {} {} origin={} token={} - {}: {}",
                exchange.getRequest().getMethod(),
                exchange.getRequest().getPath(),
                exchange.getRequest().getHeaders().getOrigin(),
                token, e.getErrorCode(), e.getErrorMsg(), e);
        return ResponseEntity
                .status(e.getErrorCode().getStatusCode())
                .body(new ProxyErrorResponse(false, e.getErrorCode().getStatusCode(), e.getErrorCode().getErrorMessage(), e.getErrorMsg()));
    }
}
