package com.example.bssm_dev.domain.api.mapper;
import com.example.bssm_dev.domain.api.dto.response.ApiTokenResponse;
import com.example.bssm_dev.domain.api.dto.response.ApiUsageSummaryResponse;
import com.example.bssm_dev.domain.api.dto.response.SecretApiTokenResponse;
import com.example.bssm_dev.domain.api.model.ApiToken;
import com.example.bssm_dev.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ApiTokenMapper {
    private final ApiUsageMapper apiUsageMapper;
    
    public ApiTokenResponse toResponse(ApiToken apiToken) {
        return new ApiTokenResponse(
                apiToken.getApiTokenId(),
                apiToken.getUser().getUserId(),
                apiToken.getSecretKey(),
                apiToken.getApiTokenName(),
                apiToken.getApiTokenUUID()
        );
    }

    public List<SecretApiTokenResponse> toSecretResponseList(Slice<ApiToken> apiTokenSlice) {
        return apiTokenSlice.stream()
                .map(this::toSecretResponse)
                .toList();
    }

    public SecretApiTokenResponse toSecretResponse(ApiToken apiToken) {
        return new SecretApiTokenResponse(
                apiToken.getApiTokenId(),
                apiToken.getApiTokenName(),
                apiToken.getApiTokenUUID(),
                apiUsageMapper.toSummaryListResponse(apiToken.getApiUsageList())
        );
    }
}
