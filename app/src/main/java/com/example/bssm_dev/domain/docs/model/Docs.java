package com.example.bssm_dev.domain.docs.model;
import com.example.bssm_dev.domain.docs.model.type.DocumentType;
import com.example.bssm_dev.domain.user.model.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private DocumentType type;
    private String domain;
    private Long writerId;
    @Builder.Default
    private Long tokenCount = 0L;

    public boolean isMyDocs(User user) {
        return this.writerId.equals(user.getUserId());
    }

    public void toggleAutoApproval() {
        this.autoApproval = !this.autoApproval;
    }

    public void updateDocs(String title, String description, String domain, String repositoryUrl) {
        this.title = title;
        this.description = description;
        this.domain = domain;
        this.repositoryUrl = repositoryUrl;
    }
}