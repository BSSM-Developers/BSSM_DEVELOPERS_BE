package com.example.bssm_dev.domain.api.event.listner;

import com.example.bssm_dev.domain.api.event.ApiUseReasonCreatedEvent;
import com.example.bssm_dev.domain.api.service.ApiUsageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApiUseReasonEventListener {
    private final ApiUsageService apiUsageService;

    @EventListener
    @Transactional
    @Async
    public void handleApiUseReasonCreated(ApiUseReasonCreatedEvent event) {
        log.info("API 사용 신청 생성 이벤트 수신: apiUseReasonId={}, apiId={}, userId={}, autoApproval={}",
                event.getApiUseReasonId(), event.getApiId(), event.getUserId(), event.getAutoApproval());

        boolean isAutoApproval = event.getAutoApproval() != null && event.getAutoApproval();
        if (isAutoApproval) {
            log.info("자동 승인 처리 중: apiUseReasonId={}", event.getApiUseReasonId());
            apiUsageService.createApiUsage(
                    event.getApiUseReasonId(),
                    event.getApiId(),
                    event.getUserId()
            );
            log.info("자동 승인 완료: apiUseReasonId={}", event.getApiUseReasonId());
        }
    }
}
