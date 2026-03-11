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

    @Column(name = "domain", nullable = false)
    private String origin;

    public static TokenDomain of(ApiToken apiToken, String origin) {
        return TokenDomain.builder()
                .apiToken(apiToken)
                .origin(origin)
                .build();
    }

    public boolean matchesOrigin(String requestOrigin) {
        return normalizeOrigin(this.origin).equals(normalizeOrigin(requestOrigin));
    }

    private String normalizeOrigin(String value) {
        if (value == null || value.isBlank()) {
            return "";
        }
        return value.toLowerCase().replaceAll("/+$", "");
    }
}
