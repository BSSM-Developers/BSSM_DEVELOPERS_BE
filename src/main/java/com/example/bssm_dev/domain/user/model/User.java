package com.example.bssm_dev.domain.user.model;

import com.example.bssm_dev.domain.user.model.type.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String name;
    private String email;
    private String profile;
    @Enumerated(EnumType.STRING)
    private UserRole role;

    public User(String email, String name, String profile, UserRole role) {
        this.email = email;
        this.name = name;
        this.profile = profile;
        this.role = role;
    }

    public static User of(String email, String name, String profile, UserRole role) {
        return new User(email, name, profile, role);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId.equals(user.userId);
    }
}

