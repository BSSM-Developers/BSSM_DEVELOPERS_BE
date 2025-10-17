package com.example.bssm_dev.domain.api.model.key;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class ApiUsageId implements Serializable {
    private Long apiTokenId;
    private Long apiId;

    public static ApiUsageId create(Long apiId, Long apiTokenId) {
        return new ApiUsageId(apiId, apiTokenId);
    }
}
