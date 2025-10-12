package com.example.bssm_dev.domain.docs.dto.request;

import java.util.List;

public record CreateDocsRequest(
        String docsTitle,
        String docsDescription,
        String domain,
        String repositoryUrl,
        Boolean autoApproval,
        List<CreateDocsSectionRequest> docsSections
) {
}
