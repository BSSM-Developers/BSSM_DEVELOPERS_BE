package com.example.bssm_dev.domain.api.service.query;

import com.example.bssm_dev.common.dto.CursorPage;
import com.example.bssm_dev.domain.api.dto.response.ApiUsageSummaryResponse;
import com.example.bssm_dev.domain.api.dto.response.SecretApiTokenResponse;
import com.example.bssm_dev.domain.api.exception.ApiTokenNotFoundException;
import com.example.bssm_dev.domain.api.mapper.ApiTokenMapper;
import com.example.bssm_dev.domain.api.mapper.ApiUsageMapper;
import com.example.bssm_dev.domain.api.model.ApiToken;
import com.example.bssm_dev.domain.api.model.ApiUsage;
import com.example.bssm_dev.domain.api.repository.ApiTokenRepository;
import com.example.bssm_dev.domain.api.repository.ApiUsageRepository;
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
@Transactional(readOnly = true)
public class ApiTokenQueryService {
    private final ApiTokenRepository apiTokenRepository;
    private final ApiTokenMapper apiTokenMapper;
    private final ApiUsageRepository apiUsageRepository;
    private final ApiUsageMapper apiUsageMapper;

    public ApiToken findById(Long apiTokenId) {
        return  apiTokenRepository.findById(apiTokenId)
                .orElseThrow(ApiTokenNotFoundException::raise);
    }


    public ApiToken findByTokenClientId(String token) {
        return apiTokenRepository.findByTokenUUID(token)
                .orElseThrow(ApiTokenNotFoundException::raise);
    }
    
    public CursorPage<SecretApiTokenResponse> getAllApiTokens(User user, Long cursor, Integer size) {
        Pageable pageable = PageRequest.of(0, size);
        
        Slice<ApiToken> apiTokenSlice = apiTokenRepository.findAllByUserIdWithCursorOrderByApiTokenIdDesc(
                user.getUserId(), 
                cursor, 
                pageable
        );

        List<SecretApiTokenResponse> responses = apiTokenSlice.getContent().stream()
                .map(apiToken -> {
                    // 각 ApiToken에 등록된 API들 조회
                    List<ApiUsage> apiUsages = apiUsageRepository.findAllByApiToken(apiToken);
                    List<ApiUsageSummaryResponse> registeredApis = apiUsageMapper.toSummaryListResponse(apiUsages);
                    return apiTokenMapper.toSecretResponse(apiToken, registeredApis);
                })
                .toList();

        return new CursorPage<>(responses, apiTokenSlice.hasNext());
    }
}
