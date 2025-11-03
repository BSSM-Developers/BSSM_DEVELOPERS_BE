package com.example.bssm_dev.domain.docs.mapper;
import com.example.bssm_dev.domain.docs.dto.request.*;
import com.example.bssm_dev.domain.docs.model.Docs;
import com.example.bssm_dev.domain.docs.model.type.DocumentType;
import com.example.bssm_dev.domain.user.model.User;
import org.springframework.stereotype.Component;

@Component
public class DocsMapper {

    public Docs toOriginalDocs(CreateOriginalDocsRequest request, User creator) {
        Docs docs = Docs.builder()
                .title(request.title())
                .repositoryUrl(request.repositoryUrl())
                .description(request.description())
                .domain(request.domain())
                .type(DocumentType.ORIGINAL)
                .auto_approval(request.autoApproval())
                .writerId(creator.getUserId())
                .build();
        return docs;
    }
}
