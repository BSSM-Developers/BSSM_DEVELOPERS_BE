package com.example.bssm_dev.domain.api.model;

import com.example.bssm_dev.domain.api.dto.response.ApiUsageSummaryResponse;
import com.example.bssm_dev.domain.api.exception.ApiTokenBlockedException;
import com.example.bssm_dev.domain.api.exception.InvalidSecretKeyException;
import com.example.bssm_dev.domain.api.exception.UnauthorizedDomainException;
import com.example.bssm_dev.domain.api.model.type.ApiTokenState;
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
import org.springframework.security.crypto.password.PasswordEncoder;

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
    @Builder.Default
    private ApiTokenState state = ApiTokenState.NORMAL;

    @OneToMany(mappedBy = "apiToken", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<TokenDomain> tokenDomains = new ArrayList<>();

    @OneToMany(mappedBy = "apiToken")
    @BatchSize(size = 30)
    @Builder.Default
    List<ApiUsage> apiUsageList = new ArrayList<>();

    public static ApiToken of(User user, String secretKey, String apiTokenName, String apiTokenUUID) {
        return ApiToken.builder()
                .user(user)
                .secretKey(secretKey)
                .apiTokenName(apiTokenName)
                .apiTokenUUID(apiTokenUUID)
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

    public void changeSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void changeApiTokenName(String apiTokenName) {
        this.apiTokenName = apiTokenName;
    }

    public void validateSecretKey(String plainSecretKey, PasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(plainSecretKey, this.secretKey)) {
            throw InvalidSecretKeyException.raise();
        }
    }

    public void validateServerAccess(String plainSecretKey, PasswordEncoder passwordEncoder) {
        if (plainSecretKey == null || plainSecretKey.isEmpty()) {
            throw InvalidSecretKeyException.raise();
        }
        validateSecretKey(plainSecretKey, passwordEncoder);
    }

    public void validateBrowserAccess(String requestOrigin) {
        if (tokenDomains == null || tokenDomains.isEmpty()) {
            throw UnauthorizedDomainException.raise();
        }
        validateDomain(requestOrigin);
    }

    private void validateDomain(String requestOrigin) {
        if (requestOrigin == null || requestOrigin.isEmpty()) {
            throw UnauthorizedDomainException.raise();
        }

        String domain = extractDomain(requestOrigin);
        boolean isAllowed = tokenDomains.stream()
                .anyMatch(tokenDomain -> tokenDomain.matchesDomain(domain));

        if (!isAllowed) {
            throw UnauthorizedDomainException.raise();
        }
    }

    private String extractDomain(String origin) {
        String domain = origin.replaceAll("^https?://", "");
        domain = domain.replaceAll(":\\d+$", "");
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

    public void validateNotBlocked() {
        if (this.state == ApiTokenState.BLOCKED) {
            throw ApiTokenBlockedException.raise();
        }
    }

    public void unblock() {
        this.state = ApiTokenState.NORMAL;
    }
}
