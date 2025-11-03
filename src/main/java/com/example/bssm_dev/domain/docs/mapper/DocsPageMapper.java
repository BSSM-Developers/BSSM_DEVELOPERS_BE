package com.example.bssm_dev.domain.docs.mapper;

import com.example.bssm_dev.domain.docs.dto.request.CreateDocsPageRequest;
import com.example.bssm_dev.domain.docs.dto.request.DocsPageBlockRequest;
import com.example.bssm_dev.domain.docs.model.Docs;
import com.example.bssm_dev.domain.docs.model.DocsPage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
                .build();
        return docsPage;
    }

    public List<DocsPage> toDocsPages(List<CreateDocsPageRequest> requests, Docs newDocs) {
        return requests.stream()
                .map(docsPage -> toDocsPage(docsPage, newDocs))
                .toList();
    }
}
