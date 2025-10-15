package com.example.bssm_dev.domain.api.service.query;

import com.example.bssm_dev.domain.api.exception.ApiNotFoundException;
import com.example.bssm_dev.domain.api.model.Api;
import com.example.bssm_dev.domain.api.repository.ApiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiQueryService {
    private final ApiRepository apiRepository;

    public Api findById(Long apiId) {
        return apiRepository.findById(apiId)
                .orElseThrow(ApiNotFoundException::raise);
    }
}
