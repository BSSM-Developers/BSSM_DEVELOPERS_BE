package com.example.bssm_dev.domain.api.service.command;

import com.example.bssm_dev.domain.api.model.Api;
import com.example.bssm_dev.domain.api.repository.ApiRepository;
import com.example.bssm_dev.domain.docs.model.DocsPage;
import com.example.bssm_dev.domain.docs.model.SideBar;
import com.example.bssm_dev.domain.docs.model.SideBarBlock;
import com.example.bssm_dev.domain.docs.model.type.SideBarModule;
import com.example.bssm_dev.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiCommandService {
    private final ApiRepository apiRepository;

    @Transactional("transactionManager")
    public void createApisFromDocs(
            String docsId,
            SideBar sideBar,
            List<DocsPage> docsPages,
            User creator,
            String domain,
            String repositoryUrl,
            Boolean autoApproval
    ) {
        log.info("Creating APIs for docsId: {}", docsId);

        List<Api> apis = new ArrayList<>();
        extractApisFromSidebar(sideBar.getSideBarBlocks(), docsPages, creator, domain, repositoryUrl, autoApproval, apis);

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
            List<DocsPage> docsPages,
            User creator,
            String domain,
            String repositoryUrl,
            Boolean autoApproval,
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

                // DocsPage 전체 가져오기
                DocsPage docsPage = findDocsPageByMappedId(block.getMappedId(), docsPages);

                if (docsPage != null && docsPage.getEndpoint() != null) {
                    log.info("DocsPage found for mappedId={}: id={}, endpoint={}",
                            block.getMappedId(), docsPage.getId(), docsPage.getEndpoint());
                    Api api = Api.of(
                            docsPage.getId(),  // DocsPage의 id를 apiId로 사용
                            creator,
                            docsPage.getEndpoint(),
                            block.getMethod(),
                            block.getLabel(),
                            domain,
                            repositoryUrl,
                            autoApproval
                    );
                    apis.add(api);
                    log.info("API added: {} {} - {} (apiId={})",
                            block.getMethod(), docsPage.getEndpoint(), block.getLabel(), docsPage.getId());
                } else {
                    log.warn("DocsPage or endpoint not found for API block with mappedId: {}", block.getMappedId());
                }
            }

            if (block.getChildrenItems() != null && !block.getChildrenItems().isEmpty()) {
                extractApisFromSidebar(block.getChildrenItems(), docsPages, creator, domain, repositoryUrl, autoApproval, apis);
            }
        }
    }

    private DocsPage findDocsPageByMappedId(String mappedId, List<DocsPage> docsPages) {
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
                return page;
            }
        }
        log.warn("No matching page found for mappedId: {}", mappedId);
        return null;
    }
}
