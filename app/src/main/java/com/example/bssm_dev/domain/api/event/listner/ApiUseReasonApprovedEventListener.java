package com.example.bssm_dev.domain.api.event.listner;

import com.example.bssm_dev.domain.api.event.ApiUseReasonApprovedEvent;
import com.example.bssm_dev.domain.api.model.ApiUseReason;
import com.example.bssm_dev.global.client.MailServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApiUseReasonApprovedEventListener {

    private final MailServiceClient mailServiceClient;

    @EventListener
    @Async
    public void handleApproved(ApiUseReasonApprovedEvent event) {
        ApiUseReason apiUseReason = event.apiUseReason();
        String to = apiUseReason.getWriter().getEmail();
        String apiName = event.api().getName();
        mailServiceClient.send(
                to,
                "[BSSM Developers] API 사용 신청 승인",
                "안녕하세요.\n\n'" + apiName + "' API 사용 신청이 승인되었습니다.\n\n감사합니다."
        );
    }
}
