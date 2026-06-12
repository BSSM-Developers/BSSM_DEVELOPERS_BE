package com.example.bssm_dev.domain.api.event.listner;

import com.example.bssm_dev.domain.api.event.UnblockRequestApprovedEvent;
import com.example.bssm_dev.domain.api.event.UnblockRequestRejectedEvent;
import com.example.bssm_dev.domain.api.model.UnblockRequest;
import com.example.bssm_dev.global.client.MailServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UnblockRequestEventListener {

    private final MailServiceClient mailServiceClient;

    @EventListener
    @Async
    public void handleApproved(UnblockRequestApprovedEvent event) {
        UnblockRequest req = event.unblockRequest();
        String to = req.getRequester().getEmail();
        String tokenName = req.getApiToken().getApiTokenName();
        mailServiceClient.send(
                to,
                "[BSSM Developers] API 토큰 차단 해제 승인",
                "안녕하세요.\n\nAPI 토큰 '" + tokenName + "'의 차단 해제 요청이 승인되었습니다.\n\n감사합니다."
        );
    }

    @EventListener
    @Async
    public void handleRejected(UnblockRequestRejectedEvent event) {
        UnblockRequest req = event.unblockRequest();
        String to = req.getRequester().getEmail();
        String tokenName = req.getApiToken().getApiTokenName();
        String rejectReason = req.getRejectReason() != null ? req.getRejectReason() : "-";
        mailServiceClient.send(
                to,
                "[BSSM Developers] API 토큰 차단 해제 거절",
                "안녕하세요.\n\nAPI 토큰 '" + tokenName + "'의 차단 해제 요청이 거절되었습니다.\n\n거절 사유: " + rejectReason + "\n\n감사합니다."
        );
    }
}
