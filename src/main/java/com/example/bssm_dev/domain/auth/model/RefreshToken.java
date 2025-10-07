package com.example.bssm_dev.domain.auth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash
@Getter
@AllArgsConstructor
public class RefreshToken {
    @Id
    private String token;
    private String email;
}
