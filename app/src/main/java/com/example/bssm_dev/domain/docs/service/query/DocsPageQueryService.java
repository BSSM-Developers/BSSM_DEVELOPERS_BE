package com.example.bssm_dev.domain.docs.service.query;

import com.example.bssm_dev.domain.api.model.Api;
import com.example.bssm_dev.domain.api.repository.ApiRepository;
import com.example.bssm_dev.domain.docs.dto.response.DocsPageResponse;
import com.example.bssm_dev.domain.docs.exception.DocsPageNotFoundException;
import com.example.bssm_dev.domain.docs.mapper.DocsPageMapper;
import com.example.bssm_dev.domain.docs.model.ContentDocsPage;
import com.example.bssm_dev.domain.docs.model.DocsPage;
import com.example.bssm_dev.domain.docs.model.ReferenceDocsPage;
import com.example.bssm_dev.domain.docs.repository.DocsPageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DocsPageQueryService {
    private final DocsPageRepository docsPageRepository;
    private final DocsPageMapper docsPageMapper;
    private final ApiRepository apiRepository;

    public DocsPageResponse getPage(String docsId, String mappedId) {
        DocsPage docsPage = docsPageRepository.findByDocsIdAndMappedId(docsId, mappedId)
                .orElseThrow(DocsPageNotFoundException::raise);

        if (docsPage instanceof ReferenceDocsPage ref) {
            ContentDocsPage sourcePage = (ContentDocsPage) docsPageRepository
                    .findByDocsIdAndMappedId(ref.getSourceDocsId(), ref.getSourceMappedId())
                    .orElseThrow(DocsPageNotFoundException::raise);
            return docsPageMapper.toDocsPageResponse(sourcePage, resolveVersion(sourcePage.getMappedId()));
        }

        ContentDocsPage contentPage = (ContentDocsPage) docsPage;
        return docsPageMapper.toDocsPageResponse(contentPage, resolveVersion(contentPage.getMappedId()));
    }

    private Integer resolveVersion(String apiGroupId) {
        return apiRepository.findByApiGroupApiGroupIdAndIsCurrentTrue(apiGroupId)
                .map(Api::getVersion)
                .orElse(null);
    }

    public DocsPage findById(String apiId) {
        return docsPageRepository.findById(apiId)
                .orElseThrow(DocsPageNotFoundException::raise);
    }
}
