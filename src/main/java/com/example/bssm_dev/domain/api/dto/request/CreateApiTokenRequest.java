package com.example.bssm_dev.domain.api.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CreateApiTokenRequest(
        @NotBlank(message = "API 토큰 이름은 필수입니다.")
        String apiTokenName,

        List<String> domains
) {
}
