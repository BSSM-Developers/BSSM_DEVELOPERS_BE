package com.example.bssm_dev.domain.docs.dto.response;

import com.example.bssm_dev.domain.docs.document.ContentDocument;
import com.example.bssm_dev.domain.docs.document.DocumentBlock;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record ContentMongoResponse(
        String id,
        String docsId,
        List<BlockResponse> blocks,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ContentMongoResponse from(ContentDocument content) {
        return new ContentMongoResponse(
                content.getId(),
                content.getDocsId(),
                content.getBlocks().stream()
                        .map(BlockResponse::from)
                        .toList(),
                content.getCreatedAt(),
                content.getUpdatedAt()
        );
    }
    
    public record BlockResponse(
            String id,
            String module,
            String content,
            Integer order,
            Map<String, Object> metadata
    ) {
        public static BlockResponse from(DocumentBlock block) {
            return new BlockResponse(
                    block.getId(),
                    block.getModule().getValue(),  // 소문자 값 사용
                    block.getContent(),
                    block.getOrder(),
                    block.getMetadata()
            );
        }
    }
}
