package com.example.bssm_dev.domain.user.model;

import com.example.bssm_dev.domain.user.model.type.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String name;
    private String email;
    private String profile;
    private UserRole role;

    public User(String email, String profile, String name, UserRole role) {
        this.email = email;
        this.name = name;
        this.profile = profile;
        this.role = role;
    }


    public static User of(String email, String name, String profile, UserRole role) {
        return new User(email, name, profile, role);
    }
}
