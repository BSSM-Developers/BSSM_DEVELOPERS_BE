package com.example.bssm_dev.domain.docs.service.command;

import com.example.bssm_dev.domain.docs.exception.DocsPageNotFoundException;
import com.example.bssm_dev.domain.docs.exception.DocsPageNotApiPageException;
import com.example.bssm_dev.domain.docs.service.query.ApiDocumentQueryService;
import com.example.bssm_dev.domain.docs.validator.DocsValidator;
import com.example.bssm_dev.domain.docs.mapper.DocsMapper;
import com.example.bssm_dev.domain.docs.mapper.ApiDocumentMapper;
import com.example.bssm_dev.domain.api.model.Api;
import com.example.bssm_dev.domain.docs.service.query.DocsSectionQueryService;
import com.example.bssm_dev.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class DocsPageCommandService {
    private final DocsPageRepository docsPageRepository;
    private final DocsSectionQueryService docsSectionQueryService;
    private final DocsMapper docsMapper;
    private final ApiDocumentMapper apiDocumentMapper;
    private final ApiDocumentQueryService apiDocumentQueryService;
    private final ApiDocumentCommandService apiDocumentCommandService;


    public void addPage(Long docsId, Long sectionId, AddDocsPageRequest request, User user) {
        DocsSection section = docsSectionQueryService.findById(sectionId);

        DocsValidator.checkIfIsSectionOfDocs(docsId, section);
        // 본인이 작성한 문서만 페이지 추가 가능
        DocsValidator.checkIfIsMyDocs(user, section);
        // 현재 페이지들의 최대 order 값 + 1
        Long newOrder = section.nextOrderValue();
        // DocsPage 생성 및 저장
        DocsPage page = docsMapper.toPageEntity(request, section, newOrder);
        docsPageRepository.save(page);
    }

    public void addApiPage(Long docsId, Long sectionId, AddApiDocsPageRequest request, User user) {
        DocsSection section = docsSectionQueryService.findById(sectionId);

        DocsValidator.checkIfIsSectionOfDocs(docsId, section);
        // 본인이 작성한 문서만 페이지 추가 가능
        DocsValidator.checkIfIsMyDocs(user, section);
        // 현재 페이지들의 최대 order 값 + 1
        Long newOrder = section.nextOrderValue();

        // API DocsPage 생성 및 저장
        DocsPage page = docsMapper.toApiPageEntity(request, section, user, newOrder);
        docsPageRepository.save(page);
    }

    public void updateOrders(Long sectionId, Long targetPageId,List<Long> sortedDocsPageIds, User user) {
        DocsSection section = docsSectionQueryService.findById(sectionId);

        // 본인이 작성한 문서만 페이지 순서 변경 가능
        DocsValidator.checkIfIsMyDocs(user, section);

        DocsPage targetDocsPage = docsPageRepository.findById(targetPageId)
                .orElseThrow();
        targetDocsPage.updateSection(section);

        // DocsSection에 속한 모든 페이지를 한 번에 조회하여 Map으로 변환
        Map<Long, DocsPage> pageMap = docsMapper.toDocsPageMap(section);

        // 정렬된 페이지 ID 리스트의 순서대로 order 업데이트
        for (int i = 0; i < sortedDocsPageIds.size(); i++) {
            Long pageId = sortedDocsPageIds.get(i);
            DocsPage page = pageMap.get(pageId);

            page.updateOrder(i + 1);
        }
    }

    public void deletePage(Long docsId, Long sectionId, Long pageId, User user) {
        // DocsPage 조회
        DocsPage page = docsPageRepository.findById(pageId)
                .orElseThrow(DocsPageNotFoundException::raise);

        DocsSection section = page.getDocsSection();

        // 해당 페이지가 이 섹션에 속하는지 확인
        DocsValidator.checkIfIsSectionOfDocs(docsId, section);
        // 본인이 작성한 문서만 페이지 삭제 가능
        DocsValidator.checkIfIsMyDocs(user, section);

        // DocsPage 삭제 (ApiPage, Api 모두 cascade로 자동 삭제)
        // ApiDocument는 ApiPageListener에서 자동 삭제됨
        docsPageRepository.delete(page);
    }


    public void updatePage(Long docsId, Long pageId, UpdateDocsPageRequest request, User user) {
        // DocsPage 조회
        DocsPage page = docsPageRepository.findById(pageId)
                .orElseThrow(DocsPageNotFoundException::raise);

        DocsSection section = page.getDocsSection();

        // 해당 페이지가 이 섹션에 속하는지 확인
        DocsValidator.checkIfIsSectionOfDocs(docsId, section);
        // 본인이 작성한 문서만 페이지 수정 가능
        DocsValidator.checkIfIsMyDocs(user, section);

        // title과 description 업데이트
        page.updateTitleAndDescription(request.docsPageTitle(), request.docsPageDescription());
    }


    public void updateApiPage(Long docsId, Long pageId, UpdateApiPageRequest request, User user) {
        DocsPage page = docsPageRepository.findById(pageId)
                .orElseThrow(DocsPageNotFoundException::raise);

        DocsSection section = page.getDocsSection();

        // 해당 페이지가 이 섹션에 속하는지 확인
        DocsValidator.checkIfIsSectionOfDocs(docsId, section);
        // 본인이 작성한 문서만 페이지 수정 가능
        DocsValidator.checkIfIsMyDocs(user, section);

        // ApiPage가 아닌 경우 예외 처리
        boolean isApiPapge = page.isApiPage();
        if (!isApiPapge) throw DocsPageNotApiPageException.raise();

        // DocsPage 업데이트
        page.updateTitleAndDescription(request.docsPageTitle(), request.docsPageDescription());

        // Api 업데이트
        Api api = page.getApi();
        api.updateApiInfo(request.endpoint(), request.method(), request.name(), request.domain());

        // ApiDocument 업데이트
        ApiDocument apiDocument = apiDocumentQueryService.findByApiId(api);
        if (apiDocument == null) return;
        updateApiDocuement(request, apiDocument);
    }

    public void duplicatePage(Long docsId, Long pageId, Long targetDocsSectionId, User currentUser) {
        DocsPage originalPage = docsPageRepository.findById(pageId)
                .orElseThrow(DocsPageNotFoundException::raise);

        // 해당 페이지가 이 섹션에 속하는지 확인
        DocsSection section = originalPage.getDocsSection();
        DocsValidator.checkIfIsSectionOfDocs(docsId, section);

        // Docs가 custom docs인지 검증
        DocsSection targetDocsSection = docsSectionQueryService.findById(targetDocsSectionId);
        DocsValidator.checkCustomizeDocs(targetDocsSection.getDocs());

        DocsPage duplicatedDocsPage = DocsPage.duplicate(
                targetDocsSection,
                originalPage,
                currentUser
        );

        docsPageRepository.save(duplicatedDocsPage);
    }

    private void updateApiDocuement(UpdateApiPageRequest request, ApiDocument apiDocument) {
        ApiDocument.RequestInfo requestInfo = apiDocumentMapper.toRequestInfo(request.request());
        ApiDocument.ResponseInfo responseInfo = apiDocumentMapper.toResponseInfo(request.response());
        apiDocument.updateDocument(requestInfo, responseInfo);
        apiDocumentCommandService.update(apiDocument);
    }
}

