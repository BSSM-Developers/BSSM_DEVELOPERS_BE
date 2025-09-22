package com.example.bssm_dev.domain.signup.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class SignupRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long signupRequestId;

    private String name;
    private String email;
    private String profile;

    public SignupRequest(String name, String email, String profile) {
        this.name = name;
        this.email = email;
        this.profile = profile;
    }

    public static SignupRequest of(String name, String email, String profile) {
        return new SignupRequest(name, email, profile);
    }
}