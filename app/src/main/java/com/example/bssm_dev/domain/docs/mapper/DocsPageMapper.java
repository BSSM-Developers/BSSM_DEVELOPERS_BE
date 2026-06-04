package com.example.bssm_dev.domain.docs.mapper;

import com.example.bssm_dev.domain.docs.dto.request.CreateDocsPageRequest;
import com.example.bssm_dev.domain.docs.dto.response.DocsPageBlockResponse;
import com.example.bssm_dev.domain.docs.dto.response.DocsPageResponse;
import com.example.bssm_dev.domain.docs.model.ContentDocsPage;
import com.example.bssm_dev.domain.docs.model.Docs;
import com.example.bssm_dev.domain.docs.model.DocsPage;
import com.example.bssm_dev.domain.docs.model.DocsPageBlock;
import com.example.bssm_dev.domain.docs.model.ReferenceDocsPage;
import com.example.bssm_dev.domain.docs.model.type.DocsModule;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DocsPageMapper {
    private final DocsPageBlockMapper docsPageBlockMapper;
    private final ObjectMapper objectMapper;

    public DocsPage toDocsPage(CreateDocsPageRequest request, Docs docs) {
        if (request.sourceDocsId() != null) {
            return ReferenceDocsPage.builder()
                    .mappedId(request.id())
                    .docsId(docs.getId())
                    .sourceDocsId(request.sourceDocsId())
                    .sourceMappedId(request.sourceMappedId())
                    .build();
        }

        return ContentDocsPage.builder()
                .mappedId(request.id())
                .docsId(docs.getId())
                .docsBlocks(request.blocks() != null
                        ? docsPageBlockMapper.toDocsPageBlocks(request.blocks())
                        : List.of())
                .endpoint(request.endpoint())
                .method(request.method())
                .build();
    }

    public List<DocsPage> toDocsPages(List<CreateDocsPageRequest> requests, Docs docs) {
        return requests.stream()
                .map(request -> toDocsPage(request, docs))
                .toList();
    }

    public DocsPageResponse toDocsPageResponse(ContentDocsPage docsPage, Integer version) {
        List<DocsPageBlockResponse> blockResponses = docsPage.getDocsBlocks().stream()
                .map(this::toBlockResponse)
                .toList();

        return new DocsPageResponse(
                docsPage.getId(),
                docsPage.getMappedId(),
                docsPage.getDocsId(),
                docsPage.getEndpoint(),
                version,
                blockResponses
        );
    }

    private DocsPageBlockResponse toBlockResponse(DocsPageBlock block) {
        String content = block.getContent();
        if (block.getModule() == DocsModule.API && content != null) {
            content = maskApiContent(content);
        }
        return new DocsPageBlockResponse(
                block.getId(),
                block.getMappedId(),
                block.getModuleName(),
                content
        );
    }

    private String maskApiContent(String content) {
        try {
            JsonNode root = objectMapper.readTree(content);
            String[] paramFields = {"headerParams", "cookieParams", "pathParams", "queryParams", "bodyParams", "responseParams"};
            for (String field : paramFields) {
                JsonNode params = root.get(field);
                if (params != null && params.isArray()) {
                    maskParams((ArrayNode) params);
                }
            }
            return objectMapper.writeValueAsString(root);
        } catch (Exception e) {
            return content;
        }
    }

    private void maskParams(ArrayNode params) {
        for (JsonNode param : params) {
            if (!param.isObject()) continue;
            ObjectNode obj = (ObjectNode) param;
            JsonNode maskNode = obj.get("mask");
            if (maskNode != null && maskNode.asBoolean()) {
                obj.put("example", "****");
            }
            JsonNode children = obj.get("children");
            if (children != null && children.isArray()) {
                maskParams((ArrayNode) children);
            }
        }
    }
}
