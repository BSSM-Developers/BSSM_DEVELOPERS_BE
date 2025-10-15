package com.example.bssm_dev.domain.api.event.listner;

import com.example.bssm_dev.domain.api.event.ApiUseReasonCreatedEvent;
import com.example.bssm_dev.domain.api.model.Api;
import com.example.bssm_dev.domain.api.service.ApiUsageCommandService;
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
    private final ApiUsageCommandService apiUsageService;

    @EventListener
    @Transactional
    @Async
    public void handleApiUseReasonCreated(ApiUseReasonCreatedEvent event) {
        Api api = event.getApi();

        Boolean apiAutoApproval = api.getAutoApproval();
        boolean isAutoApproval = apiAutoApproval != null && apiAutoApproval;

        if (isAutoApproval) {
            log.info("자동 승인 처리 중");
            apiUsageService.createApiUsage(
                    api,
                    event.getCurrentApiToken(),
                    event.getApiUseReason()
            );
            log.info("자동 승인 완료");
        }
    }
}
