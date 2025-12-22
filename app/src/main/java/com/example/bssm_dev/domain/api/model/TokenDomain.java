package com.example.bssm_dev.domain.api.model;

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
public class TokenDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenDomainId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "api_token_id", nullable = false)
    private ApiToken apiToken;

    @Column(nullable = false)
    private String domain;

    public static TokenDomain of(ApiToken apiToken, String domain) {
        return TokenDomain.builder()
                .apiToken(apiToken)
                .domain(domain)
                .build();
    }

    public boolean matchesDomain(String requestDomain) {
        String normalizedStored = normalizeDomain(this.domain);
        String normalizedRequest = normalizeDomain(requestDomain);
        return normalizedStored.equals(normalizedRequest);
    }

    private String normalizeDomain(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        String domain = value.replaceAll("^https?://", "");
        domain = domain.replaceAll(":\\d+$", "");
        int slashIndex = domain.indexOf('/');
        if (slashIndex != -1) {
            domain = domain.substring(0, slashIndex);
        }
        return domain;
    }
}
