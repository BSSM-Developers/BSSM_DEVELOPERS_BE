package com.example.bssm_dev.domain.docs.event.listener;

import com.example.bssm_dev.domain.api.event.ApiUseReasonApprovedEvent;
import com.example.bssm_dev.domain.api.model.Api;
import com.example.bssm_dev.domain.docs.model.Docs;
import com.example.bssm_dev.domain.docs.model.DocsPage;
import com.example.bssm_dev.domain.docs.service.query.DocsPageQueryService;
import com.example.bssm_dev.domain.docs.service.query.DocsQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DocsTokenIncreaseEventListener {
    private final DocsPageQueryService docsPageQueryService;
    private final DocsQueryService docsQueryService;

    @EventListener
    @Transactional("mongoTransactionManager")
    public void handleApiUseReasonApproved(ApiUseReasonApprovedEvent event) {
        Api api = event.api();
        
        log.info("ApiUseReasonApprovedEvent received for api id: {}", api.getApiId());

        DocsPage docsPage = docsPageQueryService.findById(api.getApiId());
        log.info("DocsPage found: docsId={}", docsPage.getDocsId());
        
        Docs docs = docsQueryService.findById(docsPage.getDocsId());
        docs.incrementTokenCount();

        log.info("Token count incremented for docsId: {}, new count: {}", docs.getId(), docs.getTokenCount());
    }
}
