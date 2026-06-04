package com.example.bssm_dev.domain.auth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(timeToLive = 604800)
@Getter
@AllArgsConstructor
public class RefreshToken {
    @Id
    private String token;
    @Indexed
    private Long userId;
    private String email;
    private String userRole;
}
