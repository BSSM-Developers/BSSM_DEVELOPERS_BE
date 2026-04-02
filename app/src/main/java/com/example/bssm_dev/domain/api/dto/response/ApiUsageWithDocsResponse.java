package com.example.bssm_dev.domain.api.dto.response;

import com.example.bssm_dev.domain.docs.dto.response.DocsPageBlockResponse;

import java.util.List;

public record ApiUsageWithDocsResponse(
        String apiId,
        String name,
        String endpoint,
        String apiMethod,
        String apiUseState,
        List<DocsPageBlockResponse> docsBlocks
) {
}
