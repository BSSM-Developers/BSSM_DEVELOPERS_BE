package com.example.bssm_dev.domain.github.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "GitHub OAuth 연동 요청")
public record GitHubConnectRequest(
        @Schema(description = "GitHub OAuth 콜백으로 전달받은 인증 코드", example = "abc123def456")
        String code
) {}
