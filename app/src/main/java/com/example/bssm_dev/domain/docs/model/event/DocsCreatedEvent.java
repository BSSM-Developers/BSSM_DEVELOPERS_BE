package com.example.bssm_dev.domain.docs.model.event;

import com.example.bssm_dev.domain.docs.model.Docs;
import com.example.bssm_dev.domain.docs.model.DocsPage;
import com.example.bssm_dev.domain.docs.model.SideBar;
import com.example.bssm_dev.domain.docs.model.SideBarBlock;
import com.example.bssm_dev.domain.docs.model.type.DocumentType;

import java.util.List;

public record DocsCreatedEvent (
        // docs
        String docsId,
        String title,
        boolean autoApproval,
        String description,
        String repositoryUrl,
        DocumentType type,
        String domain,
        Long writerId,
        // docs sidebar
        SideBar sidebar,
        // docs pages
        List<DocsPage> docsPages
) {

    public static DocsCreatedEvent from(
            Docs docs,
            SideBar sidebar,
            List<DocsPage> docsPages
    ) {
        return new DocsCreatedEvent(
                docs.getId(),
                docs.getTitle(),
                docs.getAutoApproval(),
                docs.getDescription(),
                docs.getRepositoryUrl(),
                docs.getType(),
                docs.getDomain(),
                docs.getWriterId(),
                sidebar,
                docsPages
        );
    }

}
