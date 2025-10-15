package com.example.bssm_dev.domain.docs.event.listner;

import com.example.bssm_dev.domain.docs.event.DocsCreatedEvent;
import com.example.bssm_dev.domain.docs.service.ApiDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class DocsCreatedEventListner {
    private final ApiDocumentService apiDocumentService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleDocsCreated(DocsCreatedEvent event) {
        apiDocumentService.saveApiDocuments(event.getApiDocuments());
    }
}
