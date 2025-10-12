package com.example.bssm_dev.domain.docs.dto.request;

public record CreateDocsPageRequest(
        String docsPageTitle,
        String docsPageDescription,
        String type, // "markdown" or "api"
        String method, // nullable - only for type "api"
        String endpoint // nullable - only for type "api"
) {
}
