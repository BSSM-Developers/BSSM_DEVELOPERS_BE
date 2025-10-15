package com.example.bssm_dev.domain.docs.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Map;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ApiRequestData(
        @NotBlank(message = "요청 applicationType은 필수입니다")
        String applicationType,
        
        List<String> header,
        List<String> pathParams,
        List<String> queryParams,
        Map<String, Object> body,
        List<String> cookie
) {
}
