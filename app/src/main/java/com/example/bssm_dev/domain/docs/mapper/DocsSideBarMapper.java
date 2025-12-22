package com.example.bssm_dev.domain.docs.mapper;

import com.example.bssm_dev.domain.docs.dto.request.CreateDocsSideBarRequest;
import com.example.bssm_dev.domain.docs.dto.request.SideBarBlockRequest;
import com.example.bssm_dev.domain.docs.dto.response.DocsSideBarResponse;
import com.example.bssm_dev.domain.docs.dto.response.SideBarBlockResponse;
import com.example.bssm_dev.domain.docs.model.Docs;
import com.example.bssm_dev.domain.docs.model.SideBar;
import com.example.bssm_dev.domain.docs.model.SideBarBlock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DocsSideBarMapper {
    private final DocsSideBarBlockMapper docsSideBarBlockMapper;

    public SideBar toDocsSideBar(CreateDocsSideBarRequest request, Docs newDocs) {
        SideBar sideBar = SideBar.builder()
                .docsId(newDocs.getId())
                .sideBarBlocks(
                        docsSideBarBlockMapper.toDocsSideBarBlocks(request.blocks())
                )
                .build();
        return sideBar;
    }

    public DocsSideBarResponse toResponse(SideBar sideBar) {
        List<SideBarBlockResponse> blocks = sideBar.getSideBarBlocks().stream()
                .map(this::toBlockResponse)
                .collect(Collectors.toList());

        return new DocsSideBarResponse(
                sideBar.getId(),
                sideBar.getDocsId(),
                blocks
        );
    }

    private SideBarBlockResponse toBlockResponse(SideBarBlock block) {
        List<SideBarBlockResponse> childrenItems = null;

        List<SideBarBlock> rawChildren = block.getChildrenItems();
        boolean blockChildrenIsEmpty = rawChildren == null || rawChildren.isEmpty();

        if (!blockChildrenIsEmpty) {
            childrenItems = rawChildren.stream()
                    .map(this::toBlockResponse)
                    .collect(Collectors.toList());
        }

        return new SideBarBlockResponse(
                block.getId(),
                block.getMappedId(),
                block.getLabel(),
                block.getModuleName(),
                childrenItems
        );
    }
}
