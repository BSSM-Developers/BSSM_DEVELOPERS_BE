package com.example.bssm_dev.domain.api.model.r2dbc;

import com.example.bssm_dev.domain.api.exception.InvalidSecretKeyException;
import com.example.bssm_dev.domain.api.exception.UnauthorizedDomainException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Table("api_token")
@Data
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
            throw UnauthorizedDomainException.raise();
        }
        validateDomain(requestOrigin, tokenDomains);
    }

    private void validateDomain(String requestOrigin, List<TokenDomainR2dbc> tokenDomains) {
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
}
