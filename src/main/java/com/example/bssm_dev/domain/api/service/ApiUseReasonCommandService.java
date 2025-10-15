package com.example.bssm_dev.domain.api.service;

import com.example.bssm_dev.domain.api.dto.request.CreateApiUseReasonRequest;
import com.example.bssm_dev.domain.api.event.ApiUseReasonCreatedEvent;
import com.example.bssm_dev.domain.api.exception.UnauthorizedApiTokenAccessException;
import com.example.bssm_dev.domain.api.mapper.ApiUseReasonMapper;
import com.example.bssm_dev.domain.api.model.Api;
import com.example.bssm_dev.domain.api.model.ApiToken;
import com.example.bssm_dev.domain.api.model.ApiUseReason;
import com.example.bssm_dev.domain.api.repository.ApiUseReasonRepository;
import com.example.bssm_dev.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ApiUseReasonCommandService {
    private final ApiUseReasonRepository apiUseReasonRepository;
    private final ApiUseReasonMapper apiUseReasonMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final ApiQueryService apiQueryService;
    private final ApiTokenQueryService apiTokenQueryService;


    public void createApiUseReason(CreateApiUseReasonRequest request, User user, Long apiTokenId) {
        Api api = apiQueryService.findById(request.apiId());
        ApiToken apiToken = apiTokenQueryService.findById(apiTokenId);

        boolean equalsUser = user.equals(apiToken.getUser());
        if (!equalsUser) {
            throw UnauthorizedApiTokenAccessException.raise();
        }

        ApiUseReason apiUseReason = apiUseReasonMapper.toApiUserReason(request, user, api, apiToken);
        ApiUseReason savedApiUseReason = apiUseReasonRepository.save(apiUseReason);

        // 이벤트 발행
        eventPublisher.publishEvent(new ApiUseReasonCreatedEvent(
                savedApiUseReason,
                api,
                apiToken
            )
        );
    }
}
