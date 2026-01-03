package com.example.bssm_dev.domain.api.event;

import com.example.bssm_dev.domain.api.service.ApiRateLimiterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * API 요청 이벤트 핸들러
 * 비동기적으로 요청 추적 및 차단 정책 검증 처리
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApiRequestEventHandler {
    private final ApiRateLimiterService apiRateLimiterService;
    
    /**
     * 요청 시작 이벤트 처리
     * - 동시성 카운터 증가
     * - 분당 요청 수 증가
     * - 차단 정책 검증
     */
    @Async
    @EventListener
    public void handleRequestStarted(ApiRequestStartedEvent event) {
        Long apiTokenId = event.getApiTokenId();
        
        apiRateLimiterService.incrementConcurrent(apiTokenId)
                .flatMap(concurrent -> apiRateLimiterService.incrementAndGet(apiTokenId))
                .flatMap(requestCount -> apiRateLimiterService.checkAndUpdateState(apiTokenId))
                .subscribe(
                        newState -> {
                            if (newState != null) {
                                log.warn("API Token {} state updated to: {}", apiTokenId, newState);
                            }
                        },
                        error -> log.error("Error handling request started event for token {}", apiTokenId, error)
                );
    }
    
    /**
     * 요청 완료 이벤트 처리
     * - 동시성 카운터 감소
     */
    @Async
    @EventListener
    public void handleRequestCompleted(ApiRequestCompletedEvent event) {
        Long apiTokenId = event.getApiTokenId();
        
        apiRateLimiterService.decrementConcurrent(apiTokenId)
                .subscribe(
                        concurrent -> log.debug("API Token {} concurrent decreased to: {}", apiTokenId, concurrent),
                        error -> log.error("Error handling request completed event for token {}", apiTokenId, error)
                );
    }
}
