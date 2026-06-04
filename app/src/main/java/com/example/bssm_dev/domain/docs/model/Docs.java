package com.example.bssm_dev.domain.docs.model;
import com.example.bssm_dev.domain.docs.model.type.DocumentType;
import com.example.bssm_dev.domain.user.model.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "docs")
@Builder
@Getter
public class Docs {
    @Id
    private String id;
    private String title;
    private Boolean autoApproval;
    private String description;
    private String repositoryUrl;
    private String branch;
    private DocumentType type;
    private String domain;
    private Long writerId;
    @Builder.Default
    private Long tokenCount = 0L;
    private LocalDateTime deletedAt;

    public boolean isMyDocs(User user) {
        return this.writerId.equals(user.getUserId());
    }

    public void toggleAutoApproval() {
        this.autoApproval = !this.autoApproval;
    }

    public void updateDocs(String title, String description, String domain, String repositoryUrl, String branch) {
        this.title = title;
        this.description = description;
        this.domain = domain;
        this.repositoryUrl = repositoryUrl;
        this.branch = branch;
    }

    public void incrementTokenCount() {
        this.tokenCount++;
    }

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }

    public boolean isDeleted() {
        return this.deletedAt != null;
    }
}