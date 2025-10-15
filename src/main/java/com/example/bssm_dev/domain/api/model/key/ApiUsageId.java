package com.example.bssm_dev.domain.api.model.key;

import com.example.bssm_dev.domain.api.model.Api;
import com.example.bssm_dev.domain.api.model.ApiToken;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class ApiUsageId implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "api_token_id")
    private ApiToken apiToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "api_id")
    private Api api;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApiUsageId)) return false;
        ApiUsageId that = (ApiUsageId) o;
        return Objects.equals(apiToken, that.apiToken)
                && Objects.equals(api, that.api);
    }

    @Override
    public int hashCode() {
        return Objects.hash(apiToken, api);
    }
}
