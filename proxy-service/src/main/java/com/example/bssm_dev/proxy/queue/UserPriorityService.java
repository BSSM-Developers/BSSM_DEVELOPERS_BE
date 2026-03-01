package com.example.bssm_dev.proxy.queue;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserPriorityService {
    private static final String FAILURE_COUNT_PREFIX = "proxy:queue:failure:";
    private static final Duration FAILURE_COUNT_TTL = Duration.ofHours(1);

    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;
    private final ProxyQueueProperties properties;

    public Mono<Double> getPriority(String clientId) {
        if (clientId == null || clientId.isBlank()) {
            return Mono.just(properties.getBasePriority());
        }

        String key = FAILURE_COUNT_PREFIX + clientId;
        return reactiveRedisTemplate.opsForValue().get(key)
                .map(Integer::parseInt)
                .defaultIfEmpty(0)
                .map(this::calculatePriority);
    }

    public Mono<Void> incrementFailure(String clientId) {
        if (clientId == null || clientId.isBlank()) {
            return Mono.empty();
        }

        String key = FAILURE_COUNT_PREFIX + clientId;
        return reactiveRedisTemplate.opsForValue().increment(key)
                .flatMap(count -> reactiveRedisTemplate.expire(key, FAILURE_COUNT_TTL))
                .doOnSuccess(v -> log.debug("Incremented failure count for client: {}", clientId))
                .then();
    }

    public Mono<Void> resetFailure(String clientId) {
        if (clientId == null || clientId.isBlank()) {
            return Mono.empty();
        }

        String key = FAILURE_COUNT_PREFIX + clientId;
        return reactiveRedisTemplate.delete(key)
                .doOnSuccess(v -> log.debug("Reset failure count for client: {}", clientId))
                .then();
    }

    private double calculatePriority(int failureCount) {
        double priority = properties.getBasePriority() + (failureCount * properties.getPriorityIncrement());
        return Math.min(priority, properties.getMaxPriority());
    }
}
