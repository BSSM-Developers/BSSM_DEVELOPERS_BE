package com.example.bssm_dev.domain.api.mapper;
import com.example.bssm_dev.domain.api.dto.response.ApiTokenListResponse;
import com.example.bssm_dev.domain.api.dto.response.ApiTokenResponse;
import com.example.bssm_dev.domain.api.dto.response.ApiTokenWithDocsResponse;
import com.example.bssm_dev.domain.api.dto.response.ApiUsageWithDocsResponse;
import com.example.bssm_dev.domain.api.dto.response.SecretApiTokenResponse;
import com.example.bssm_dev.domain.api.model.ApiToken;
import com.example.bssm_dev.domain.api.model.ApiUsage;
import com.example.bssm_dev.domain.docs.dto.response.DocsPageBlockResponse;
import com.example.bssm_dev.domain.docs.model.ContentDocsPage;
import com.example.bssm_dev.domain.docs.model.DocsPageBlock;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ApiTokenMapper {
    private final ApiUsageMapper apiUsageMapper;
    
    public SecretApiTokenResponse toSecretApiTokenResponse(ApiToken apiToken, String plainSecretKey) {
        List<String> origins = apiToken.getTokenDomains().stream()
                .map(tokenDomain -> tokenDomain.getOrigin())
                .toList();
        
        return new SecretApiTokenResponse(
                apiToken.getApiTokenId(),
                apiToken.getApiTokenName(),
                apiToken.getApiTokenUUID(),
                plainSecretKey,
                origins
        );
    }

    public List<ApiTokenListResponse> toListResponse(Slice<ApiToken> apiTokenSlice) {
        return apiTokenSlice.stream()
                .map(this::toListResponseItem)
                .toList();
    }

    private ApiTokenListResponse toListResponseItem(ApiToken apiToken) {
        return new ApiTokenListResponse(
                apiToken.getApiTokenId(),
                apiToken.getApiTokenName(),
                apiToken.getApiTokenUUID(),
                apiToken.getState()
        );
    }

    public List<ApiTokenResponse> toApiTokenResponseList(Slice<ApiToken> apiTokenSlice) {
        return apiTokenSlice.stream()
                .map(this::toApiTokenResponse)
                .toList();
    }

    public ApiTokenResponse toApiTokenResponse(ApiToken apiToken) {
        List<String> origins = apiToken.getTokenDomains().stream()
                .map(tokenDomain -> tokenDomain.getOrigin())
                .toList();

        return new ApiTokenResponse(
                apiToken.getApiTokenId(),
                apiToken.getApiTokenName(),
                apiToken.getApiTokenUUID(),
                apiToken.getState(),
                origins,
                apiUsageMapper.toSummaryListResponse(apiToken.getApiUsageList())
        );
    }

    public ApiTokenWithDocsResponse toApiTokenWithDocsResponse(
            ApiToken apiToken,
            Map<String, ContentDocsPage> pageMap,
            Map<String, String> groupIdToApiId
    ) {
        List<String> origins = apiToken.getTokenDomains().stream()
                .map(tokenDomain -> tokenDomain.getOrigin())
                .toList();

        List<ApiUsageWithDocsResponse> registeredApis = apiToken.getApiUsageList().stream()
                .map(usage -> {
                    String apiId = groupIdToApiId.get(usage.getApiGroupId());
                    return toApiUsageWithDocsResponse(usage, apiId != null ? pageMap.get(apiId) : null);
                })
                .toList();

        return new ApiTokenWithDocsResponse(
                apiToken.getApiTokenId(),
                apiToken.getApiTokenName(),
                apiToken.getApiTokenUUID(),
                apiToken.getState(),
                origins,
                registeredApis
        );
    }

    private ApiUsageWithDocsResponse toApiUsageWithDocsResponse(
            ApiUsage usage,
            ContentDocsPage page
    ) {
        List<DocsPageBlockResponse> docsBlocks = page != null
                ? page.getDocsBlocks().stream().map(this::toBlockResponse).toList()
                : List.of();

        return new ApiUsageWithDocsResponse(
                usage.getApiGroupId(),
                usage.getName(),
                usage.getEndpoint(),
                usage.getMethod(),
                usage.getApiUseState().name(),
                docsBlocks
        );
    }

    private DocsPageBlockResponse toBlockResponse(DocsPageBlock block) {
        return new DocsPageBlockResponse(
                block.getId(),
                block.getMappedId(),
                block.getModuleName(),
                block.getContent()
        );
    }
}
