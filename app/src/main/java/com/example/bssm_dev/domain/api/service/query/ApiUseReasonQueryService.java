package com.example.bssm_dev.domain.api.service.query;

import com.example.bssm_dev.common.dto.CursorPage;
import com.example.bssm_dev.domain.api.dto.response.ApiUseReasonResponse;
import com.example.bssm_dev.domain.api.exception.ApiUseReasonNotFoundException;
import com.example.bssm_dev.domain.api.exception.UnauthorizedApiUseReasonAccessException;
import com.example.bssm_dev.domain.api.model.Api;
import com.example.bssm_dev.domain.api.mapper.ApiUseReasonMapper;
import com.example.bssm_dev.domain.api.model.ApiUseReason;
import com.example.bssm_dev.domain.api.model.type.ApiUseState;
import com.example.bssm_dev.domain.api.repository.ApiUseReasonRepository;
import com.example.bssm_dev.domain.docs.model.DocsPage;
import com.example.bssm_dev.domain.docs.repository.DocsPageRepository;
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
@Transactional(value = "transactionManager", readOnly = true)
public class ApiUseReasonQueryService {
    private final ApiUseReasonRepository apiUseReasonRepository;
    private final ApiUseReasonMapper apiUseReasonMapper;
    private final ApiQueryService apiQueryService;
    private final DocsPageRepository docsPageRepository;

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

    public CursorPage<ApiUseReasonResponse> getApiUseReasonsByState(Long cursor, Integer size, String stateParam) {
        Pageable pageable = PageRequest.of(0, size);

        ApiUseState state = ApiUseState.fromString(stateParam);
        Slice<ApiUseReason> apiUseReasonSlice = fetchApiUseReasonSlice(state, cursor, pageable);

        List<ApiUseReasonResponse> responses = apiUseReasonMapper.toListResponse(apiUseReasonSlice);
        return new CursorPage<>(responses, apiUseReasonSlice.hasNext());
    }
    
    public CursorPage<ApiUseReasonResponse> getApiUseReasonsByMyDocs(User user, String docsId, Long cursor, Integer size) {
        Pageable pageable = PageRequest.of(0, size);
        Slice<ApiUseReason> apiUseReasonSlice;
        if (docsId != null) {
            List<String> apiIds = docsPageRepository.findAllByDocsId(docsId).stream()
                    .map(DocsPage::getId)
                    .toList();
            apiUseReasonSlice = apiUseReasonRepository.findAllByCreatorUserIdAndApiIdsWithCursor(
                    user.getUserId(), apiIds, cursor, pageable);
        } else {
            apiUseReasonSlice = apiUseReasonRepository.findAllByCreatorUserIdWithCursor(
                    user.getUserId(), cursor, pageable);
        }
        List<ApiUseReasonResponse> responses = apiUseReasonMapper.toListResponse(apiUseReasonSlice);
        return new CursorPage<>(responses, apiUseReasonSlice.hasNext());
    }

    public CursorPage<ApiUseReasonResponse> getApiUseReasonsByApiId(User user, String apiId, Long cursor, Integer size) {
        Api api = apiQueryService.findById(apiId);

        boolean isCreator = api.isCreator(user);
        if (!isCreator) {
            throw UnauthorizedApiUseReasonAccessException.raise();
        }

        Pageable pageable = PageRequest.of(0, size);
        Slice<ApiUseReason> apiUseReasonSlice = apiUseReasonRepository.findAllByApiIdWithCursor(apiId, cursor, pageable);

        List<ApiUseReasonResponse> responses = apiUseReasonMapper.toListResponse(apiUseReasonSlice);
        return new CursorPage<>(responses, apiUseReasonSlice.hasNext());
    }

    private Slice<ApiUseReason> fetchApiUseReasonSlice(
            ApiUseState state,
            Long cursor, 
            Pageable pageable
    ) {
        return switch (state) {
            case null -> apiUseReasonRepository.findAllWithCursor(cursor, pageable);
            case PENDING, APPROVED, REJECTED, DELETED -> apiUseReasonRepository.findByStateWithCursor(state, cursor, pageable);
        };
    }
}


