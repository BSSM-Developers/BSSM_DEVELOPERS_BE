package com.example.bssm_dev.domain.docs.mapper;

import com.example.bssm_dev.domain.api.model.Api;
import com.example.bssm_dev.domain.docs.dto.request.CreateDocsPageRequest;
import com.example.bssm_dev.domain.docs.dto.request.CreateDocsRequest;
import com.example.bssm_dev.domain.docs.dto.request.CreateDocsSectionRequest;
import com.example.bssm_dev.domain.docs.dto.response.DocsResponse;
import com.example.bssm_dev.domain.docs.model.ApiPage;
import com.example.bssm_dev.domain.docs.model.Docs;
import com.example.bssm_dev.domain.docs.model.DocsPage;
import com.example.bssm_dev.domain.docs.model.DocsSection;
import com.example.bssm_dev.domain.docs.model.type.DocsType;
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
                DocsType.CUSTOMIZE,
                request.domain(),
                request.repositoryUrl(),
                request.autoApproval()
        );

        if (request.docsSections() != null) {
            for (int i = 0; i < request.docsSections().size(); i++) {
                CreateDocsSectionRequest sectionRequest = request.docsSections().get(i);
                DocsSection section = toSectionEntity(sectionRequest, docs, creator, (long) i);
                docs.addSection(section);
            }
        }

        return docs;
    }

    private DocsSection toSectionEntity(CreateDocsSectionRequest request, Docs docs, User creator, Long order) {
        DocsSection section = DocsSection.of(
                docs,
                request.docsSectionTitle(),
                order
        );

        if (request.docsPages() != null) {
            for (int i = 0; i < request.docsPages().size(); i++) {
                CreateDocsPageRequest pageRequest = request.docsPages().get(i);
                DocsPage page = toPageEntity(pageRequest, section, creator, (long) i);
                section.addPage(page);
            }
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

        // If type is "api", create Api and ApiPage entities
        if ("api".equalsIgnoreCase(request.type()) && request.method() != null && request.endpoint() != null) {
            Api api = Api.of(
                    creator,
                    request.endpoint(),
                    request.method(),
                    request.docsPageTitle(), // docsPageTitle becomes api name
                    section.getDocs().getDomain(),
                    section.getDocs().getRepositoryUrl(),
                    section.getDocs().getAutoApproval()
            );

            ApiPage apiPage = ApiPage.of(page, api);
            page.setApiPage(apiPage);
        }

        return page;
    }



    public DocsResponse toResponse(Docs docs) {
        return new DocsResponse(
                docs.getDocsId(),
                docs.getTitle(),
                docs.getDescription(),
                docs.getDomain(),
                docs.getRepositoryUrl(),
                docs.getAutoApproval()
        );
    }
}
