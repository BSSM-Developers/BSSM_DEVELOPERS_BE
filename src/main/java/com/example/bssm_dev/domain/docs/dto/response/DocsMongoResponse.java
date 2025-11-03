package com.example.bssm_dev.domain.docs.dto.response;

import com.example.bssm_dev.domain.docs.document.DocsDocument;
import com.example.bssm_dev.domain.docs.document.SidebarItem;

import java.time.LocalDateTime;
import java.util.List;

public record DocsMongoResponse(
        String id,
        String title,
        String description,
        CreatorInfo creator,
        String type,
        String domain,
        String repositoryUrl,
        Boolean autoApproval,
        List<SidebarItemResponse> sidebar,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static DocsMongoResponse from(DocsDocument docs) {
        return new DocsMongoResponse(
                docs.getId(),
                docs.getTitle(),
                docs.getDescription(),
                new CreatorInfo(
                        docs.getCreator().getUserId(),
                        docs.getCreator().getName(),
                        docs.getCreator().getEmail()
                ),
                docs.getType().name(),
                docs.getDomain(),
                docs.getRepositoryUrl(),
                docs.getAutoApproval(),
                docs.getSidebar().stream()
                        .map(SidebarItemResponse::from)
                        .toList(),
                docs.getCreatedAt(),
                docs.getUpdatedAt()
        );
    }
    
    public record CreatorInfo(
            Long userId,
            String name,
            String email
    ) {}
    
    public record SidebarItemResponse(
            String id,
            String label,
            String module,
            ApiMetadataResponse apiMetadata,
            List<SidebarItemResponse> childrenItems
    ) {
        public static SidebarItemResponse from(SidebarItem item) {
            return new SidebarItemResponse(
                    item.getId(),
                    item.getLabel(),
                    item.getModule().getValue(),  // 소문자 값 사용
                    item.getApiMetadata() != null 
                            ? new ApiMetadataResponse(
                                    item.getApiMetadata().getMethod(),
                                    item.getApiMetadata().getEndpoint(),
                                    item.getApiMetadata().getTags()
                            )
                            : null,
                    item.getChildrenItems() != null
                            ? item.getChildrenItems().stream()
                                    .map(SidebarItemResponse::from)
                                    .toList()
                            : null
            );
        }
    }
    
    public record ApiMetadataResponse(
            String method,
            String endpoint,
            List<String> tags
    ) {}
}
