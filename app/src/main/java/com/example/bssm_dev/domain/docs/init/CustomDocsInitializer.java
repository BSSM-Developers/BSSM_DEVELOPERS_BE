package com.example.bssm_dev.domain.docs.init;

import com.example.bssm_dev.common.util.RandomNumberGenerateUtil;
import com.example.bssm_dev.domain.docs.model.DocsPage;
import com.example.bssm_dev.domain.docs.model.DocsPageBlock;
import com.example.bssm_dev.domain.docs.model.SideBar;
import com.example.bssm_dev.domain.docs.model.SideBarBlock;
import com.example.bssm_dev.domain.docs.model.type.DocsModule;
import com.example.bssm_dev.domain.docs.model.type.SideBarModule;

import java.util.Collections;
import java.util.List;

public class CustomDocsInitializer {
    private static final String DOCS_MAPPED_ID = "doc-1";

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
                .mappedId("init-1")
                .content("빈 문서입니다.")
                .module(DocsModule.DOCS_1)
                .build();
    }

    private static SideBarBlock initSideBarBlock() {
        return SideBarBlock.builder()
                .id(RandomNumberGenerateUtil.generate(5))
                .mappedId(DOCS_MAPPED_ID)
                .label("시작하기")
                .module(SideBarModule.DEFAULT)
                .build();
    }
}
