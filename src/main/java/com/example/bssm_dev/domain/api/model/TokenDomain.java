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
        return this.domain.equals(requestDomain);
    }
}
