package com.example.bssm_dev.domain.api.model;

import com.example.bssm_dev.domain.api.model.type.ApiUseState;
import com.example.bssm_dev.domain.user.model.User;
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
public class ApiUseReason {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long apiUseReasonId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", nullable = false)
    private User writer;

    @Column(nullable = false)
    private String apiUseReason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApiUseState apiUseState;

    public static ApiUseReason of(User writer, String apiUseReason, ApiUseState apiUseState) {
        return ApiUseReason.builder()
                .writer(writer)
                .apiUseReason(apiUseReason)
                .apiUseState(apiUseState)
                .build();
    }
}
