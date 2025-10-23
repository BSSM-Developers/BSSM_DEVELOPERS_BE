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

    @OneToMany(mappedBy = "apiToken")
    @BatchSize(size = 30)
    List<ApiUsage> apiUsageList = new ArrayList<>();

    public static ApiToken of(User user, String secretKey, String apiTokenName, String apiTokenUUID) {
        return ApiToken.builder()
                .user(user)
                .secretKey(secretKey)
                .apiTokenName(apiTokenName)
                .apiTokenUUID(apiTokenUUID)
                .build();
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
}
