package com.example.bssm_dev.proxy.error;

import com.example.bssm_dev.domain.api.dto.response.ProxyErrorResponse;
import com.example.bssm_dev.domain.api.exception.ExternalApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ProxyExceptionHandler {

    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<ProxyErrorResponse> externalApiExceptionHandler(ExternalApiException e) {
        if (e.getUpstreamStatusCode() != null) {
            ProxyErrorResponse response = new ProxyErrorResponse(
                    true,
                    e.getUpstreamStatusCode(),
                    e.getErrorMsg(),
                    e.getUpstreamBody()
            );
            return ResponseEntity.status(e.getUpstreamStatusCode()).body(response);
        }

        ProxyErrorResponse response = new ProxyErrorResponse(
                false,
                e.getErrorCode().getStatusCode(),
                e.getErrorCode().getErrorMessage(),
                e.getErrorMsg()
        );
        return ResponseEntity.status(e.getErrorCode().getStatusCode()).body(response);
    }
}
