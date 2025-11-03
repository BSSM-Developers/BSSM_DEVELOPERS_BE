package com.example.bssm_dev.domain.docs.mapper;

import com.example.bssm_dev.domain.docs.dto.request.CreateDocsSideBarRequest;
import com.example.bssm_dev.domain.docs.dto.request.SideBarBlockRequest;
import com.example.bssm_dev.domain.docs.model.Docs;
import com.example.bssm_dev.domain.docs.model.SideBar;
import com.example.bssm_dev.domain.docs.model.SideBarBlock;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DocsSideBarMapper {

    public SideBar toDocsSideBar(CreateDocsSideBarRequest request, Docs newDocs) {
        SideBar sideBar = SideBar.builder()
                .docsId(newDocs.getId())
                .sideBarBlocks(
                        toDocsSideBarBlocks(request.blocks())
                )
                .build();
        return sideBar;
    }

    public SideBarBlock toSideBarBlock(SideBarBlockRequest request) {
        SideBarBlock sideBarBlock = SideBarBlock.builder()
                .id()
                .mappedId(request.id())
                .label(request.label())
                .module(request.module())
                .build();
        return sideBarBlock;
    }

    private List<SideBarBlock> toDocsSideBarBlocks(List<SideBarBlockRequest> blocks) {
        return blocks.stream()
                .map(this::toSideBarBlock)
                .toList();
    }
}
