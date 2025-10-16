package com.example.bssm_dev.domain.api.service.query;

import com.example.bssm_dev.domain.api.exception.ApiUseReasonNotFoundException;
import com.example.bssm_dev.domain.api.model.ApiUseReason;
import com.example.bssm_dev.domain.api.repository.ApiUseReasonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApiUseReasonQueryService {
    private final ApiUseReasonRepository apiUseReasonRepository;

    public ApiUseReason findById(Long apiUseReasonId) {
        return apiUseReasonRepository.findById(apiUseReasonId)
                .orElseThrow(ApiUseReasonNotFoundException::raise);
    }
}
