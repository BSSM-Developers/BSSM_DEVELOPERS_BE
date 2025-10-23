package com.example.bssm_dev.domain.docs.service.query;

import com.example.bssm_dev.domain.api.model.Api;
import com.example.bssm_dev.domain.docs.exception.ApiDocumentNotFoundException;
import com.example.bssm_dev.domain.docs.model.ApiDocument;
import com.example.bssm_dev.domain.docs.repository.ApiDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApiDocumentQueryService {
    private final ApiDocumentRepository apiDocumentRepository;

    public Map<Long, ApiDocument> findApiDocumentsByApiIds(List<Long> apiIds) {
        return apiDocumentRepository.findByApiIdIn(apiIds).stream()
                .collect(Collectors.toMap(ApiDocument::getApiId, doc -> doc));
    }

    public ApiDocument findByApiId(Api api) {
        return apiDocumentRepository.findByApiId(api.getApiId())
                .orElseThrow(ApiDocumentNotFoundException::raise);
    }
}
