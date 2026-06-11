package com.example.bssm_dev.domain.api.controller.command;

import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.common.util.HttpUtil;
import com.example.bssm_dev.domain.api.service.command.ApiTokenWebhookCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/webhook/api/token")
public class WebhookApiTokenController {
    private final ApiTokenWebhookCommandService apiTokenWebhookCommandService;

    @PostMapping("/{tokenId}/block")
    public ResponseEntity<ResponseDto<Void>> blockApiToken(
            @RequestHeader("X-Webhook-Secret") String webhookSecret,
            @PathVariable("tokenId") Long tokenId
    ) {
        apiTokenWebhookCommandService.blockApiToken(webhookSecret, tokenId);
        ResponseDto<Void> responseDto = HttpUtil.success("Successfully blocked API token");
        return ResponseEntity.ok(responseDto);
    }
}
