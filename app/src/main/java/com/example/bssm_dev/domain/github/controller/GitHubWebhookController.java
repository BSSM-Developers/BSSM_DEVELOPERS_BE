package com.example.bssm_dev.domain.github.controller;

import com.example.bssm_dev.domain.github.service.GitHubWebhookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "GitHub Webhook", description = "GitHub App에서 수신하는 웹훅 이벤트 처리 API")
@RestController
@RequestMapping("/webhook/github")
@RequiredArgsConstructor
public class GitHubWebhookController {

    private final GitHubWebhookService gitHubWebhookService;

    @Operation(
            summary = "GitHub 웹훅 수신",
            description = "GitHub App에서 발생한 웹훅 이벤트를 수신하여 처리합니다.\n\n" +
                          "**처리하는 이벤트:**\n" +
                          "- `installation` (created): GitHub App 설치 완료 → installationId 저장\n" +
                          "- `installation` (deleted): GitHub App 제거 → installationId 초기화\n" +
                          "- `push`: 등록된 레포지토리에 코드 push → endpoint-parser로 AST 분석 후 엔드포인트 자동 동기화\n\n" +
                          "**보안:** X-Hub-Signature-256 헤더를 HMAC-SHA256으로 검증합니다. 서명이 일치하지 않으면 요청을 거부합니다.\n\n" +
                          "이 API는 GitHub에서만 호출하며, 직접 호출하지 않습니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "웹훅 이벤트 처리 성공"),
            @ApiResponse(responseCode = "401", description = "웹훅 시그니처 검증 실패")
    })
    @SecurityRequirements
    @PostMapping
    public ResponseEntity<Void> receive(
            @Parameter(description = "GitHub 이벤트 종류 (installation, push 등)", example = "push")
            @RequestHeader("X-GitHub-Event") String event,
            @Parameter(description = "HMAC-SHA256 서명 (sha256={hex})", example = "sha256=abc123...")
            @RequestHeader("X-Hub-Signature-256") String signature,
            @RequestBody String payload
    ) {
        gitHubWebhookService.handle(event, signature, payload);
        return ResponseEntity.ok().build();
    }
}
