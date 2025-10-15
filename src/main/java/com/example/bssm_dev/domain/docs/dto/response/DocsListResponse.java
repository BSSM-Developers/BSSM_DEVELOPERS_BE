package com.example.bssm_dev.domain.docs.dto.response;

public record DocsListResponse(
        Long docsId,
        String title,
        String description,
        String type,
        String domain,
        String repositoryUrl,
        Boolean autoApproval
) {
}
