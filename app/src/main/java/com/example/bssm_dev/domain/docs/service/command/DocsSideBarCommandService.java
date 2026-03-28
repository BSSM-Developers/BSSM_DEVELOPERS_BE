package com.example.bssm_dev.domain.docs.service.command;

import com.example.bssm_dev.domain.docs.dto.request.CreateDocsSideBarRequest;
import com.example.bssm_dev.domain.docs.dto.request.UpdateDocsSideBarRequest;
import com.example.bssm_dev.domain.docs.exception.DocsNotFoundException;
import com.example.bssm_dev.domain.docs.exception.DocsSideBarNotFoundException;
import com.example.bssm_dev.domain.docs.mapper.DocsSideBarBlockMapper;
import com.example.bssm_dev.domain.docs.mapper.DocsSideBarMapper;
import com.example.bssm_dev.domain.docs.model.ContentDocsPage;
import com.example.bssm_dev.domain.docs.model.Docs;
import com.example.bssm_dev.domain.docs.model.SideBar;
import com.example.bssm_dev.domain.docs.model.SideBarBlock;
import com.example.bssm_dev.domain.docs.repository.DocsPageRepository;
import com.example.bssm_dev.domain.docs.repository.DocsRepository;
import com.example.bssm_dev.domain.docs.repository.DocsSideBarRepository;
import com.example.bssm_dev.domain.docs.validator.DocsValidator;
import com.example.bssm_dev.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocsSideBarCommandService {
    private final DocsSideBarRepository docsSideBarRepository;
    private final DocsSideBarMapper docsSideBarMapper;
    private final DocsSideBarBlockMapper docsSideBarBlockMapper;
    private final DocsRepository docsRepository;
    private final DocsPageRepository docsPageRepository;

    public SideBar save(CreateDocsSideBarRequest request, Docs newDocs) {
        SideBar sideBar = docsSideBarMapper.toDocsSideBar(request, newDocs);
        SideBar newSideBar = docsSideBarRepository.save(sideBar);
        return newSideBar;
    }

    public void save(SideBar sideBar) {
        docsSideBarRepository.save(sideBar);
    }

    public void delete(String docsId) {
        docsSideBarRepository.deleteByDocsId(docsId);
    }

    @Transactional("mongoTransactionManager")
    public void update(String docsId, UpdateDocsSideBarRequest request, User user) {
        Docs docs = docsRepository.findById(docsId)
                .orElseThrow(DocsNotFoundException::raise);
        DocsValidator.checkIfIsMyDocs(user, docs);

        SideBar sideBar = docsSideBarRepository.findByDocsId(docsId)
                .orElseThrow(DocsSideBarNotFoundException::raise);

        List<SideBarBlock> updatedBlocks = docsSideBarBlockMapper.toDocsSideBarBlocks(request.blocks());

        // 기존에 존재하는 DocsPage의 mappedId 목록 조회
        Set<String> existingMappedIds = docsPageRepository.findAllByDocsId(docsId)
                .stream()
                .map(page -> page.getMappedId())
                .collect(Collectors.toSet());

        // 새로 추가된 블록 중 대응하는 DocsPage가 없는 것을 찾아 빈 페이지 생성
        List<ContentDocsPage> missingPages = new ArrayList<>();
        collectMissingPages(updatedBlocks, existingMappedIds, docsId, missingPages);

        if (!missingPages.isEmpty()) {
            docsPageRepository.saveAll(missingPages);
        }

        sideBar.updateSideBarBlocks(updatedBlocks);
        docsSideBarRepository.save(sideBar);
    }

    /**
     * 재귀적으로 블록 트리를 순회하며, DocsPage가 없는 블록에 대해 빈 ContentDocsPage를 수집한다.
     */
    private void collectMissingPages(
            List<SideBarBlock> blocks,
            Set<String> existingMappedIds,
            String docsId,
            List<ContentDocsPage> missingPages
    ) {
        if (blocks == null) return;

        for (SideBarBlock block : blocks) {
            String mappedId = block.getMappedId();
            if (mappedId != null && !existingMappedIds.contains(mappedId)) {
                missingPages.add(ContentDocsPage.builder()
                        .mappedId(mappedId)
                        .docsId(docsId)
                        .docsBlocks(List.of())
                        .build());
                // 중복 생성 방지: 같은 요청 내 동일 mappedId가 여러 번 나와도 한 번만 생성
                existingMappedIds.add(mappedId);
            }
            collectMissingPages(block.getChildrenItems(), existingMappedIds, docsId, missingPages);
        }
    }
}
