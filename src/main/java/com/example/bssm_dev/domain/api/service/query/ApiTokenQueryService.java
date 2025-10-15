package com.example.bssm_dev.domain.api.service.query;

import com.example.bssm_dev.common.dto.CursorPage;
import com.example.bssm_dev.domain.api.dto.response.SecretApiTokenResponse;
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
@Transactional(readOnly = true)
public class ApiTokenQueryService {
    private final ApiTokenRepository apiTokenRepository;
    private final ApiTokenMapper apiTokenMapper;

    public ApiToken findById(Long apiTokenId) {
        return  apiTokenRepository.findById(apiTokenId)
                .orElseThrow(ApiTokenNotFoundException::raise);
    }
    
    public CursorPage<SecretApiTokenResponse> getAllApiTokens(User user, Long cursor, Integer size) {
        Pageable pageable = PageRequest.of(0, size);
        
        Slice<ApiToken> apiTokenSlice = apiTokenRepository.findAllByUserIdWithCursorOrderByApiTokenIdDesc(
                user.getUserId(), 
                cursor, 
                pageable
        );

        List<SecretApiTokenResponse> responses = apiTokenMapper.toSecretListResponse(apiTokenSlice);

        return new CursorPage<>(responses, apiTokenSlice.hasNext());
    }
}
