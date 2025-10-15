package com.example.bssm_dev.domain.docs.dto.response;

public record DocsResponse(
        Long docsId,
        String title,
        String description,
        String domain,
        String repositoryUrl,
        Boolean autoApproval
) {
}
