package com.example.bssm_dev.domain.docs.service.query;

import com.example.bssm_dev.common.dto.CursorPage;
import com.example.bssm_dev.domain.docs.dto.response.DocsDetailResponse;
import com.example.bssm_dev.domain.docs.dto.response.DocsListResponse;
import com.example.bssm_dev.domain.docs.extractor.DocsExtractor;
import com.example.bssm_dev.domain.docs.mapper.DocsMapper;
import com.example.bssm_dev.domain.docs.model.type.DocumentType;
import com.example.bssm_dev.domain.docs.exception.DocsNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DocsQueryService {
    private final DocsRepository docsRepository;
    private final DocsMapper docsMapper;
    private final DocsExtractor docsExtractor;
    private final ApiDocumentQueryService apiDocumentQueryService;

    public CursorPage<DocsListResponse> getAllDocs(DocumentType type, Long cursor, Integer size) {
        
        Pageable pageable = PageRequest.of(0, size);

        Slice<Docs> docsSlice = docsRepository.findAllWithCursorOrderByDocsIdDesc(type, cursor, pageable);
        
        List<DocsListResponse> docsListResponse = docsMapper.toListResponse(docsSlice);
        
        return new CursorPage<>(docsListResponse, docsSlice.hasNext());
    }

    public CursorPage<DocsListResponse> getMyDocs(Long userId, DocumentType type, Long cursor, Integer size) {
        
        Pageable pageable = PageRequest.of(0, size);

        Slice<Docs> docsSlice = docsRepository.findMyDocsWithCursorOrderByDocsIdDesc(userId, type, cursor, pageable);
        
        List<DocsListResponse> docsListResponse = docsMapper.toListResponse(docsSlice);
        
        return new CursorPage<>(docsListResponse, docsSlice.hasNext());
    }

    public DocsDetailResponse getDocsDetail(Long docsId) {
        Docs docs = docsRepository.findById(docsId)
                .orElseThrow(DocsNotFoundException::raise);

        DocsDetailResponse response = docsMapper.toDetailResponse(docs);

        // API 문서 정보를 MongoDB에서 가져와서 추가
        List<Long> apiIds = docsExtractor.extractApiIds(docs);
        boolean apiIdsEmpty = apiIds.isEmpty();
        if (!apiIdsEmpty) {
            Map<Long, ApiDocument> apiDocumentMap = apiDocumentQueryService.findApiDocumentsByApiIds(apiIds);
            response = docsMapper.enrichWithApiDocuments(response, apiDocumentMap);
        }

        return response;
    }

    public Docs findById(Long docsId) {
        return docsRepository.findById(docsId)
                .orElseThrow(DocsNotFoundException::raise);
    }
}
