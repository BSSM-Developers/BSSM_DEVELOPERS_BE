package com.example.bssm_dev.domain.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class ApiGroup {

    @Id
    @Column(length = 500)
    private String apiGroupId;

    private ApiGroup(String apiGroupId) {
        this.apiGroupId = apiGroupId;
    }

    public static ApiGroup of(String apiGroupId) {
        return new ApiGroup(apiGroupId);
    }
}
