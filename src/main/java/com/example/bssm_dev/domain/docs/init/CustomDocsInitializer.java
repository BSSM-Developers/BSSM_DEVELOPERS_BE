package com.example.bssm_dev.domain.docs.init;

import com.example.bssm_dev.common.util.RandomNumberGenerateUtil;
import com.example.bssm_dev.domain.docs.dto.request.CreateDocsSideBarRequest;
import com.example.bssm_dev.domain.docs.model.DocsPage;
import com.example.bssm_dev.domain.docs.model.DocsPageBlock;
import com.example.bssm_dev.domain.docs.model.SideBar;
import com.example.bssm_dev.domain.docs.model.SideBarBlock;
import com.example.bssm_dev.domain.docs.model.type.DocsModule;
import com.example.bssm_dev.domain.docs.model.type.SideBarModule;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

public class CustomDocsInitializer {
    private static final String DOCS_MAPPED_ID = "doc-1";
    private static final String LABEL = "시작하기";
    private static final SideBarModule SIDE_BAR_MODULE = SideBarModule.DEFAULT;

    private static final DocsModule DOCS_MODULE = DocsModule.DOCS_1;
    private static final String DOCS_PAGE_BLOCK_MAPPED_ID = "init-1";
    private static final String DOCS_PAGE_BLOCK_CONTENT = "빈 문서입니다.";

    private static final List<SideBarBlock> SIDE_BAR_BLOCKS = Collections.singletonList(
            initSideBarBlock()
    );

    private static final List<DocsPageBlock> DOCS_PAGE_BLOCKS = Collections.singletonList(
            initDocsPageBlock()
    );

    public static SideBar initSideBar(String docsId) {
        return SideBar.builder()
                .docsId(docsId)
                .sideBarBlocks(SIDE_BAR_BLOCKS)
                .build();
    }

    public static DocsPage initDocsPage(String docsId) {
        return DocsPage.builder()
                .mappedId(DOCS_MAPPED_ID)
                .docsId(docsId)
                .docsBlocks(DOCS_PAGE_BLOCKS)
                .build();
    }

    private static DocsPageBlock initDocsPageBlock() {
        return DocsPageBlock.builder()
                .id(RandomNumberGenerateUtil.generate(5))
                .mappedId(DOCS_PAGE_BLOCK_MAPPED_ID)
                .content(DOCS_PAGE_BLOCK_CONTENT)
                .module(DOCS_MODULE)
                .build();
    }

    private static SideBarBlock initSideBarBlock() {
        return SideBarBlock.builder()
                .id(RandomNumberGenerateUtil.generate(5))
                .mappedId(DOCS_MAPPED_ID)
                .label(LABEL)
                .module(SIDE_BAR_MODULE)
                .build();
    }
}
