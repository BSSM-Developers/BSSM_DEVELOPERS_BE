package com.example.bssm_dev.domain.api.event.listner;

import com.example.bssm_dev.domain.api.model.Api;
import com.example.bssm_dev.domain.api.repository.ApiRepository;
import com.example.bssm_dev.domain.docs.model.DocsPage;
import com.example.bssm_dev.domain.docs.model.SideBarBlock;
import com.example.bssm_dev.domain.docs.model.event.DocsCreatedEvent;
import com.example.bssm_dev.domain.docs.model.type.SideBarModule;
import com.example.bssm_dev.domain.user.model.User;
import com.example.bssm_dev.domain.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DocsCreatedEventListener {
    private final ApiRepository apiRepository;
    private final UserQueryService userQueryService;

    @EventListener
    public void createApi(DocsCreatedEvent docsCreatedEvent) {
        log.info("DocsCreatedEvent received for docsId: {}", docsCreatedEvent.docsId());
        
        User creator = userQueryService.findById(docsCreatedEvent.writerId());
        log.info("Creator found: userId={}, name={}", creator.getUserId(), creator.getName());

        List<Api> apis = new ArrayList<>();
        extractApisFromSidebar(docsCreatedEvent.sidebar().getSideBarBlocks(), docsCreatedEvent, creator, apis);
        
        log.info("Total APIs extracted: {}", apis.size());
        if (!apis.isEmpty()) {
            apiRepository.saveAll(apis);
            log.info("APIs saved successfully: {}", apis.size());
        } else {
            log.warn("No APIs were extracted from the docs");
        }
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
            log.debug("Processing block: id={}, mappedId={}, module={}, label={}", 
                block.getId(), block.getMappedId(), block.getModule(), block.getLabel());
            
            if (block.getModule() == SideBarModule.API) {
                log.info("API block found: mappedId={}, label={}, method={}", 
                    block.getMappedId(), block.getLabel(), block.getMethod());
                
                // DocsPage에서 endpoint 직접 가져오기
                String endpoint = findEndpointByMappedId(block.getMappedId(), event.docsPages());
                
                if (endpoint != null) {
                    log.info("Endpoint found for mappedId={}: {}", block.getMappedId(), endpoint);
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
                    log.info("API added: {} {} - {}", block.getMethod(), endpoint, block.getLabel());
                } else {
                    log.warn("Endpoint not found for API block with mappedId: {}", block.getMappedId());
                }
            }

            if (block.getChildrenItems() != null && !block.getChildrenItems().isEmpty()) {
                extractApisFromSidebar(block.getChildrenItems(), event, creator, apis);
            }
        }
    }

    private String findEndpointByMappedId(String mappedId, List<DocsPage> docsPages) {
        if (mappedId == null || docsPages == null) {
            log.warn("mappedId or docsPages is null");
            return null;
        }
        
        log.debug("Searching for mappedId: {} in {} pages", mappedId, docsPages.size());
        for (DocsPage page : docsPages) {
            log.debug("Checking page: id={}, mappedId={}, endpoint={}", 
                page.getId(), page.getMappedId(), page.getEndpoint());
            
            if (mappedId.equals(page.getMappedId())) {
                log.debug("Match found for mappedId: {}, endpoint: {}", mappedId, page.getEndpoint());
                return page.getEndpoint();  // 이제 endpoint 필드에서 직접 가져옴!
            }
        }
        log.warn("No matching page found for mappedId: {}", mappedId);
        return null;
    }

}
