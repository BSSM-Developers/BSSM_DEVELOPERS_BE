package com.example.bssm_dev.domain.docs.service;

import com.example.bssm_dev.domain.docs.dto.ApiDocumentData;
import com.example.bssm_dev.domain.docs.dto.request.CreateDocsRequest;
import com.example.bssm_dev.domain.docs.extractor.DocsExtractor;
import com.example.bssm_dev.domain.docs.mapper.DocsMapper;
import com.example.bssm_dev.domain.docs.model.Docs;
import com.example.bssm_dev.domain.docs.event.DocsCreatedEvent;
import com.example.bssm_dev.domain.docs.exception.DocsNotFoundException;
import com.example.bssm_dev.domain.docs.exception.UnauthorizedDocsAccessException;
import com.example.bssm_dev.domain.docs.repository.ApiDocumentRepository;
import com.example.bssm_dev.domain.docs.repository.DocsRepository;
import com.example.bssm_dev.domain.user.model.User;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DocsCommandService {
    private final DocsRepository docsRepository;
    private final ApiDocumentRepository apiDocumentRepository;
    private final DocsMapper docsMapper;
    private final DocsExtractor docsExtractor;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void createDocs(CreateDocsRequest request, User creator) {
        Docs docs = docsMapper.toEntity(request, creator);
        Docs savedDocs = docsRepository.save(docs);

        List<ApiDocumentData> apiDocuments = docsExtractor.extractApiDocuments(request, savedDocs);
        eventPublisher.publishEvent(new DocsCreatedEvent(apiDocuments));
    }

    @Transactional
    public void deleteDocs(Long docsId, User user) {
        Docs docs = docsRepository.findById(docsId)
                .orElseThrow(DocsNotFoundException::raise);
        
        // 본인이 작성한 문서만 삭제 가능
        if (!docs.isMyDocs(user)) {
            throw UnauthorizedDocsAccessException.raise();
        }
        
        // MongoDB의 ApiDocument도 함께 삭제
        List<Long> apiIds = docsExtractor.extractApiIds(docs);
        if (!apiIds.isEmpty()) {
            apiDocumentRepository.deleteAll(
                apiDocumentRepository.findByApiIdIn(apiIds)
            );
        }
        
        docsRepository.delete(docs);
    }

}
