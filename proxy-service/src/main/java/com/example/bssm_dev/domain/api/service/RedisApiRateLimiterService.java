package com.example.bssm_dev.domain.api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Redis 기반 Rate Limiter 서비스
 * Redis String + INCR + TTL 방식으로 분 단위 요청 제한 추적
 */
@Service
@Profile("!dev")
@RequiredArgsConstructor
public class RedisApiRateLimiterService implements ApiRateLimiterService {
    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;
    private final ApiTokenStateUpdateService apiTokenStateUpdateService;
    
    @Value("${api.rate-limit.threshold-multiplier:200}")
    private int thresholdMultiplier;
    
    private static final String KEY_PREFIX = "api:ratelimit:";
    private static final String CONCURRENT_PREFIX = "api:concurrent:";
    private static final DateTimeFormatter BUCKET_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
    private static final Duration BUCKET_TTL = Duration.ofMinutes(5);
    private static final Duration CONCURRENT_TTL = Duration.ofSeconds(10);
    
    @Override
    public Mono<Long> incrementAndGet(Long apiTokenId) {
        String bucket = getCurrentBucket();
        String key = buildKey(apiTokenId, bucket);
        
        return reactiveRedisTemplate.opsForValue()
                .increment(key)
                .flatMap(count -> {
                    if (count == 1) {
                        return reactiveRedisTemplate.expire(key, BUCKET_TTL)
                                .thenReturn(count);
                    }
                    return Mono.just(count);
                });
    }
    
    @Override
    public Mono<Long> getCurrentCount(Long apiTokenId) {
        String bucket = getCurrentBucket();
        String key = buildKey(apiTokenId, bucket);
        
        return reactiveRedisTemplate.opsForValue()
                .get(key)
                .map(Long::parseLong)
                .defaultIfEmpty(0L);
    }
    
    private String getCurrentBucket() {
        return LocalDateTime.now().format(BUCKET_FORMATTER);
    }
    
    private String buildKey(Long apiTokenId, String bucket) {
        return KEY_PREFIX + apiTokenId + ":" + bucket;
    }
    
    @Override
    public Mono<Long> incrementConcurrent(Long apiTokenId) {
        String key = CONCURRENT_PREFIX + apiTokenId;
        return reactiveRedisTemplate.opsForValue()
                .increment(key)
                .flatMap(count -> 
                    reactiveRedisTemplate.expire(key, CONCURRENT_TTL)
                            .thenReturn(count)
                );
    }
    
    @Override
    public Mono<Long> decrementConcurrent(Long apiTokenId) {
        String key = CONCURRENT_PREFIX + apiTokenId;
        return reactiveRedisTemplate.opsForValue()
                .decrement(key)
                .map(count -> Math.max(0, count));
    }
    
    @Override
    public Mono<Long> getConcurrent(Long apiTokenId) {
        String key = CONCURRENT_PREFIX + apiTokenId;
        return reactiveRedisTemplate.opsForValue()
                .get(key)
                .map(Long::parseLong)
                .defaultIfEmpty(0L);
    }
    
    @Override
    public Mono<String> checkAndUpdateState(Long apiTokenId) {
        return Mono.zip(
                getConcurrent(apiTokenId),
                getCurrentCount(apiTokenId)
        ).flatMap(tuple -> {
            long concurrent = tuple.getT1();
            long requestCount = tuple.getT2();
            long threshold = concurrent * thresholdMultiplier;  // 설정값 사용
            
            if (requestCount > threshold) {
                return apiTokenStateUpdateService.transitionState(apiTokenId);
            }
            return Mono.empty();
        });
    }
}
