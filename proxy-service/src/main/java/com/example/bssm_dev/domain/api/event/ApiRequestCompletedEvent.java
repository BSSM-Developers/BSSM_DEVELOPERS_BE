package com.example.bssm_dev.domain.api.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * API 요청 완료 이벤트
 */
@Getter
@AllArgsConstructor
public class ApiRequestCompletedEvent {
    private final Long apiTokenId;
}
