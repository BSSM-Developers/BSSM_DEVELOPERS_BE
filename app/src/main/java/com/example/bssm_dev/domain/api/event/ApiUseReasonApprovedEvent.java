package com.example.bssm_dev.domain.api.event;

import com.example.bssm_dev.domain.api.model.Api;
import com.example.bssm_dev.domain.api.model.ApiToken;
import com.example.bssm_dev.domain.api.model.ApiUseReason;

public record ApiUseReasonApprovedEvent (
        ApiUseReason apiUseReason,
        Api api,
        ApiToken apiToken
) {

}
