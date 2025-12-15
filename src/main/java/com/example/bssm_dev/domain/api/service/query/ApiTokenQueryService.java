package com.example.bssm_dev.domain.api.service.query;

import com.example.bssm_dev.common.dto.CursorPage;
import com.example.bssm_dev.domain.api.dto.response.ApiTokenListResponse;
import com.example.bssm_dev.domain.api.dto.response.ApiTokenResponse;
import com.example.bssm_dev.domain.api.dto.response.SecretApiTokenResponse;
import com.example.bssm_dev.domain.api.exception.UnauthorizedApiTokenAccessException;
import com.example.bssm_dev.domain.api.exception.ApiTokenNotFoundException;
import com.example.bssm_dev.domain.api.mapper.ApiTokenMapper;
import com.example.bssm_dev.domain.api.model.ApiToken;
import com.example.bssm_dev.domain.api.repository.ApiTokenRepository;
import com.example.bssm_dev.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(value = "transactionManager", readOnly = true)
public class ApiTokenQueryService {
    private final ApiTokenRepository apiTokenRepository;
    private final ApiTokenMapper apiTokenMapper;

    public ApiToken findById(Long apiTokenId) {
        return  apiTokenRepository.findById(apiTokenId)
                .orElseThrow(ApiTokenNotFoundException::raise);
    }


    public ApiToken findByTokenClientId(String token) {
        return apiTokenRepository.findByTokenUUID(token)
                .orElseThrow(ApiTokenNotFoundException::raise);
    }
    
    public CursorPage<ApiTokenListResponse> getAllApiTokens(User user, Long cursor, Integer size) {
        Pageable pageable = PageRequest.of(0, size);
        
        Slice<ApiToken> apiTokenSlice = apiTokenRepository.findAllByUserIdWithCursorOrderByApiTokenIdDesc(
                user.getUserId(), 
                cursor, 
                pageable
        );

        List<ApiTokenListResponse> apiTokenListResponseList = apiTokenMapper.toListResponse(apiTokenSlice);

        return new CursorPage<>(apiTokenListResponseList, apiTokenSlice.hasNext());
    }

    public ApiTokenResponse getApiTokenDetail(User user, Long apiTokenId) {
        ApiToken apiToken = apiTokenRepository.findById(apiTokenId)
                .orElseThrow(ApiTokenNotFoundException::raise);

        // 사용자 권한 확인
        boolean equalsUser = apiToken.isOwner(user);
        if (!equalsUser) throw UnauthorizedApiTokenAccessException.raise();
        
        return apiTokenMapper.toApiTokenResponse(apiToken);
    }
}
