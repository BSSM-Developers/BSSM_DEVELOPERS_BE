package com.example.bssm_dev.proxy.error;

import com.example.bssm_dev.domain.api.dto.response.ProxyErrorResponse;
import com.example.bssm_dev.domain.api.util.TokenMasker;
import com.example.bssm_dev.exception.ErrorCode;
import com.example.bssm_dev.exception.GlobalException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
@RequiredArgsConstructor
public class ProxyWebExceptionHandler implements WebExceptionHandler {
    private static final byte[] FALLBACK_BODY =
            "{\"statusCode\":500,\"message\":\"Internal server error\"}".getBytes(StandardCharsets.UTF_8);

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if (exchange.getResponse().isCommitted()) {
            return Mono.error(ex);
        }
        if (!(ex instanceof GlobalException globalException)) {
            log.error("[UnhandledException] {} {} origin={} token={} - {}: {}",
                    exchange.getRequest().getMethod(),
                    exchange.getRequest().getPath(),
                    exchange.getRequest().getHeaders().getOrigin(),
                    TokenMasker.mask(exchange.getRequest().getHeaders().getFirst("bssm-dev-token")),
                    ex.getClass().getSimpleName(),
                    ex.getMessage(), ex);
            return Mono.error(ex);
        }

        ErrorCode errorCode = globalException.getErrorCode();
        log.warn("[GlobalException] {} {} origin={} token={} - {}: {}",
                exchange.getRequest().getMethod(),
                exchange.getRequest().getPath(),
                exchange.getRequest().getHeaders().getOrigin(),
                TokenMasker.mask(exchange.getRequest().getHeaders().getFirst("bssm-dev-token")),
                globalException.getClass().getSimpleName(),
                errorCode.getErrorMessage());

        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.valueOf(errorCode.getStatusCode()));
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        ProxyErrorResponse body = new ProxyErrorResponse(false, errorCode.getStatusCode(), errorCode.getErrorMessage(), null);
        return Mono.fromCallable(() -> objectMapper.writeValueAsBytes(body))
                .flatMap(bytes -> response.writeWith(Mono.just(response.bufferFactory().wrap(bytes))))
                .onErrorResume(writeErr ->
                        response.writeWith(Mono.just(response.bufferFactory().wrap(FALLBACK_BODY)))
                );
    }
}
