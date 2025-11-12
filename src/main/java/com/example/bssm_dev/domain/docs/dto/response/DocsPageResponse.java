package com.example.bssm_dev.domain.docs.dto.response;

import java.util.List;

public record DocsPageResponse(
     String id,
     String mappedId,
     String docsId,
     List<DocsPageBlockResponse> docsBlocks
) {

}
