package com.example.bssm_dev.domain.api.service.query;

import com.example.bssm_dev.domain.api.dto.response.TryItTokenResponse;
import com.example.bssm_dev.domain.api.exception.ApiTokenNotFoundException;
import com.example.bssm_dev.domain.api.model.TryItToken;
import com.example.bssm_dev.domain.api.repository.TryItTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(value = "transactionManager", readOnly = true)
public class TryItTokenQueryService {
    private final TryItTokenRepository tryItTokenRepository;

    public TryItTokenResponse findByApiGroupId(String apiGroupId) {
        TryItToken tryItToken = tryItTokenRepository.findByApiApiGroupApiGroupId(apiGroupId)
                .orElseThrow(ApiTokenNotFoundException::raise);

        return new TryItTokenResponse(tryItToken.getApiTokenUUID());
    }
}
