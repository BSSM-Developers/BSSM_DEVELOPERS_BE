package com.example.bssm_dev.domain.api.mapper;

import com.example.bssm_dev.domain.api.dto.response.ApiTokenResponse;
import com.example.bssm_dev.domain.api.dto.response.ApiUsageSummaryResponse;
import com.example.bssm_dev.domain.api.dto.response.SecretApiTokenResponse;
import com.example.bssm_dev.domain.api.model.ApiToken;
import com.example.bssm_dev.domain.user.model.User;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApiTokenMapper {
    
    public ApiTokenResponse toResponse(ApiToken apiToken) {
        return new ApiTokenResponse(
                apiToken.getApiTokenId(),
                apiToken.getUser().getUserId(),
                apiToken.getSecretKey(),
                apiToken.getApiTokenName(),
                apiToken.getApiTokenUUID()
        );
    }

    public SecretApiTokenResponse toSecretResponse(ApiToken apiToken, List<ApiUsageSummaryResponse> registeredApis) {
        return new SecretApiTokenResponse(
                apiToken.getApiTokenId(),
                apiToken.getApiTokenName(),
                apiToken.getApiTokenUUID(),
                registeredApis
        );
    }


}
