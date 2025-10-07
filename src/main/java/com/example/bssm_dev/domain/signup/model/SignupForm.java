package com.example.bssm_dev.domain.signup.model;

import com.example.bssm_dev.domain.signup.model.type.SignUpFormState;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class SignupForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long signupRequestId;

    private String name;
    private String email;
    private String profile;
    private String purpose;
    @Enumerated(EnumType.STRING)
    private SignUpFormState state;

    public SignupForm(String name, String email, String profile) {
        this.name = name;
        this.email = email;
        this.profile = profile;
    }

    public static SignupForm of(String name, String email, String profile) {
        return new SignupForm(name, email, profile);
    }
}