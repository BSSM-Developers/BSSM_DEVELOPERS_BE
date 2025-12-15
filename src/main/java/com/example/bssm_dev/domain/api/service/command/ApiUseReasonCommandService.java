package com.example.bssm_dev.domain.api.service.command;

import com.example.bssm_dev.domain.api.dto.request.CreateApiUseReasonRequest;
import com.example.bssm_dev.domain.api.event.ApiUseReasonCreatedEvent;
import com.example.bssm_dev.domain.api.exception.ApiUsageAlreadyExistsException;
import com.example.bssm_dev.domain.api.exception.UnauthorizedApiTokenAccessException;
import com.example.bssm_dev.domain.api.mapper.ApiUseReasonMapper;
import com.example.bssm_dev.domain.api.model.Api;
import com.example.bssm_dev.domain.api.model.ApiToken;
import com.example.bssm_dev.domain.api.model.ApiUseReason;
import com.example.bssm_dev.domain.api.repository.ApiUseReasonRepository;
import com.example.bssm_dev.domain.api.repository.ApiUsageRepository;
import com.example.bssm_dev.domain.api.model.ApiUsage;
import com.example.bssm_dev.domain.api.exception.ApiUseReasonNotFoundException;
import com.example.bssm_dev.domain.api.exception.UnauthorizedApiUseReasonAccessException;
import com.example.bssm_dev.domain.api.service.query.ApiQueryService;
import com.example.bssm_dev.domain.api.service.query.ApiTokenQueryService;
import com.example.bssm_dev.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional("transactionManager")
public class ApiUseReasonCommandService {
    private final ApiUseReasonRepository apiUseReasonRepository;
    private final ApiUseReasonMapper apiUseReasonMapper;

    private final ApplicationEventPublisher eventPublisher;

    private final ApiQueryService apiQueryService;
    private final ApiTokenQueryService apiTokenQueryService;
    private final ApiUsageCommandService apiUsageCommandService;


    public void createApiUseReason(CreateApiUseReasonRequest request, User currentUser, Long apiTokenId) {
        Api api = apiQueryService.findById(request.apiId());
        ApiToken apiToken = apiTokenQueryService.findById(apiTokenId);

        boolean equalsUser = currentUser.equals(apiToken.getUser());
        if (!equalsUser) throw UnauthorizedApiTokenAccessException.raise();

        boolean alreadyApiUsage = apiToken.checkApiUsage(api);
        if (alreadyApiUsage) throw ApiUsageAlreadyExistsException.raise();

        ApiUseReason apiUseReason = apiUseReasonMapper.toApiUserReason(request, currentUser, api, apiToken);
        ApiUseReason savedApiUseReason = apiUseReasonRepository.save(apiUseReason);

        // 이벤트 발행
        eventPublisher.publishEvent(new ApiUseReasonCreatedEvent(
                savedApiUseReason,
                api,
                apiToken
            )
        );
    }


    public void approveApiUseReason(Long apiUseReasonId, User currentUser) {
        ApiUseReason apiUseReason = apiUseReasonRepository.findById(apiUseReasonId)
                .orElseThrow(ApiUseReasonNotFoundException::raise);

        Api api = apiUseReason.getApi();

        User creator = api.getCreator();
        boolean eqaulsUser = creator.equals(currentUser);
        if (!eqaulsUser) throw UnauthorizedApiUseReasonAccessException.raise();

        ApiToken apiToken = apiUseReason.getApiToken();

        apiUsageCommandService.save(apiToken, api, apiUseReason);
        apiUseReason.approved();
    }


    public void rejectApiUseReason(Long apiUseReasonId, User currentUser) {
        ApiUseReason apiUseReason = apiUseReasonRepository.findById(apiUseReasonId)
                .orElseThrow(ApiUseReasonNotFoundException::raise);

        Api api = apiUseReason.getApi();

        User creator = api.getCreator();
        boolean equalsUser = creator.equals(currentUser);
        if (!equalsUser) throw UnauthorizedApiUseReasonAccessException.raise();

        apiUseReason.rejected();
    }

}
