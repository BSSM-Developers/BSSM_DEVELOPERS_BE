package com.example.bssm_dev.domain.docs.service.command;

import com.example.bssm_dev.domain.docs.dto.request.AddDocsSectionRequest;
import com.example.bssm_dev.domain.docs.exception.DocsNotFoundException;
import com.example.bssm_dev.domain.docs.exception.UnauthorizedDocsAccessException;
import com.example.bssm_dev.domain.docs.mapper.DocsMapper;
import com.example.bssm_dev.domain.docs.model.Docs;
import com.example.bssm_dev.domain.docs.model.DocsSection;
import com.example.bssm_dev.domain.docs.repository.DocsRepository;
import com.example.bssm_dev.domain.docs.repository.DocsSectionRepository;
import com.example.bssm_dev.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DocsSectionCommandService {
    private final DocsSectionRepository docsSectionRepository;
    private final DocsRepository docsRepository;
    private final DocsMapper docsMapper;

    @Transactional
    public void addSection(String docsId, AddDocsSectionRequest request, User user) {
        Long parsedDocsId = Long.parseLong(docsId);
        Docs docs = docsRepository.findById(parsedDocsId)
                .orElseThrow(DocsNotFoundException::raise);

        // 본인이 작성한 문서만 섹션 추가 가능
        boolean isMyDocs = docs.isMyDocs(user);
        if (!isMyDocs) throw UnauthorizedDocsAccessException.raise();


        // 빈 섹션 생성 및 저장
        DocsSection section = docsMapper.toSectionEntity(request, docs, user);
        docsSectionRepository.save(section);
    }
}
