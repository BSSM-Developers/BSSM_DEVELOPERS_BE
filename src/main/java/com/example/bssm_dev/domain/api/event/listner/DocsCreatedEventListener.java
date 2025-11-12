package com.example.bssm_dev.domain.api.event.listner;

import com.example.bssm_dev.domain.api.model.Api;
import com.example.bssm_dev.domain.api.repository.ApiRepository;
import com.example.bssm_dev.domain.docs.model.DocsPage;
import com.example.bssm_dev.domain.docs.model.SideBarBlock;
import com.example.bssm_dev.domain.docs.model.event.DocsCreatedEvent;
import com.example.bssm_dev.domain.docs.model.type.SideBarModule;
import com.example.bssm_dev.domain.user.model.User;
import com.example.bssm_dev.domain.user.repository.UserRepository;
import com.example.bssm_dev.domain.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DocsCreatedEventListener {
    private final ApiRepository apiRepository;
    private final UserQueryService userQueryService;

    @TransactionalEventListener
    public void createApi(DocsCreatedEvent docsCreatedEvent) {
        User creator = userQueryService.findById(docsCreatedEvent.writerId());

        List<Api> apis = new ArrayList<>();
        extractApisFromSidebar(docsCreatedEvent.sidebar().getSideBarBlocks(), docsCreatedEvent, creator, apis);

        apiRepository.saveAll(apis);
    }

    private void extractApisFromSidebar(
            List<SideBarBlock> blocks,
            DocsCreatedEvent event,
            User creator,
            List<Api> apis
    ) {
        if (blocks == null) {
            return;
        }

        for (SideBarBlock block : blocks) {
            if (block.getModule() == SideBarModule.API) {
                String endpoint = findEndpointByMappedId(block.getMappedId(), event.docsPages());
                
                if (endpoint != null) {
                    Api api = Api.of(
                            creator,
                            endpoint,
                            block.getMethod(),
                            block.getLabel(),
                            event.domain(),
                            event.repositoryUrl(),
                            event.autoApproval()
                    );
                    apis.add(api);
                }
            }

            if (block.getChildrenItems() != null && !block.getChildrenItems().isEmpty()) {
                extractApisFromSidebar(block.getChildrenItems(), event, creator, apis);
            }
        }
    }

    private String findEndpointByMappedId(String mappedId, List<DocsPage> docsPages) {
        if (mappedId == null || docsPages == null) {
            return null;
        }

        for (DocsPage page : docsPages) {
            if (mappedId.equals(page.getMappedId())) {
                return extractEndpointFromPage(page);
            }
        }
        return null;
    }

    private String extractEndpointFromPage(DocsPage page) {
        if (page.getDocsBlocks() == null || page.getDocsBlocks().isEmpty()) {
            return null;
        }

        return page.getDocsBlocks().stream()
                .filter(block -> block.getContent() != null)
                .findFirst()
                .map(block -> block.getContent().trim())
                .orElse(null);
    }

}
