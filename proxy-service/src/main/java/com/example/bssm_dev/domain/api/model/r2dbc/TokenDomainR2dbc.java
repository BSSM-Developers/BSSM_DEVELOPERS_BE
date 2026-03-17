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
    private String origin;

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
