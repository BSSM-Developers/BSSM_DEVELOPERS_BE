package com.example.bssm_dev.domain.api.model;

import com.example.bssm_dev.domain.api.dto.response.ApiUsageSummaryResponse;
import com.example.bssm_dev.domain.api.exception.InvalidSecretKeyException;
import com.example.bssm_dev.domain.user.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class ApiToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long apiTokenId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String apiTokenName;

    @Column(nullable = false, name = "api_token_uuid")
    private String apiTokenUUID;

    @Column(nullable = false)
    private String secretKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TokenType tokenType;

    @OneToMany(mappedBy = "apiToken", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<TokenDomain> tokenDomains = new ArrayList<>();

    @OneToMany(mappedBy = "apiToken")
    @BatchSize(size = 30)
    @Builder.Default
    List<ApiUsage> apiUsageList = new ArrayList<>();

    public static ApiToken of(User user, String secretKey, String apiTokenName, String apiTokenUUID, TokenType tokenType) {
        return ApiToken.builder()
                .user(user)
                .secretKey(secretKey)
                .apiTokenName(apiTokenName)
                .apiTokenUUID(apiTokenUUID)
                .tokenType(tokenType)
                .build();
    }

    public void addTokenDomain(String domain) {
        TokenDomain tokenDomain = TokenDomain.of(this, domain);
        this.tokenDomains.add(tokenDomain);
    }

    public void updateTokenDomains(List<String> domains) {
        this.tokenDomains.clear();
        domains.forEach(this::addTokenDomain);
    }

    public boolean isAllowedDomain(String requestDomain) {
        if (tokenType == TokenType.SERVER) {
            return true; // SERVER 타입은 도메인 검증 불필요
        }
        return tokenDomains.stream()
                .anyMatch(tokenDomain -> tokenDomain.matchesDomain(requestDomain));
    }

    public void changeSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void changeApiTokenName(String apiTokenName) {
        this.apiTokenName = apiTokenName;
    }

    public void validateSecretKey(String secretKey) {
        boolean equalsSecretKey = this.secretKey.equals(secretKey);
        if (!equalsSecretKey)
            throw InvalidSecretKeyException.raise();
    }

    public void validateAccess(String secretKey, String requestOrigin) {
        if (tokenType == TokenType.SERVER) {
            if (secretKey == null || secretKey.isEmpty()) {
                throw InvalidSecretKeyException.raise();
            }
            validateSecretKey(secretKey);
        } else if (tokenType == TokenType.BROWSER) {
            validateDomain(requestOrigin);
        }
    }

    private void validateDomain(String requestOrigin) {
        if (requestOrigin == null || requestOrigin.isEmpty()) {
            throw com.example.bssm_dev.domain.api.exception.UnauthorizedDomainException.raise();
        }

        String domain = extractDomain(requestOrigin);
        boolean isAllowed = tokenDomains.stream()
                .anyMatch(tokenDomain -> tokenDomain.matchesDomain(domain));

        if (!isAllowed) {
            throw com.example.bssm_dev.domain.api.exception.UnauthorizedDomainException.raise();
        }
    }

    private String extractDomain(String origin) {
        // http:// 또는 https:// 제거
        String domain = origin.replaceAll("^https?://", "");
        // 포트 번호 제거
        domain = domain.replaceAll(":\\d+$", "");
        // 경로 제거
        int slashIndex = domain.indexOf('/');
        if (slashIndex != -1) {
            domain = domain.substring(0, slashIndex);
        }
        return domain;
    }

    public boolean checkApiUsage(Api api) {
        return this.apiUsageList.stream()
                .anyMatch(apiUsage -> apiUsage.equalsApi(api));
    }

    public boolean isOwner(User user) {
        return this.user.equals(user);
    }
}
