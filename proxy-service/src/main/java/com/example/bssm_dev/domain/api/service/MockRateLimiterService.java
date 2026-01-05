package com.example.bssm_dev.domain.api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * 개발 환경용 Mock Rate Limiter 서비스
 * 실제 카운팅은 하지 않고 항상 성공 응답만 반환
 */
@Slf4j
@Service
@Profile("dev")
public class MockRateLimiterService implements ApiRateLimiterService {
    
    @Override
    public Mono<Long> incrementAndGet(Long apiTokenId) {
        log.debug("[MockRateLimiter] incrementAndGet called for apiTokenId: {}", apiTokenId);
        return Mono.just(1L);
    }
    
    @Override
    public Mono<Long> getCurrentCount(Long apiTokenId) {
        log.debug("[MockRateLimiter] getCurrentCount called for apiTokenId: {}", apiTokenId);
        return Mono.just(0L);
    }
    
    @Override
    public Mono<Long> incrementConcurrent(Long apiTokenId) {
        log.debug("[MockRateLimiter] incrementConcurrent called for apiTokenId: {}", apiTokenId);
        return Mono.just(1L);
    }
    
    @Override
    public Mono<Long> decrementConcurrent(Long apiTokenId) {
        log.debug("[MockRateLimiter] decrementConcurrent called for apiTokenId: {}", apiTokenId);
        return Mono.just(0L);
    }
    
    @Override
    public Mono<Long> getConcurrent(Long apiTokenId) {
        log.debug("[MockRateLimiter] getConcurrent called for apiTokenId: {}", apiTokenId);
        return Mono.just(0L);
    }
    
    @Override
    public Mono<String> checkAndUpdateState(Long apiTokenId) {
        log.debug("[MockRateLimiter] checkAndUpdateState called for apiTokenId: {}", apiTokenId);
        return Mono.empty();
    }
}
