package com.example.bssm_dev.proxy.error;

import com.example.bssm_dev.domain.api.dto.response.ProxyErrorResponse;
import com.example.bssm_dev.exception.ErrorCode;
import com.example.bssm_dev.exception.GlobalException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
@RequiredArgsConstructor
public class ProxyWebExceptionHandler implements WebExceptionHandler {
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if (exchange.getResponse().isCommitted()) {
            return Mono.error(ex);
        }
        if (!(ex instanceof GlobalException globalException)) {
            return Mono.error(ex);
        }

        ErrorCode errorCode = globalException.getErrorCode();
        ProxyErrorResponse response = new ProxyErrorResponse(
                false,
                errorCode.getStatusCode(),
                errorCode.getErrorMessage(),
                null
        );
        ServerHttpResponse httpResponse = exchange.getResponse();
        httpResponse.setStatusCode(HttpStatus.valueOf(errorCode.getStatusCode()));
        httpResponse.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        return Mono.fromCallable(() -> objectMapper.writeValueAsBytes(response))
                .flatMap(bytes -> httpResponse.writeWith(
                        Mono.just(httpResponse.bufferFactory().wrap(bytes))
                ))
                .onErrorResume(writeErr -> {
                    byte[] fallback = "{\"statusCode\":429,\"message\":\"Too many requests\"}"
                            .getBytes(StandardCharsets.UTF_8);
                    return httpResponse.writeWith(
                            Mono.just(httpResponse.bufferFactory().wrap(fallback))
                    );
                });
    }
}
