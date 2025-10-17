package com.example.bssm_dev.domain.api.model;

import com.example.bssm_dev.domain.api.exception.InvalidSecretKeyException;
import com.example.bssm_dev.domain.user.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String secretKey;

    public static ApiToken of(User user, String secretKey) {
        return ApiToken.builder()
                .user(user)
                .secretKey(secretKey)
                .build();
    }

    public void changeSecretKey(String secretKey) {
    }

    public void validateSecretKey(String secretKey) {
        boolean equalsSecretKey = this.secretKey.equals(secretKey);
        if (!equalsSecretKey)
            throw InvalidSecretKeyException.raise();
    }
}
