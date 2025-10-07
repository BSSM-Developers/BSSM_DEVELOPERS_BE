package com.example.bssm_dev.domain.auth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(timeToLive = 60)
@Getter
@AllArgsConstructor
public class GoogleCodeVerifier {
    @Id
    private String state;
    private String codeVerifier;

    public static GoogleCodeVerifier of(String state, String codeVerifier) {
        return new GoogleCodeVerifier(state, codeVerifier);
    }
}
