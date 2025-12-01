package com.example.bssm_dev.domain.auth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(timeToLive = 300) // 5분
@Getter
@AllArgsConstructor
public class GoogleAuthCode {
    @Id
    private String token;
    private String code;
    private String codeVerifier;

    public static GoogleAuthCode of(String token, String code, String codeVerifier) {
        return new GoogleAuthCode(token, code, codeVerifier);
    }
}
