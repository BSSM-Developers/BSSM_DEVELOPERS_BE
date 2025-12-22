package com.example.bssm_dev.domain.api.mapper;
import com.example.bssm_dev.domain.api.dto.response.ApiTokenListResponse;
import com.example.bssm_dev.domain.api.dto.response.ApiTokenResponse;
import com.example.bssm_dev.domain.api.dto.response.SecretApiTokenResponse;
import com.example.bssm_dev.domain.api.model.ApiToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ApiTokenMapper {
    private final ApiUsageMapper apiUsageMapper;
    
    public SecretApiTokenResponse toSecretApiTokenResponse(ApiToken apiToken, String plainSecretKey) {
        List<String> domains = apiToken.getTokenDomains().stream()
                .map(tokenDomain -> tokenDomain.getDomain())
                .toList();
        
        return new SecretApiTokenResponse(
                apiToken.getApiTokenId(),
                apiToken.getApiTokenName(),
                apiToken.getApiTokenUUID(),
                plainSecretKey,
                domains
        );
    }

    public List<ApiTokenListResponse> toListResponse(Slice<ApiToken> apiTokenSlice) {
        return apiTokenSlice.stream()
                .map(this::toListResponseItem)
                .toList();
    }

    private ApiTokenListResponse toListResponseItem(ApiToken apiToken) {
        return new ApiTokenListResponse(
                apiToken.getApiTokenId(),
                apiToken.getApiTokenName(),
                apiToken.getApiTokenUUID()
        );
    }

    public List<ApiTokenResponse> toApiTokenResponseList(Slice<ApiToken> apiTokenSlice) {
        return apiTokenSlice.stream()
                .map(this::toApiTokenResponse)
                .toList();
    }

    public ApiTokenResponse toApiTokenResponse(ApiToken apiToken) {
        List<String> domains = apiToken.getTokenDomains().stream()
                .map(tokenDomain -> tokenDomain.getDomain())
                .toList();
        
        return new ApiTokenResponse(
                apiToken.getApiTokenId(),
                apiToken.getApiTokenName(),
                apiToken.getApiTokenUUID(),
                domains,
                apiUsageMapper.toSummaryListResponse(apiToken.getApiUsageList())
        );
    }
}
