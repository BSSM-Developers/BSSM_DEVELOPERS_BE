package com.example.bssm_dev.domain.docs.mapper;

import com.example.bssm_dev.domain.docs.dto.request.CreateDocsPageRequest;
import com.example.bssm_dev.domain.docs.dto.response.DocsPageBlockResponse;
import com.example.bssm_dev.domain.docs.dto.response.DocsPageResponse;
import com.example.bssm_dev.domain.docs.model.ContentDocsPage;
import com.example.bssm_dev.domain.docs.model.Docs;
import com.example.bssm_dev.domain.docs.model.DocsPage;
import com.example.bssm_dev.domain.docs.model.DocsPageBlock;
import com.example.bssm_dev.domain.docs.model.ReferenceDocsPage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DocsPageMapper {
    private final DocsPageBlockMapper docsPageBlockMapper;

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
        return new DocsPageBlockResponse(
                block.getId(),
                block.getMappedId(),
                block.getModuleName(),
                block.getContent()
        );
    }
}
