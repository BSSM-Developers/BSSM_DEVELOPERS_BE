package com.example.bssm_dev.domain.api.service;

import com.example.bssm_dev.domain.api.model.type.ApiTokenState;
import com.example.bssm_dev.domain.api.repository.r2dbc.ApiTokenR2dbcRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * API 토큰 상태 업데이트 서비스
 * NORMAL → WARNING → BLOCKED 상태 전환
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ApiTokenStateUpdateService {
    private final ApiTokenR2dbcRepository apiTokenRepository;
    
    /**
     * API 토큰 상태 전환
     * NORMAL → WARNING
     * WARNING → BLOCKED
     * 
     * @param apiTokenId API 토큰 ID
     * @return 새로운 상태 (변경 없으면 empty)
     */
    public Mono<String> transitionState(Long apiTokenId) {
        return apiTokenRepository.findById(apiTokenId)
                .flatMap(apiToken -> {
                    ApiTokenState currentState = apiToken.getState();
                    ApiTokenState newState = apiToken.transitionToNextState();
                    
                    if (newState != null) {
                        log.warn("API Token {} state changed: {} -> {}", 
                                apiTokenId, currentState, newState);
                        
                        return apiTokenRepository.save(apiToken)
                                .thenReturn(newState.name());
                    }
                    
                    return Mono.empty();
                });
    }
}
