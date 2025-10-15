package com.example.bssm_dev.domain.api.model;

import com.example.bssm_dev.domain.api.model.key.ApiUsageId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class ApiUsage {
    @EmbeddedId
    private ApiUsageId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "api_use_reason_id", nullable = false)
    private ApiUseReason apiUseReason;

    @Column(length = 30, nullable = false)
    private String name;

    @Column(nullable = false)
    private String endpoint;

    public static ApiUsage of(ApiToken apiToken, Api api, ApiUseReason apiUseReason, String name, String endpoint) {
        return ApiUsage.builder()
                .id(new ApiUsageId(apiToken, api))
                .apiUseReason(apiUseReason)
                .name(name)
                .endpoint(endpoint)
                .build();
    }
}
