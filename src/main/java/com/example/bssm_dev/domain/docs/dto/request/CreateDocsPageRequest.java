package com.example.bssm_dev.domain.docs.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CreateDocsPageRequest(
        String docsPageTitle,
        String docsPageDescription,
        String type, // "markdown" or "api"
        String method, // nullable - only for type "api"
        String endpoint, // nullable - only for type "api"
        ApiRequestData request, // nullable - only for type "api"
        ApiResponseData response // nullable - only for type "api"
) {
}
