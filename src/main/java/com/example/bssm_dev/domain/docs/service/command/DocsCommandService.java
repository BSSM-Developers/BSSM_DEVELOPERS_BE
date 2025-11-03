package com.example.bssm_dev.domain.docs.service.command;

import com.example.bssm_dev.domain.docs.dto.request.CreateCustomDocsRequest;
import com.example.bssm_dev.domain.docs.dto.request.CreateOriginalDocsRequest;
import com.example.bssm_dev.domain.docs.exception.DocsNotFoundException;
import com.example.bssm_dev.domain.docs.init.CustomDocsInitializer;
import com.example.bssm_dev.domain.docs.mapper.DocsMapper;
import com.example.bssm_dev.domain.docs.model.Docs;
import com.example.bssm_dev.domain.docs.repository.DocsRepository;
import com.example.bssm_dev.domain.docs.validator.DocsValidator;
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

        String docsId = docsRepository.save(docs).getId();
        docsSideBarCommandService.save(CustomDocsInitializer.initSideBar(docsId));
        docsPageCommandService.save(CustomDocsInitializer.initDocsPage(docsId));
    }

    public void updateDocsAutoApproval(String docsId, User user) {
        Docs docs = getMyDocs(docsId, user);
        docs.toggleAutoApproval();
        docsRepository.save(docs);
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
