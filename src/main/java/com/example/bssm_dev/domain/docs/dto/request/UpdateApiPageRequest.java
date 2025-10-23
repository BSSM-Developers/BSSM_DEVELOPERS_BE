package com.example.bssm_dev.domain.docs.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UpdateApiPageRequest(
        @NotBlank(message = "페이지 제목은 필수입니다")
        String docsPageTitle,

        String docsPageDescription,

        @NotBlank(message = "엔드포인트는 필수입니다")
        String endpoint,

        @NotBlank(message = "메서드는 필수입니다")
        String method,

        @NotBlank(message = "API 이름은 필수입니다")
        String name,

        String domain,

        @Valid
        ApiRequestData request,

        @Valid
        ApiResponseData response
) {
}
