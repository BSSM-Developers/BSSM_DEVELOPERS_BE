package com.example.bssm_dev.domain.api.model;

import com.example.bssm_dev.domain.api.model.type.ApiTokenState;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Entity
@NoArgsConstructor
@Getter
@AllArgsConstructor
@DiscriminatorValue("TRY_IT")
public class TryItToken extends ApiToken {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "api_id", nullable = false)
    private Api api;

    public static TryItToken of(Api api, String secretKey) {
        TryItToken token = new TryItToken();
        token.user = api.getCreator();
        token.secretKey = secretKey;
        token.apiTokenName = "Try It - " + api.getName();
        token.apiTokenUUID = api.getApiId();  // API ID를 UUID로 사용
        token.state = ApiTokenState.NORMAL;
        token.tokenDomains = new ArrayList<>();
        token.apiUsageList = new ArrayList<>();
        token.api = api;
        return token;
    }
}
