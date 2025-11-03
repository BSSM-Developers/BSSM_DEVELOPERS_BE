package com.example.bssm_dev.domain.docs.extractor;

import com.example.bssm_dev.domain.docs.dto.ApiDocumentData;
import com.example.bssm_dev.domain.docs.dto.request.CreateOriginalDocsRequest;
import com.example.bssm_dev.domain.docs.policy.ApiPolicy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DocsExtractor {
    public List<ApiDocumentData> extractApiDocuments(CreateOriginalDocsRequest request, Docs savedDocs) {
        List<ApiDocumentData> result = new ArrayList<>();

        boolean isSectionEmpty = request.docsSections() == null || savedDocs.getSections() == null;
        if (isSectionEmpty) return result;

        for (int i = 0; i < savedDocs.getSections().size(); i++) {
            DocsSection section = savedDocs.getSections().get(i);
            CreateDocsSectionRequest sectionRequest = request.docsSections().get(i);

            boolean isPageEmpty = sectionRequest.docsPages() == null || section.getPages() == null;
            if (isPageEmpty) continue;

            for (int j = 0; j < section.getPages().size(); j++) {
                DocsPage page = section.getPages().get(j);
                CreateDocsPageRequest pageRequest = sectionRequest.docsPages().get(j);

                boolean canBeApiPage = ApiPolicy.canBeApiPage(pageRequest);
                if (!canBeApiPage || page.getApiPage() == null) continue;

                Long apiId = page.getApiPage().getApiID();
                result.add(
                        new ApiDocumentData(
                                apiId,
                                pageRequest.request(),
                                pageRequest.response()
                        )
                );
            }
        }

        return result;
    }



    public List<Long> extractApiIds(Docs docs) {
        return docs.getSections().stream()
                .flatMap(section -> section.getPages().stream())
                .filter(DocsPage::isApiPage)
                .map(page -> page.getApiPage().getApiID())
                .toList();
    }


    public List<Long> extractApiIds(DocsSection section) {
        return section.getPages().stream()
                .filter(DocsPage::isApiPage)
                .map(page -> page.getApiPage().getApiID())
                .toList();
    }
}
