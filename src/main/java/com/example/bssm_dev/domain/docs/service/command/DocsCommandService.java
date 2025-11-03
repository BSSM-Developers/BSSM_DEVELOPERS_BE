package com.example.bssm_dev.domain.docs.service.command;

import com.example.bssm_dev.domain.docs.dto.request.CreateCustomDocsRequest;
import com.example.bssm_dev.domain.docs.dto.request.CreateOriginalDocsRequest;
import com.example.bssm_dev.domain.docs.mapper.DocsMapper;
import com.example.bssm_dev.domain.docs.model.Docs;
import com.example.bssm_dev.domain.docs.repository.DocsRepository;
import com.example.bssm_dev.domain.user.model.User;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
//@Transactional("mongoTransactionManager")
public class DocsCommandService {
    private final DocsRepository docsRepository;
    private final DocsMapper docsMapper;

    private final DocsSideBarCommandService docsSideBarCommandService;
    private final DocsPageCommandService docsPageCommandService;


    public void createOriginalDocs(CreateOriginalDocsRequest request, User creator) {
        Docs docs = docsMapper.toOriginalDocs(request, creator);
        Docs newDocs = docsRepository.save(docs);
        docsSideBarCommandService.save(request.sidebar(), newDocs);
        docsPageCommandService.save(request.docsPages(), newDocs);
    }

    public void createCustomDocs(CreateCustomDocsRequest request, User creator) {
        Docs docs = docsMapper.toCustomDocs(request, creator);
        docsRepository.save(docs);
        // Custom Docs는 빈 문서로 생성되므로 sidebar와 docsPages를 저장하지 않음
    }
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
