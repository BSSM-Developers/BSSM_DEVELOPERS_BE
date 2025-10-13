package com.example.bssm_dev.domain.docs.mapper;

import com.example.bssm_dev.domain.api.model.Api;
import com.example.bssm_dev.domain.docs.dto.request.CreateDocsPageRequest;
import com.example.bssm_dev.domain.docs.dto.request.CreateDocsRequest;
import com.example.bssm_dev.domain.docs.dto.request.CreateDocsSectionRequest;
import com.example.bssm_dev.domain.docs.model.ApiPage;
import com.example.bssm_dev.domain.docs.model.Docs;
import com.example.bssm_dev.domain.docs.model.DocsPage;
import com.example.bssm_dev.domain.docs.model.DocsSection;
import com.example.bssm_dev.domain.docs.model.type.DocsType;
import com.example.bssm_dev.domain.docs.policy.ApiPolicy;
import com.example.bssm_dev.domain.user.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DocsMapper {

    public Docs toEntity(CreateDocsRequest request, User creator) {
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
}
