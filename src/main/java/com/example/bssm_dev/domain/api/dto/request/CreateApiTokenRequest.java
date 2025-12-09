package com.example.bssm_dev.domain.api.dto.request;

import com.example.bssm_dev.domain.api.model.TokenType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateApiTokenRequest(
        @NotBlank(message = "API 토큰 이름은 필수입니다.")
        String apiTokenName,

        @NotNull(message = "토큰 타입은 필수입니다.")
        TokenType tokenType,

        List<String> domains
) {
}
