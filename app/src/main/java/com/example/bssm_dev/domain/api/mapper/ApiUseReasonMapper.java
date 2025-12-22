package com.example.bssm_dev.domain.api.mapper;

import com.example.bssm_dev.domain.api.dto.request.CreateApiUseReasonRequest;
import com.example.bssm_dev.domain.api.dto.response.ApiUseReasonResponse;
import com.example.bssm_dev.domain.api.model.Api;
import com.example.bssm_dev.domain.api.model.ApiToken;
import com.example.bssm_dev.domain.api.model.ApiUseReason;
import com.example.bssm_dev.domain.api.model.type.ApiUseState;
import com.example.bssm_dev.domain.user.model.User;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApiUseReasonMapper {
    
    public ApiUseReasonResponse toResponse(ApiUseReason apiUseReason) {
        return new ApiUseReasonResponse(
                apiUseReason.getApiUseReasonId(),
                apiUseReason.getWriter().getUserId(),
                apiUseReason.getApiUseReason(),
                apiUseReason.getApiUseState().name()
        );
    }

    public ApiUseReason toApiUserReason(CreateApiUseReasonRequest request, User user, Api api, ApiToken apiToken) {
        return ApiUseReason.of(
                user,
                api,
                apiToken,
                request.apiUseReason(),
                ApiUseState.PENDING
        );
    }
    
    public List<ApiUseReasonResponse> toListResponse(Slice<ApiUseReason> apiUseReasonSlice) {
        return apiUseReasonSlice.getContent().stream()
                .map(this::toResponse)
                .toList();
    }
}

