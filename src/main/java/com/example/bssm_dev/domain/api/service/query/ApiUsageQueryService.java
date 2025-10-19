package com.example.bssm_dev.domain.api.service.query;

import com.example.bssm_dev.domain.api.model.ApiToken;
import com.example.bssm_dev.domain.api.model.ApiUsage;
import com.example.bssm_dev.domain.api.model.type.MethodType;
import com.example.bssm_dev.domain.api.repository.ApiUsageRepository;
import com.example.bssm_dev.domain.api.exception.EndpointNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApiUsageQueryService {
    private final ApiUsageRepository apiUsageRepository;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public ApiUsage findByTokenAndEndpoint(ApiToken apiToken, String endpoint, MethodType methodType) {
        String methodTypeString = methodType.toString();

        // 정확히 일치하는 경로가 있는지 먼저 확인
        Optional<ApiUsage> exactMatch = apiUsageRepository.findByApiTokenAndEndpoint(apiToken, endpoint);
        if (exactMatch.isPresent() && exactMatch.get().getMethod().equals(methodTypeString)) {
            return exactMatch.get();
        }
        
        // endpoint에서 첫 번째 세그먼트 추출 (예: /objects/1 → /objects)
        String endpointPrefix = extractEndpointPrefix(endpoint);
        
        // LIKE로 후보군 조회 + method 필터링
        List<ApiUsage> candidates = apiUsageRepository.findCandidatesByPrefixAndMethod(
                apiToken, 
                endpointPrefix, 
                methodTypeString
        );
        
        // Java에서 정확한 패턴 매칭
        return candidates.stream()
                .filter(usage -> pathMatcher.match(usage.getEndpoint(), endpoint))
                .findFirst()
                .orElseThrow(EndpointNotFoundException::raise);
    }
    
    private String extractEndpointPrefix(String endpoint) {
        // /objects/1/items/2 → /objects
        // /users/123 → /users
        int secondSlashIndex = endpoint.indexOf('/', 1);
        if (secondSlashIndex > 0) {
            return endpoint.substring(0, secondSlashIndex);
        }
        return endpoint;
    }
}
