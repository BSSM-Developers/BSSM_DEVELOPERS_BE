package com.example.bssm_dev.domain.api.service;

import reactor.core.publisher.Mono;

/**
 * API 토큰의 요청 제한을 추적하는 서비스 인터페이스
 */
public interface ApiRateLimiterService {
    
    /**
     * API 토큰의 요청을 기록하고 현재 분 버킷의 요청 수를 반환
     * 
     * @param apiTokenId API 토큰 ID
     * @return 현재 분 버킷의 요청 수
     */
    Mono<Long> incrementAndGet(Long apiTokenId);
    
    /**
     * 특정 API 토큰의 현재 분 버킷 요청 수 조회
     * 
     * @param apiTokenId API 토큰 ID
     * @return 현재 분 버킷의 요청 수
     */
    Mono<Long> getCurrentCount(Long apiTokenId);
    
    /**
     * 동시성 카운터 증가 (요청 시작)
     * 
     * @param apiTokenId API 토큰 ID
     * @return 현재 동시성 수
     */
    Mono<Long> incrementConcurrent(Long apiTokenId);
    
    /**
     * 동시성 카운터 감소 (요청 종료)
     * 
     * @param apiTokenId API 토큰 ID
     * @return 현재 동시성 수
     */
    Mono<Long> decrementConcurrent(Long apiTokenId);
    
    /**
     * 현재 동시성 조회
     * 
     * @param apiTokenId API 토큰 ID
     * @return 현재 동시성 수
     */
    Mono<Long> getConcurrent(Long apiTokenId);
    
    /**
     * 차단 정책 검증 및 필요 시 상태 변경
     * concurrent * 60 < 분당 요청 수면 상태 변경
     * NORMAL → WARNING → BLOCKED
     * 
     * @param apiTokenId API 토큰 ID
     * @return 새로운 상태 (변경 없으면 null)
     */
    Mono<String> checkAndUpdateState(Long apiTokenId);
}
