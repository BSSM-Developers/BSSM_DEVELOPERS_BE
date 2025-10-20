package com.example.bssm_dev.domain.docs.service.command;

import com.example.bssm_dev.domain.docs.dto.request.AddDocsPageRequest;
import com.example.bssm_dev.domain.docs.exception.DocsSectionMismatchException;
import com.example.bssm_dev.domain.docs.exception.DocsSectionNotFoundException;
import com.example.bssm_dev.domain.docs.exception.UnauthorizedDocsAccessException;
import com.example.bssm_dev.domain.docs.mapper.DocsMapper;
import com.example.bssm_dev.domain.docs.model.Docs;
import com.example.bssm_dev.domain.docs.model.DocsPage;
import com.example.bssm_dev.domain.docs.model.DocsSection;
import com.example.bssm_dev.domain.docs.repository.DocsPageRepository;
import com.example.bssm_dev.domain.docs.repository.DocsSectionRepository;
import com.example.bssm_dev.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DocsPageCommandService {
    private final DocsPageRepository docsPageRepository;
    private final DocsSectionRepository docsSectionRepository;
    private final DocsMapper docsMapper;

    @Transactional
    public void addPage(Long docsId, Long sectionId, AddDocsPageRequest request, User user) {
        DocsSection section = docsSectionRepository.findById(sectionId)
                .orElseThrow(DocsSectionNotFoundException::raise);

        boolean isDocsSection = section.isSectionOfDocs(docsId);
        if (!isDocsSection) throw DocsSectionMismatchException.raise();

        // 본인이 작성한 문서만 페이지 추가 가능
        boolean isMyDocs = section.isMyDocs(user);
        if (!isMyDocs) throw UnauthorizedDocsAccessException.raise();

        // DocsPage 생성 및 저장
        DocsPage page = docsMapper.toPageEntity(request, section);
        docsPageRepository.save(page);
    }
}

