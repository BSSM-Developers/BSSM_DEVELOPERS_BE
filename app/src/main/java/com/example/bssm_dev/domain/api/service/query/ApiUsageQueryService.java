package com.example.bssm_dev.domain.api.service.query;

import com.example.bssm_dev.common.dto.CursorPage;
import com.example.bssm_dev.domain.api.dto.response.ApiUsageResponse;
import com.example.bssm_dev.domain.api.mapper.ApiUsageMapper;
import com.example.bssm_dev.domain.api.model.ApiUsage;
import com.example.bssm_dev.domain.api.repository.ApiUsageRepository;
import com.example.bssm_dev.domain.api.exception.UnauthorizedApiUsageAccessException;
import com.example.bssm_dev.domain.api.model.Api;
import com.example.bssm_dev.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiUsageQueryService {
    private final ApiUsageRepository apiUsageRepository;
    private final ApiUsageMapper apiUsageMapper;
    private final ApiQueryService apiQueryService;
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
