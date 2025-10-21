package com.example.bssm_dev.domain.docs.service.command;

import com.example.bssm_dev.domain.docs.dto.request.AddDocsPageRequest;
import com.example.bssm_dev.domain.docs.dto.request.AddApiDocsPageRequest;
import com.example.bssm_dev.domain.docs.exception.DocsSectionMismatchException;
import com.example.bssm_dev.domain.docs.exception.UnauthorizedDocsAccessException;
import com.example.bssm_dev.domain.docs.mapper.DocsMapper;
import com.example.bssm_dev.domain.docs.model.DocsPage;
import com.example.bssm_dev.domain.docs.model.DocsSection;
import com.example.bssm_dev.domain.docs.repository.DocsPageRepository;
import com.example.bssm_dev.domain.docs.service.query.DocsSectionQueryService;
import com.example.bssm_dev.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DocsPageCommandService {
    private final DocsPageRepository docsPageRepository;
private final DocsSectionQueryService docsSectionQueryService;
    private final DocsMapper docsMapper;


    public void addPage(Long docsId, Long sectionId, AddDocsPageRequest request, User user) {
        DocsSection section = docsSectionQueryService.findById(sectionId);


        checkIfIsSectionOfDocs(docsId, section);
        // 본인이 작성한 문서만 페이지 추가 가능
        checkIfIsMyDocs(user, section);
        // 현재 페이지들의 최대 order 값 조회 후 +1
        Long newOrder = getNewOrder(sectionId);
        // DocsPage 생성 및 저장
        DocsPage page = docsMapper.toPageEntity(request, section, newOrder);
        docsPageRepository.save(page);
    }

    public void addApiPage(Long docsId, Long sectionId, AddApiDocsPageRequest request, User user) {
        DocsSection section = docsSectionQueryService.findById(sectionId);

        checkIfIsSectionOfDocs(docsId, section);
        // 본인이 작성한 문서만 페이지 추가 가능
        checkIfIsMyDocs(user, section);
        // 현재 페이지들의 최대 order 값 조회 후 +1
        Long newOrder = getNewOrder(sectionId);
        // API DocsPage 생성 및 저장
        DocsPage page = docsMapper.toApiPageEntity(request, section, user, newOrder);
        docsPageRepository.save(page);
    }

    private Long getNewOrder(Long sectionId) {
        Long maxOrder = docsPageRepository.findMaxOrderBySectionId(sectionId);
        Long newOrder = maxOrder + 1;
        return newOrder;
    }


    private void checkIfIsMyDocs(User user, DocsSection section) {
        boolean isMyDocs = section.isMyDocs(user);
        if (!isMyDocs) throw UnauthorizedDocsAccessException.raise();
    }

    private void checkIfIsSectionOfDocs(Long docsId, DocsSection section) {
        boolean isDocsSection = section.isSectionOfDocs(docsId);
        if (!isDocsSection) throw DocsSectionMismatchException.raise();
    }

}

