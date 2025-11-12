package com.example.bssm_dev.domain.docs.mapper;

import com.example.bssm_dev.domain.docs.dto.request.CreateDocsPageRequest;
import com.example.bssm_dev.domain.docs.dto.response.DocsPageBlockResponse;
import com.example.bssm_dev.domain.docs.dto.response.DocsPageResponse;
import com.example.bssm_dev.domain.docs.model.Docs;
import com.example.bssm_dev.domain.docs.model.DocsPage;
import com.example.bssm_dev.domain.docs.model.DocsPageBlock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DocsPageMapper {
    private final DocsPageBlockMapper docsPageBlockMapper;

    public DocsPage toDocsPage(CreateDocsPageRequest request, Docs newDocs) {
        DocsPage docsPage = DocsPage.builder()
                .mappedId(request.id())
                .docsId(newDocs.getId())
                .docsBlocks(
                        docsPageBlockMapper.toDocsPageBlocks(request.blocks())
                )
                .endpoint(request.endpoint())  // API 페이지인 경우에만 값이 있음
                .build();
        return docsPage;
    }

    public List<DocsPage> toDocsPages(List<CreateDocsPageRequest> requests, Docs newDocs) {
        return requests.stream()
                .map(docsPage -> toDocsPage(docsPage, newDocs))
                .toList();
    }

    public DocsPageResponse toDocsPageResponse(DocsPage docsPage) {
        List<DocsPageBlockResponse> blockResponses = docsPage.getDocsBlocks().stream()
                .map(this::toBlockResponse)
                .toList();

        return new DocsPageResponse(
                docsPage.getId(),
                docsPage.getMappedId(),
                docsPage.getDocsId(),
                docsPage.getEndpoint(),
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
