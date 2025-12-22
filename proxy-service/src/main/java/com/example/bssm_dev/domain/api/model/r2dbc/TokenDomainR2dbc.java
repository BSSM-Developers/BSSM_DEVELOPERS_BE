package com.example.bssm_dev.domain.api.model.r2dbc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("token_domain")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDomainR2dbc {
    @Id
    @Column("token_domain_id")
    private Long tokenDomainId;

    @Column("api_token_id")
    private Long apiTokenId;

    @Column("domain")
    private String domain;

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
