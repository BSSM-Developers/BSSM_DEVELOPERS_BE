package com.example.bssm_dev.domain.docs.service.command;

import com.example.bssm_dev.domain.api.event.ApiUseReasonCreatedEvent;
import com.example.bssm_dev.domain.api.exception.UnauthorizedApiTokenAccessException;
import com.example.bssm_dev.domain.api.model.Api;
import com.example.bssm_dev.domain.api.model.ApiToken;
import com.example.bssm_dev.domain.api.model.ApiUseReason;
import com.example.bssm_dev.domain.api.model.type.ApiUseState;
import com.example.bssm_dev.domain.api.repository.ApiUseReasonRepository;
import com.example.bssm_dev.domain.api.service.query.ApiQueryService;
import com.example.bssm_dev.domain.api.service.query.ApiTokenQueryService;
import com.example.bssm_dev.domain.docs.exception.DocsNoApiPagesException;
import com.example.bssm_dev.domain.docs.exception.DocsPageNotFoundException;
import com.example.bssm_dev.domain.docs.model.ContentDocsPage;
import com.example.bssm_dev.domain.docs.model.Docs;
import com.example.bssm_dev.domain.docs.model.DocsPage;
import com.example.bssm_dev.domain.docs.model.ReferenceDocsPage;
import com.example.bssm_dev.domain.docs.repository.DocsPageRepository;
import com.example.bssm_dev.domain.docs.service.query.DocsQueryService;
import com.example.bssm_dev.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional("transactionManager")
public class DocsApiUseRequestCommandService {
    private final DocsQueryService docsQueryService;
    private final DocsPageRepository docsPageRepository;
    private final ApiQueryService apiQueryService;
    private final ApiTokenQueryService apiTokenQueryService;
    private final ApiUseReasonRepository apiUseReasonRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void applyForDocs(String docsId, Long apiTokenId, String useReason, User currentUser) {
        Docs docs = docsQueryService.findById(docsId);
        ApiToken apiToken = apiTokenQueryService.findById(apiTokenId);

        boolean isOwner = currentUser.equals(apiToken.getUser());
        if (!isOwner) throw UnauthorizedApiTokenAccessException.raise();

        List<DocsPage> pages = docsPageRepository.findAllByDocsId(docsId);

        List<Api> targetApis = switch (docs.getType()) {
            case ORIGINAL -> resolveApisFromOriginalDocs(pages);
            case CUSTOMIZE -> resolveApisFromCustomDocs(pages);
        };

        if (targetApis.isEmpty()) throw DocsNoApiPagesException.raise();

        targetApis.forEach(api -> {
            boolean alreadyExists = apiToken.checkApiUsage(api);
            if (!alreadyExists) {
                ApiUseReason saved = apiUseReasonRepository.save(
                        ApiUseReason.of(currentUser, api, apiToken, useReason, ApiUseState.PENDING)
                );
                eventPublisher.publishEvent(new ApiUseReasonCreatedEvent(saved, api, apiToken));
            }
        });
    }

    private List<Api> resolveApisFromOriginalDocs(List<DocsPage> pages) {
        return pages.stream()
                .filter(p -> p instanceof ContentDocsPage cp && cp.getEndpoint() != null)
                .map(p -> apiQueryService.findById(p.getId()))
                .toList();
    }

    private List<Api> resolveApisFromCustomDocs(List<DocsPage> pages) {
        return pages.stream()
                .filter(p -> p instanceof ReferenceDocsPage)
                .map(p -> (ReferenceDocsPage) p)
                .map(ref -> docsPageRepository
                        .findByDocsIdAndMappedId(ref.getSourceDocsId(), ref.getSourceMappedId())
                        .orElseThrow(DocsPageNotFoundException::raise))
                .map(sourcePage -> apiQueryService.findById(sourcePage.getId()))
                .toList();
    }
}
