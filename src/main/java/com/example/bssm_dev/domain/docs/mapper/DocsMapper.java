package com.example.bssm_dev.domain.docs.mapper;

import com.example.bssm_dev.domain.api.model.Api;
import com.example.bssm_dev.domain.docs.dto.request.*;
import com.example.bssm_dev.domain.docs.dto.response.*;
import com.example.bssm_dev.domain.docs.dto.response.ApiDetailResponse;
import com.example.bssm_dev.domain.docs.model.ApiPage;
import com.example.bssm_dev.domain.docs.model.Docs;
import com.example.bssm_dev.domain.docs.model.DocsPage;
import com.example.bssm_dev.domain.docs.model.DocsSection;
import com.example.bssm_dev.domain.docs.model.ApiDocument;
import com.example.bssm_dev.domain.docs.model.type.DocsType;
import com.example.bssm_dev.domain.docs.model.type.PageType;
import com.example.bssm_dev.domain.docs.policy.ApiPolicy;
import com.example.bssm_dev.domain.user.model.User;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class DocsMapper {

    public Docs toOriginalDocs(CreateOriginalDocsRequest request, User creator) {
        Docs docs = Docs.of(
                creator,
                request.docsTitle(),
                request.docsDescription(),
                DocsType.ORIGINAL,
                request.domain(),
                request.repositoryUrl(),
                request.autoApproval()
        );
        boolean hasDocsSection = request.docsSections() != null;
        if (hasDocsSection) {
            List<DocsSection> sectionList = new ArrayList<>();

            for (int i = 0; i < request.docsSections().size(); i++) {
                CreateDocsSectionRequest sectionRequest = request.docsSections().get(i);
                DocsSection section = toSectionEntity(sectionRequest, docs, creator, i);
                sectionList.add(section);
            }

            docs.addSectionList(sectionList);
        }

        return docs;
    }

    private DocsSection toSectionEntity(CreateDocsSectionRequest request, Docs docs, User creator, int order) {
        DocsSection section = DocsSection.of(
                docs,
                request.docsSectionTitle(),
                order
        );
        boolean hasDocsPage = request.docsPages() != null;
        if (hasDocsPage) {
            List<DocsPage> pageList = new ArrayList<>();
            for (int i = 0; i < request.docsPages().size(); i++) {
                CreateDocsPageRequest pageRequest = request.docsPages().get(i);
                DocsPage page = toPageEntity(pageRequest, section, creator, (long) i);
                pageList.add(page);
            }
            section.addPageList(pageList);
        }
        return section;
    }

    public DocsSection toSectionEntity(AddDocsSectionRequest request, Docs docs, User creator, int order) {
        return DocsSection.of(
                docs,
                request.docsSectionTitle(),
                order
        );
    }

    public DocsPage toPageEntity(AddDocsPageRequest request, DocsSection section, Long order) {
        return DocsPage.of(
                section,
                request.docsPageTitle(),
                request.docsPageDescription(),
                order
        );
    }

    private DocsPage toPageEntity(CreateDocsPageRequest request, DocsSection section, User creator, Long order) {
        DocsPage page = DocsPage.of(
                section,
                request.docsPageTitle(),
                request.docsPageDescription(),
                order
        );

        if (ApiPolicy.canBeApiPage(request)) {
            Api api = Api.of(
                    creator,
                    request.endpoint(),
                    request.method(),
                    request.docsPageTitle(),
                    section.getDocs().getDomain(),
                    section.getDocs().getRepositoryUrl(),
                    section.getDocs().getAutoApproval()
            );

            ApiPage apiPage = ApiPage.of(page, api);
            page.apiPage(apiPage);
        }

        return page;
    }
    public List<DocsListResponse> toListResponse(Slice<Docs> docsSlice) {
        return docsSlice.getContent().stream()
                .map(this::toListResponse)
                .toList();
    }

    public DocsListResponse toListResponse(Docs docs) {
        return new DocsListResponse(
                docs.getDocsId(),
                docs.getTitle(),
                docs.getDescription(),
                docs.getWriterId(),
                docs.getWriterName(),
                docs.getType().name(),
                docs.getDomain(),
                docs.getRepositoryUrl(),
                docs.getAutoApproval()
        );
    }

    public DocsDetailResponse toDetailResponse(Docs docs) {
        List<DocsSectionResponse> sectionResponses = docs.getSections().stream()
                .map(this::toSectionResponse)
                .toList();

        return new DocsDetailResponse(
                docs.getDocsId(),
                docs.getTitle(),
                docs.getDescription(),
                docs.getWriterId(),
                docs.getWriterName(),
                docs.getType().name(),
                docs.getDomain(),
                docs.getRepositoryUrl(),
                docs.getAutoApproval(),
                sectionResponses
        );
    }

    private DocsSectionResponse toSectionResponse(DocsSection section) {
        List<DocsPageResponse> pageResponses = section.getPages().stream()
                .map(this::toPageResponse)
                .toList();

        return new DocsSectionResponse(
                section.getDocsSectionId(),
                section.getTitle(),
                section.getOrder(),
                pageResponses
        );
    }

    private DocsPageResponse toPageResponse(DocsPage page) {
        ApiDetailResponse apiDetail = null;
        PageType pageType = PageType.MARKDOWN;
        
        if (page.isApiPage()) {
            apiDetail = toApiDetailResponse(page.getApiPage());
            pageType = PageType.API;
        }

        return new DocsPageResponse(
                page.getDocsPageId(),
                page.getTitle(),
                page.getDescription(),
                page.getOrder(),
                pageType.toString(),
                apiDetail
        );
    }

    private ApiDetailResponse toApiDetailResponse(ApiPage apiPage) {
        Api api = apiPage.getApi();
        return new ApiDetailResponse(
                api.getApiId(),
                api.getEndpoint(),
                api.getMethod(),
                api.getName(),
                api.getDomain(),
                api.getRepositoryUrl(),
                null
        );
    }

    public DocsDetailResponse enrichWithApiDocuments(DocsDetailResponse response, Map<Long, ApiDocument> apiDocumentMap) {
        List<DocsSectionResponse> enrichedSections = response.sections().stream()
                .map(section -> new DocsSectionResponse(
                        section.docsSectionId(),
                        section.title(),
                        section.order(),

                        section.pages().stream()
                                .map(page -> enrichPageWithApiDocument(page, apiDocumentMap))
                                .toList()
                ))
                .toList();

        return new DocsDetailResponse(
                response.docsId(),
                response.title(),
                response.description(),
                response.writerId(),
                response.writer(),
                response.type(),
                response.domain(),
                response.repositoryUrl(),
                response.autoApproval(),
                enrichedSections
        );
    }

    private DocsPageResponse enrichPageWithApiDocument(DocsPageResponse page, Map<Long, ApiDocument> apiDocumentMap) {
        if (page.apiDetail() == null) {
            return page;
        }

        ApiDocument apiDocument = apiDocumentMap.get(page.apiId());
        if (apiDocument == null) {
            return page;
        }

        ApiDocument.RequestInfo request = apiDocument.getRequest();
        ApiDocument.ResponseInfo response = apiDocument.getResponse();

        ApiDetailResponse.ApiDocumentResponse documentResponse = new ApiDetailResponse.ApiDocumentResponse(
                new ApiDetailResponse.RequestInfoResponse(
                        request.getApplicationType(),
                        request.getHeader(),
                        request.getPathParams(),
                        request.getQueryParams(),
                        request.getBody(),
                        request.getCookie()
                ),
                new ApiDetailResponse.ResponseInfoResponse(
                        response.getApplicationType(),
                        response.getHeader(),
                        response.getStatusCode(),
                        response.getBody(),
                        response.getCookie()
                )
        );
        ApiDetailResponse pageApiDetail = page.apiDetail();

        ApiDetailResponse enrichedApiDetail = new ApiDetailResponse(
                pageApiDetail.apiId(),
                pageApiDetail.endpoint(),
                pageApiDetail.method(),
                pageApiDetail.name(),
                pageApiDetail.domain(),
                pageApiDetail.repositoryUrl(),
                documentResponse
        );

        return new DocsPageResponse(
                page.docsPageId(),
                page.title(),
                page.description(),
                page.order(),
                page.type(),
                enrichedApiDetail
        );
    }

    public DocsPage toApiPageEntity(AddApiDocsPageRequest request, DocsSection section, User creator, Long order) {
        DocsPage page = DocsPage.of(
                section,
                request.docsPageTitle(),
                request.docsPageDescription(),
                order
        );

        Docs docs = section.getDocs();
        Api api = Api.of(
                creator,
                request.endpoint(),
                request.method(),
                request.docsPageTitle(),
                docs.getDomain(),
                docs.getRepositoryUrl(),
                docs.getAutoApproval()
        );

        ApiPage apiPage = ApiPage.of(page, api);
        page.apiPage(apiPage);

        return page;
    }

    public Docs toCustomDocs(CreateCustomDocsRequest request, User creator) {
        Docs docs = Docs.of(
                creator,
                request.docsTitle(),
                request.docsDescription(),
                DocsType.CUSTOMIZE,
                request.domain(),
                request.repositoryUrl(),
                request.autoApproval()
        );
        return docs;
    }
}
