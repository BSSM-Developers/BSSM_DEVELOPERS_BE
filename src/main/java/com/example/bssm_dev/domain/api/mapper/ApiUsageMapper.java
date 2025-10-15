package com.example.bssm_dev.domain.api.mapper;

import com.example.bssm_dev.domain.api.model.Api;
import com.example.bssm_dev.domain.api.model.ApiToken;
import com.example.bssm_dev.domain.api.model.ApiUsage;
import com.example.bssm_dev.domain.api.model.ApiUseReason;
import org.springframework.stereotype.Component;

@Component
public class ApiUsageMapper {

    public ApiUsage toApiUsage(ApiToken apiToken, Api api, ApiUseReason apiUseReason) {
        return ApiUsage.of(
                apiToken,
                api,
                apiUseReason,
                api.getName(),
                api.getEndpoint()
        );

    }
}
