package com.example.bssm_dev.domain.api.service.query;

import com.example.bssm_dev.common.dto.CursorPage;
import com.example.bssm_dev.domain.api.dto.response.ApiUsageResponse;
import com.example.bssm_dev.domain.api.mapper.ApiUsageMapper;
import com.example.bssm_dev.domain.api.model.ApiToken;
import com.example.bssm_dev.domain.api.model.ApiUsage;
import com.example.bssm_dev.domain.api.model.type.MethodType;
import com.example.bssm_dev.domain.api.model.vo.RequestInfo;
import com.example.bssm_dev.domain.api.repository.ApiUsageRepository;
import com.example.bssm_dev.domain.api.exception.EndpointNotFoundException;
import com.example.bssm_dev.domain.api.exception.UnauthorizedApiUsageAccessException;
import com.example.bssm_dev.domain.api.model.Api;
import com.example.bssm_dev.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApiUsageQueryService {
    private final ApiUsageRepository apiUsageRepository;
    private final ApiUsageMapper apiUsageMapper;
    private final ApiQueryService apiQueryService;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    public ApiUsage findByTokenAndEndpoint(ApiToken apiToken, RequestInfo requestInfo) {

        // 정확히 일치하는 경로가 있는지 먼저 확인
        Optional<ApiUsage> exactMatch = apiUsageRepository.findByApiTokenAndEndpoint(apiToken, requestInfo.endpoint());

        Optional<ApiUsage> validExactMatch = exactMatch
                .filter(match -> match.getMethod().equals(requestInfo.method().toString()));
        if (validExactMatch.isPresent()) {
            return validExactMatch.get();
        }
        
        // endpoint에서 첫 번째 세그먼트 추출 (예: /objects/1 → /objects)
        String endpointPrefix = extractEndpointPrefix(requestInfo.endpoint());
        
        // LIKE로 후보군 조회 + method 필터링
        List<ApiUsage> candidates = apiUsageRepository.findCandidatesByPrefixAndMethod(
                apiToken, 
                endpointPrefix,
                requestInfo.method().toString()
        );
        
        // Java에서 정확한 패턴 매칭
        return candidates.stream()
                .filter(usage -> pathMatcher.match(usage.getEndpoint(), requestInfo.endpoint()))
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

    
    public CursorPage<ApiUsageResponse> getApiUsagesByApiId(User user, String apiId, Long cursor, Integer size) {
        Api api = apiQueryService.findById(apiId);

        boolean isApiCreator = api.isCreator(user);
        if (!isApiCreator) {
            throw UnauthorizedApiUsageAccessException.raise();
        }
        
        Pageable pageable = PageRequest.of(0, size);
        Slice<ApiUsage> apiUsageSlice = apiUsageRepository.findAllByApiIdWithCursor(apiId, cursor, pageable);
        
        List<ApiUsageResponse> responses = apiUsageMapper.toListResponse(apiUsageSlice);
        
        return new CursorPage<>(responses, apiUsageSlice.hasNext());
    }
}

