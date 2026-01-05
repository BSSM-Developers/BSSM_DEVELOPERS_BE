package com.example.bssm_dev.domain.api.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * API 요청 시작 이벤트
 */
@Getter
@AllArgsConstructor
public class ApiRequestStartedEvent {
    private final Long apiTokenId;
}
