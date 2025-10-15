package com.example.bssm_dev.domain.docs.dto.response;

import java.util.List;
import java.util.Map;

public record ApiDetailResponse(
        Long apiId,
        String endpoint,
        String method,
        String name,
        String domain,
        String repositoryUrl,
        ApiDocumentResponse document
) {
    public record ApiDocumentResponse(
            RequestInfoResponse request,
            ResponseInfoResponse response
    ) {
    }

    public record RequestInfoResponse(
            String applicationType,
            List<String> header,
            List<String> pathParams,
            List<String> queryParams,
            Map<String, Object> body,
            List<String> cookie
    ) {
    }

    public record ResponseInfoResponse(
            String applicationType,
            List<String> header,
            Integer statusCode,
            Map<String, Object> body,
            List<String> cookie
    ) {
    }
}
