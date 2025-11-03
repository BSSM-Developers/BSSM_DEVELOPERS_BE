package com.example.bssm_dev.domain.docs.service.command;

import com.example.bssm_dev.domain.docs.dto.ApiDocumentData;
import com.example.bssm_dev.domain.docs.dto.request.CreateCustomDocsRequest;
import com.example.bssm_dev.domain.docs.dto.request.CreateOriginalDocsRequest;
import com.example.bssm_dev.domain.docs.extractor.DocsExtractor;
import com.example.bssm_dev.domain.docs.mapper.DocsMapper;
import com.example.bssm_dev.domain.docs.event.DocsCreatedEvent;
import com.example.bssm_dev.domain.docs.exception.DocsNotFoundException;
import com.example.bssm_dev.domain.docs.model.Docs;
import com.example.bssm_dev.domain.docs.repository.DocsRepository;
import com.example.bssm_dev.domain.docs.validator.DocsValidator;
import com.example.bssm_dev.domain.user.model.User;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DocsCommandService {
    private final DocsRepository docsRepository;
    private final DocsMapper docsMapper;

    private final DocsSideBarCommandService docsSideBarCommandService;
    private final DocsPageCommandService docsPageCommandService;


    public void createOriginalDocs(CreateOriginalDocsRequest request, User creator) {
        Docs docs = docsMapper.toOriginalDocs(request, creator);
        Docs newDocs = docsRepository.save(docs);
        docsSideBarCommandService.save(request.sidebarRequest(), newDocs);
        docsPageCommandService.save(request.docsPagesRequest(), newDocs);
    }

//    public void createCustomDocs(CreateCustomDocsRequest request, User user) {
//        Docs docs = docsMapper.toCustomDocs(request, user);
//        docsRepository.save(docs);
//    }
//
//    public void deleteDocs(Long docsId, User user) {
//        Docs docs = docsRepository.findById(docsId)
//                .orElseThrow(DocsNotFoundException::raise);
//
//        // 본인이 작성한 문서만 삭제 가능
//        DocsValidator.checkIfIsMyDocs(user, docs);
//
//        docsRepository.delete(docs);
//    }
//
//
//    public void updateDocs(Long docsId, UpdateDocsRequest request, User user) {
//        Docs docs = docsRepository.findById(docsId)
//                .orElseThrow(DocsNotFoundException::raise);
//
//        // 본인이 작성한 문서만 수정 가능
//        DocsValidator.checkIfIsMyDocs(user, docs);
//
//        // Docs 업데이트
//        docs.updateDocs(
//                request.docsTitle(),
//                request.docsDescription(),
//                request.domain()
//        );
//    }
//
//    public void updateDocsAutoApproval(Long docsId, User user) {
//        Docs docs = docsRepository.findById(docsId)
//                .orElseThrow(DocsNotFoundException::raise);
//
//        // 본인이 작성한 문서만 수정 가능
//        DocsValidator.checkIfIsMyDocs(user, docs);
//
//        docs.turnAutoApproval();
//    }
}
