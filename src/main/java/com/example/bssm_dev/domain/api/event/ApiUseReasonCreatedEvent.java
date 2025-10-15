package com.example.bssm_dev.domain.api.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiUseReasonCreatedEvent {
    private final Long apiUseReasonId;
    private final Long apiId;
    private final Long userId;
    private final Boolean autoApproval;
}
