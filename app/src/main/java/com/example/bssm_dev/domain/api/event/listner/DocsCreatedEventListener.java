package com.example.bssm_dev.domain.api.event.listner;

import com.example.bssm_dev.domain.api.service.command.ApiCommandService;
import com.example.bssm_dev.domain.docs.model.event.DocsCreatedEvent;
import com.example.bssm_dev.domain.user.model.User;
import com.example.bssm_dev.domain.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class DocsCreatedEventListener {
    private final ApiCommandService apiCommandService;
    private final UserQueryService userQueryService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void createApi(DocsCreatedEvent docsCreatedEvent) {
        log.info("DocsCreatedEvent received for docsId: {}", docsCreatedEvent.docsId());
        
        User creator = userQueryService.findById(docsCreatedEvent.writerId());
        log.info("Creator found: userId={}, name={}", creator.getUserId(), creator.getName());

        apiCommandService.createApisFromDocs(
                docsCreatedEvent.docsId(),
                docsCreatedEvent.sidebar(),
                docsCreatedEvent.docsPages(),
                creator,
                docsCreatedEvent.domain(),
                docsCreatedEvent.repositoryUrl(),
                docsCreatedEvent.autoApproval()
        );
    }

}
