package com.example.bssm_dev.domain.signup.model;

import com.example.bssm_dev.domain.signup.model.type.SignUpFormState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
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

    public static SignupForm of(String name, String email, String profile) {
        return SignupForm.builder()
                .name(name)
                .email(email)
                .profile(profile)
                .state(SignUpFormState.PENDING)
                .build();
    }
}