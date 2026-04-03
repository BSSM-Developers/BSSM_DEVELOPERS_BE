package com.example.bssm_dev.domain.api.event.listner;

import com.example.bssm_dev.domain.api.model.ApiUseReason;
import com.example.bssm_dev.domain.api.repository.ApiUseReasonRepository;
import com.example.bssm_dev.domain.docs.model.event.DocsDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DocsDeletedEventListener {

    private final ApiUseReasonRepository apiUseReasonRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional("transactionManager")
    public void markApisAsDeleted(DocsDeletedEvent event) {
        if (event.apiIds().isEmpty()) {
            return;
        }
        List<ApiUseReason> reasons = apiUseReasonRepository.findAllByApiIdIn(event.apiIds());
        reasons.forEach(ApiUseReason::markAsDeleted);
        apiUseReasonRepository.saveAll(reasons);
    }
}
