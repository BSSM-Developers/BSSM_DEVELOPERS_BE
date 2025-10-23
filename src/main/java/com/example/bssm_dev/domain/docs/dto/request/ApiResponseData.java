package com.example.bssm_dev.domain.docs.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ApiResponseData(
        @NotBlank(message = "응답 applicationType은 필수입니다")
        String applicationType,
        
        List<String> header,
        
        @NotNull(message = "응답 statusCode는 필수입니다")
        Integer statusCode,
        
        Map<String, Object> body,
        List<String> cookie
) {
}
