package com.example.bssm_dev.domain.docs.service.command;

import com.example.bssm_dev.domain.docs.dto.request.CreateCustomDocsRequest;
import com.example.bssm_dev.domain.docs.dto.request.DocsCreateRequest;
import com.example.bssm_dev.domain.docs.dto.request.DocsUpdateRequest;
import com.example.bssm_dev.domain.docs.dto.request.UpdateDocsRequest;
import com.example.bssm_dev.domain.docs.exception.DocsNotFoundException;
import com.example.bssm_dev.domain.docs.init.CustomDocsInitializer;
import com.example.bssm_dev.domain.docs.mapper.DocsMapper;
import com.example.bssm_dev.domain.docs.model.Docs;
import com.example.bssm_dev.domain.docs.model.DocsPage;
import com.example.bssm_dev.domain.docs.model.SideBar;
import com.example.bssm_dev.domain.docs.model.event.DocsCreatedEvent;
import com.example.bssm_dev.domain.docs.repository.DocsRepository;
import com.example.bssm_dev.domain.docs.validator.DocsValidator;
import com.example.bssm_dev.domain.user.model.User;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
//@Transactional("mongoTransactionManager")
public class DocsCommandService {
    private final DocsRepository docsRepository;
    private final DocsMapper docsMapper;

    private final DocsSideBarCommandService docsSideBarCommandService;
    private final DocsPageCommandService docsPageCommandService;

    private final ApplicationEventPublisher applicationEventPublisher;


    public void createOriginalDocs(DocsCreateRequest request, User creator) {
        Docs docs = docsMapper.toOriginalDocs(request, creator);

        Docs newDocs = docsRepository.save(docs);
        SideBar newSideBar = docsSideBarCommandService.save(request.sidebar(), newDocs);
        List<DocsPage> newDocsPages = docsPageCommandService.save(request.docsPages(), newDocs);

        applicationEventPublisher.publishEvent(
                DocsCreatedEvent.from(newDocs, newSideBar, newDocsPages)
        );
    }

    public void createCustomDocs(CreateCustomDocsRequest request, User creator) {
        Docs docs = docsMapper.toCustomDocs(request, creator);

        String docsId = docsRepository.save(docs).getId();
        docsSideBarCommandService.save(CustomDocsInitializer.initSideBar(docsId));
        docsPageCommandService.save(CustomDocsInitializer.initDocsPage(docsId));
    }

    public void updateDocs(String docsId, UpdateDocsRequest request, User user) {
        Docs docs = getMyDocs(docsId, user);

        docs.updateDocs(
                request.title(),
                request.description(),
                request.domain(),
                request.repositoryUrl()
        );
        
        docsRepository.save(docs);
    }

    public void updateDocsAutoApproval(String docsId, User user) {
        Docs docs = getMyDocs(docsId, user);
        docs.toggleAutoApproval();
        docsRepository.save(docs);
    }

    public void replaceDocs(String docsId, DocsUpdateRequest request, User user) {
        Docs docs = getMyDocs(docsId, user);
        docs.updateDocs(
                request.title(),
                request.description(),
                request.domain(),
                request.repositoryUrl()
        );
        docsRepository.save(docs);

        // 기존 sidebar, docs page 삭제
        docsSideBarCommandService.delete(docsId);
        docsPageCommandService.delete(docsId);

        // 새 sidebar, docs page 생성
        docsSideBarCommandService.save(request.sidebar(), docs);
        docsPageCommandService.save(request.docsPages(), docs);
    }

    public void deleteDocs(String docsId, User user) {
        Docs docs = getMyDocs(docsId, user);
        docsRepository.delete(docs);
        docsSideBarCommandService.delete(docsId);
        docsPageCommandService.delete(docsId);
    }

    private Docs getMyDocs(String docsId, User user) {
        Docs docs = docsRepository.findById(docsId)
                .orElseThrow(DocsNotFoundException::raise);

        DocsValidator.checkIfIsMyDocs(user, docs);
        return docs;
    }
}
