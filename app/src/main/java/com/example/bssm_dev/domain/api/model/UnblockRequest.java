package com.example.bssm_dev.domain.api.model;

import com.example.bssm_dev.domain.api.model.type.UnblockRequestState;
import com.example.bssm_dev.domain.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class UnblockRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long unblockRequestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "api_token_id", nullable = false)
    private ApiToken apiToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @Column(nullable = false, length = 500)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private UnblockRequestState state = UnblockRequestState.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id")
    private User reviewer;

    @Column
    private LocalDateTime reviewedAt;

    @Column(length = 500)
    private String rejectReason;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    

    public void approve(User reviewer) {
        this.state = UnblockRequestState.APPROVED;
        this.reviewer = reviewer;
        this.reviewedAt = LocalDateTime.now();
    }

    public void reject(User reviewer, String rejectReason) {
        this.state = UnblockRequestState.REJECTED;
        this.reviewer = reviewer;
        this.rejectReason = rejectReason;
        this.reviewedAt = LocalDateTime.now();
    }

    public boolean isRequester(User user) {
        return this.requester.equals(user);
    }

    public boolean isPending() {
        return this.state == UnblockRequestState.PENDING;
    }
}
