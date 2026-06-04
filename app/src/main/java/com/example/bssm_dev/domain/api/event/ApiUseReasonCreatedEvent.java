package com.example.bssm_dev.domain.api.event;

import com.example.bssm_dev.domain.api.model.Api;
import com.example.bssm_dev.domain.api.model.ApiToken;
import com.example.bssm_dev.domain.api.model.ApiUseReason;
import com.example.bssm_dev.domain.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

public record ApiUseReasonCreatedEvent (
        ApiUseReason apiUseReason,
        Api api,
        ApiToken currentApiToken
) {
}
