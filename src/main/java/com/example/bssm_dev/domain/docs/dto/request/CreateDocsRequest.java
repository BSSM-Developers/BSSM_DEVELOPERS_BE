package com.example.bssm_dev.domain.docs.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CreateDocsRequest(
        String docsTitle,
        String docsDescription,
        String domain,
        String repositoryUrl,
        Boolean autoApproval,
        List<CreateDocsSectionRequest> docsSections
) {
}
