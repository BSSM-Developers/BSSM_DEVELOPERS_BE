package com.example.bssm_dev.domain.api.model;

import com.example.bssm_dev.domain.api.model.key.ApiUsageId;
import com.example.bssm_dev.domain.api.model.type.ApiUseState;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
@ToString
public class ApiUsage {
    @EmbeddedId
    private ApiUsageId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("apiTokenId")
    @JoinColumn(name = "api_token_id", nullable = false)
    private ApiToken apiToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("apiId")
    @JoinColumn(name = "api_id", nullable = false)
    private Api api;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "api_use_reason_id", nullable = false)
    private ApiUseReason apiUseReason;

    @Column(length = 30, nullable = false)
    private String name;

    @Column(nullable = false)
    private String endpoint;

    public static ApiUsage of(ApiToken apiToken, Api api, ApiUseReason apiUseReason, String name, String endpoint) {
        return ApiUsage.builder()
                .id(new ApiUsageId(apiToken.getApiTokenId(), api.getApiId()))
                .apiToken(apiToken)
                .api(api)
                .apiUseReason(apiUseReason)
                .name(name)
                .endpoint(endpoint)
                .build();
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getApiId() {
        return api.getApiId();
    }

    public String getMethod() {
        return api.getMethod();
    }

    public Long getApiTokenId() {
        return apiToken.getApiTokenId();
    }

    public Long getApiUseReasonId() {
        return apiUseReason.getApiUseReasonId();
    }

    public ApiUseState getApiUseState() {
        return apiUseReason.getApiUseState();
    }

    public boolean equalsApi(Api api) {
        return this.api.equals(api);
    }
}
