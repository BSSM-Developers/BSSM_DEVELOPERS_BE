package com.example.bssm_dev.domain.api.service.query;

import com.example.bssm_dev.common.dto.CursorPage;
import com.example.bssm_dev.domain.api.dto.response.ApiUseReasonResponse;
import com.example.bssm_dev.domain.api.exception.ApiUseReasonNotFoundException;
import com.example.bssm_dev.domain.api.mapper.ApiUseReasonMapper;
import com.example.bssm_dev.domain.api.model.ApiUseReason;
import com.example.bssm_dev.domain.api.repository.ApiUseReasonRepository;
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
public class ApiUseReasonQueryService {
    private final ApiUseReasonRepository apiUseReasonRepository;
    private final ApiUseReasonMapper apiUseReasonMapper;

    public ApiUseReason findById(Long apiUseReasonId) {
        return apiUseReasonRepository.findById(apiUseReasonId)
                .orElseThrow(ApiUseReasonNotFoundException::raise);
    }
    
    public CursorPage<ApiUseReasonResponse> getAllApiUseReasons(User user, Long cursor, Integer size) {
        Pageable pageable = PageRequest.of(0, size);
        
        Slice<ApiUseReason> apiUseReasonSlice = apiUseReasonRepository.findAllByUserIdWithCursor(
                user.getUserId(),
                cursor,
                pageable
        );
        
        List<ApiUseReasonResponse> responses = apiUseReasonMapper.toListResponse(apiUseReasonSlice);
        
        return new CursorPage<>(responses, apiUseReasonSlice.hasNext());
    }
}

