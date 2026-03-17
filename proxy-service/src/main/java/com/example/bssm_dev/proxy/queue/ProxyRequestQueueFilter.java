package com.example.bssm_dev.proxy.queue;

import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import com.example.bssm_dev.proxy.error.TooManyRequestsException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
@RequiredArgsConstructor
public class ProxyRequestQueueFilter implements WebFilter {
    private static final String TOKEN_HEADER = "bssm-dev-token";

    private final RequestQueue requestQueue;
    private final UserPriorityService userPriorityService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String clientId = exchange.getRequest().getHeaders().getFirst(TOKEN_HEADER);
        if (clientId == null || clientId.isBlank()) {
            return chain.filter(exchange);
        }

        return userPriorityService.getPriority(clientId)
                .flatMap(priority -> Mono.usingWhen(
                        requestQueue.tryAcquire(clientId, priority),
                        acquired -> {
                            if (!acquired) {
                                return userPriorityService.incrementFailure(clientId)
                                        .then(Mono.error(new TooManyRequestsException()));
                            }
                            return chain.filter(exchange);
                        },
                        acquired -> acquired ? requestQueue.release() : Mono.empty(),
                        (acquired, err) -> acquired ? requestQueue.release() : Mono.empty(),
                        acquired -> acquired ? requestQueue.release() : Mono.empty()
                ));
    }
}
