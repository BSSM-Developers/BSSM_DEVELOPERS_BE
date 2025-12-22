package com.example.bssm_dev.domain.signup.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(timeToLive = 3600)
@Getter
@AllArgsConstructor
public class SignupToken {
    @Id
    private String token;
    private String email;

    public static SignupToken of(String token, String email) {
        return new SignupToken(token, email);
    }
}
