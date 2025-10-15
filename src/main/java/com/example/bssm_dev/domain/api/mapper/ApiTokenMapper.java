package com.example.bssm_dev.domain.api.mapper;

import com.example.bssm_dev.domain.api.dto.response.ApiTokenResponse;
import com.example.bssm_dev.domain.api.model.ApiToken;
import com.example.bssm_dev.domain.user.model.User;
import org.springframework.stereotype.Component;

@Component
public class ApiTokenMapper {
    
    public ApiTokenResponse toResponse(ApiToken apiToken) {
        return new ApiTokenResponse(
                apiToken.getApiTokenId(),
                apiToken.getUser().getUserId(),
                apiToken.getSecretKey()
        );
    }

    public ApiToken toApiToken(User user, String secretKey) {
        return ApiToken.of(user, secretKey);
    }
}
