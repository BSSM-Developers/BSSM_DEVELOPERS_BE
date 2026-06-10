package com.example.bssm_dev.domain.api.model;

import com.example.bssm_dev.domain.api.dto.response.ApiUsageSummaryResponse;
import com.example.bssm_dev.domain.api.exception.ApiTokenBlockedException;
import com.example.bssm_dev.domain.api.exception.InvalidSecretKeyException;
import com.example.bssm_dev.domain.api.exception.UnauthorizedDomainException;
import com.example.bssm_dev.domain.api.model.type.ApiTokenState;
import com.example.bssm_dev.domain.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "token_type", discriminatorType = DiscriminatorType.STRING)
public class ApiToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long apiTokenId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    protected User user;

    @Column(nullable = false)
    protected String apiTokenName;

    @Column(nullable = false, name = "api_token_uuid")
    protected String apiTokenUUID;

    @Column(nullable = false)
    protected String secretKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected ApiTokenState state = ApiTokenState.NORMAL;

    @OneToMany(mappedBy = "apiToken", cascade = CascadeType.ALL, orphanRemoval = true)
    protected List<TokenDomain> tokenDomains = new ArrayList<>();

    @OneToMany(mappedBy = "apiToken")
    @BatchSize(size = 30)
    protected List<ApiUsage> apiUsageList = new ArrayList<>();

    public static ApiToken of(User user, String secretKey, String apiTokenName, String apiTokenUUID) {
        ApiToken token = new ApiToken();
        token.user = user;
        token.secretKey = secretKey;
        token.apiTokenName = apiTokenName;
        token.apiTokenUUID = apiTokenUUID;
        token.state = ApiTokenState.NORMAL;
        token.tokenDomains = new ArrayList<>();
        token.apiUsageList = new ArrayList<>();
        return token;
    }

    public void addTokenOrigin(String origin) {
        TokenDomain tokenDomain = TokenDomain.of(this, origin);
        this.tokenDomains.add(tokenDomain);
    }

    public void updateTokenOrigins(List<String> origins) {
        this.tokenDomains.clear();
        origins.forEach(this::addTokenOrigin);
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
        if (requestOrigin == null || requestOrigin.isEmpty()) {
            throw UnauthorizedDomainException.raise();
        }
        boolean isAllowed = tokenDomains.stream()
                .anyMatch(tokenDomain -> tokenDomain.matchesOrigin(requestOrigin));
        if (!isAllowed) {
            throw UnauthorizedDomainException.raise();
        }
    }

    public boolean checkApiUsage(Api api) {
        return this.apiUsageList.stream()
                .anyMatch(apiUsage -> apiUsage.equalsApiGroup(api.getApiGroup()));
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

    public void block() {
        this.state = ApiTokenState.BLOCKED;
    }
}
