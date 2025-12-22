package com.example.bssm_dev.domain.api.event;

import com.example.bssm_dev.domain.api.model.Api;
import com.example.bssm_dev.domain.api.model.ApiToken;
import com.example.bssm_dev.domain.api.model.ApiUseReason;
import com.example.bssm_dev.domain.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiUseReasonCreatedEvent {
    private final ApiUseReason apiUseReason;
    private final Api api;
    private final ApiToken currentApiToken;
}
