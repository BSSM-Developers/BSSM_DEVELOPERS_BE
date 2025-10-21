package com.example.bssm_dev.domain.docs.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AddApiDocsPageRequest(
        @NotBlank(message = "페이지 제목은 필수입니다")
        String docsPageTitle,

        String docsPageDescription,

        @Positive(message = "순서는 양수여야 합니다")
        Long order,

        @NotBlank(message = "메서드는 필수입니다")
        String method,

        @NotBlank(message = "엔드포인트는 필수입니다")
        String endpoint,

        @Valid
        ApiRequestData request,

        @Valid
        ApiResponseData response
) {
}
