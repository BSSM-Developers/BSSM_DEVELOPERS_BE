package com.example.bssm_dev.domain.docs.service.command;

import com.example.bssm_dev.domain.docs.dto.request.AddDocsSectionRequest;
import com.example.bssm_dev.domain.docs.exception.DocsSectionMismatchException;
import com.example.bssm_dev.domain.docs.exception.UnauthorizedDocsAccessException;
import com.example.bssm_dev.domain.docs.mapper.DocsMapper;
import com.example.bssm_dev.domain.docs.model.Docs;
import com.example.bssm_dev.domain.docs.model.DocsSection;
import com.example.bssm_dev.domain.docs.repository.DocsSectionRepository;
import com.example.bssm_dev.domain.docs.service.query.DocsQueryService;
import com.example.bssm_dev.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocsSectionCommandService {
    private final DocsSectionRepository docsSectionRepository;
    private final DocsQueryService docsQueryService;
    private final DocsMapper docsMapper;

    @Transactional
    public void addSection(Long docsId, AddDocsSectionRequest request, User user) {
        Docs docs = docsQueryService.findById(docsId);


        // 본인이 작성한 문서만 섹션 추가 가능
        boolean isMyDocs = docs.isMyDocs(user);
        if (!isMyDocs) throw UnauthorizedDocsAccessException.raise();

        // 현재 섹션들의 최대 order 값 조회 후 +1
        int maxOrder = docsSectionRepository.findMaxOrderByDocsId(docsId);
        int newOrder = maxOrder + 1;

        // 빈 섹션 생성 및 저장
        DocsSection section = docsMapper.toSectionEntity(request, docs, user, newOrder);
        docsSectionRepository.save(section);
    }

    @Transactional
    public void updateOrders(Long docsId, List<Long> sortedDocsSectionIds, User user) {
        Docs docs = docsQueryService.findById(docsId);

        // 본인이 작성한 문서만 섹션 순서 변경 가능
        boolean isMyDocs = docs.isMyDocs(user);
        if (!isMyDocs) throw UnauthorizedDocsAccessException.raise();

        // Docs에 속한 모든 섹션을 한 번에 조회하여 Map으로 변환
        Map<Long, DocsSection> sectionMap = docsMapper.toSectionMap(docs);

        // 정렬된 섹션 ID 리스트의 순서대로 order 업데이트
        for (int i = 0; i < sortedDocsSectionIds.size(); i++) {
            Long sectionId = sortedDocsSectionIds.get(i);
            DocsSection section = sectionMap.get(sectionId);
            
            section.updateOrder(i + 1);
        }
    }
}
