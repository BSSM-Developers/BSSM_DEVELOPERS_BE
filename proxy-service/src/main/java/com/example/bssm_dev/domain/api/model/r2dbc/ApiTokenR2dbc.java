package com.example.bssm_dev.domain.api.model.r2dbc;

import com.example.bssm_dev.domain.api.exception.ApiTokenBlockedException;
import com.example.bssm_dev.domain.api.exception.InvalidSecretKeyException;
import com.example.bssm_dev.domain.api.exception.UnauthorizedDomainException;
import com.example.bssm_dev.domain.api.model.type.ApiTokenState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Slf4j
@Table("api_token")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiTokenR2dbc {
    @Id
    @Column("api_token_id")
    private Long apiTokenId;

    @Column("user_id")
    private Long userId;

    @Column("api_token_name")
    private String apiTokenName;

    @Column("api_token_uuid")
    private String apiTokenUUID;

    @Column("secret_key")
    private String secretKey;

    @Column("state")
    private ApiTokenState state;

    public void validateNotBlocked() {
        if (ApiTokenState.BLOCKED.equals(this.state)) {
            throw ApiTokenBlockedException.raise();
        }
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

    public void validateBrowserAccess(String requestOrigin, List<TokenDomainR2dbc> tokenDomains) {
        if (tokenDomains == null || tokenDomains.isEmpty()) {
            log.warn("[DomainValidation] apiTokenId={} requestOrigin={} allowedDomains=[] - 등록된 도메인 없음",
                    apiTokenId, requestOrigin);
            throw UnauthorizedDomainException.raise();
        }
        if (requestOrigin == null || requestOrigin.isEmpty()) {
            log.warn("[DomainValidation] apiTokenId={} requestOrigin=null allowedDomains={} - Origin 헤더 없음",
                    apiTokenId, tokenDomains.stream().map(TokenDomainR2dbc::getOrigin).toList());
            throw UnauthorizedDomainException.raise();
        }
        boolean isAllowed = tokenDomains.stream()
                .anyMatch(tokenDomain -> tokenDomain.matchesOrigin(requestOrigin));
        if (!isAllowed) {
            log.warn("[DomainValidation] apiTokenId={} requestOrigin={} allowedDomains={} - 도메인 불일치",
                    apiTokenId, requestOrigin, tokenDomains.stream().map(TokenDomainR2dbc::getOrigin).toList());
            throw UnauthorizedDomainException.raise();
        }
    }

    /**
     * 차단 정책에 따라 상태를 다음 단계로 전환
     * NORMAL → WARNING → BLOCKED
     * 
     * @return 상태가 변경되었으면 새로운 상태, 변경되지 않았으면 null
     */
    public ApiTokenState transitionToNextState() {
        ApiTokenState currentState = this.state;
        ApiTokenState newState = null;
        
        if (currentState == null || currentState == ApiTokenState.NORMAL) {
            this.state = ApiTokenState.WARNING;
            newState = ApiTokenState.WARNING;
        } else if (currentState == ApiTokenState.WARNING) {
            this.state = ApiTokenState.BLOCKED;
            newState = ApiTokenState.BLOCKED;
        }
        
        return newState;
    }
}
