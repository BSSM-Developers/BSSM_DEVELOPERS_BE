package com.example.bssm_dev.domain.api.mapper;

import com.example.bssm_dev.domain.api.dto.response.ApiUsageResponse;
import com.example.bssm_dev.domain.api.dto.response.ApiUsageSummaryResponse;
import com.example.bssm_dev.domain.api.model.Api;
import com.example.bssm_dev.domain.api.model.ApiToken;
import com.example.bssm_dev.domain.api.model.ApiUsage;
import com.example.bssm_dev.domain.api.model.ApiUseReason;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.List;

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
    
    public ApiUsageResponse toResponse(ApiUsage apiUsage) {
        return new ApiUsageResponse(
                apiUsage.getApiToken().getApiTokenId(),
                apiUsage.getApi().getApiId(),
                apiUsage.getName(),
                apiUsage.getEndpoint(),
                apiUsage.getApi().getName(),
                apiUsage.getApi().getDomain(),
                apiUsage.getApi().getMethod(),
                apiUsage.getApiUseReason().getApiUseReasonId().toString(),
                apiUsage.getApiUseReason().getApiUseState().name()
        );
    }
    
    public List<ApiUsageResponse> toListResponse(Slice<ApiUsage> apiUsageSlice) {
        return apiUsageSlice.getContent().stream()
                .map(this::toResponse)
                .toList();
    }
    
    public ApiUsageSummaryResponse toSummaryResponse(ApiUsage apiUsage) {
        return new ApiUsageSummaryResponse(
                apiUsage.getApi().getApiId(),
                apiUsage.getName(),
                apiUsage.getEndpoint(),
                apiUsage.getApi().getMethod(),
                apiUsage.getApiUseReason().getApiUseState().name()
        );
    }
    
    public List<ApiUsageSummaryResponse> toSummaryListResponse(List<ApiUsage> apiUsages) {
        return apiUsages.stream()
                .map(this::toSummaryResponse)
                .toList();
    }
}


