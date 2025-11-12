package com.example.bssm_dev.domain.docs.mapper;
import com.example.bssm_dev.domain.docs.dto.request.*;
import com.example.bssm_dev.domain.docs.dto.response.DocsListResponse;
import com.example.bssm_dev.domain.docs.model.Docs;
import com.example.bssm_dev.domain.docs.model.type.DocumentType;
import com.example.bssm_dev.domain.user.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DocsMapper {

    public Docs toOriginalDocs(CreateOriginalDocsRequest request, User creator) {
        Docs docs = Docs.builder()
                .title(request.title())
                .repositoryUrl(request.repositoryUrl())
                .description(request.description())
                .domain(request.domain())
                .type(DocumentType.ORIGINAL)
                .autoApproval(request.autoApproval())
                .writerId(creator.getUserId())
                .build();
        return docs;
    }

    public Docs toCustomDocs(CreateCustomDocsRequest request, User creator) {
        Docs docs = Docs.builder()
                .title(request.title())
                .repositoryUrl(request.repositoryUrl())
                .description(request.description())
                .domain(request.domain())
                .type(DocumentType.CUSTOMIZE)
                .autoApproval(request.autoApproval())
                .writerId(creator.getUserId())
                .build();
        return docs;
    }

    public DocsListResponse toListResponse(Docs docs, String writerName) {
        return new DocsListResponse(
                docs.getId(),
                docs.getTitle(),
                docs.getDescription(),
                docs.getWriterId(),
                writerName,
                docs.getType().name(),
                docs.getDomain(),
                docs.getRepositoryUrl(),
                docs.getAutoApproval()
        );
    }

    public List<DocsListResponse> toDocsListResponse(Map<Long, User> userMap, List<Docs> content) {
        return content.stream()
                .map(docs -> {
                    User writer = userMap.get(docs.getWriterId());
                    String writerName = writer != null ? writer.getName() : "Unknown";
                    return this.toListResponse(docs, writerName);
                })
                .toList();
    }

    public List<DocsListResponse> toDocsListResponse(String writerName,  List<Docs> content) {
        return content.stream()
                .map(docs -> this.toListResponse(docs, writerName))
                .toList();
    }
}
