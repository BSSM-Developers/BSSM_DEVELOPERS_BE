package com.example.bssm_dev.domain.docs.mapper;

import com.example.bssm_dev.domain.docs.dto.request.SideBarBlockRequest;
import com.example.bssm_dev.domain.docs.model.SideBarBlock;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DocsSideBarBlockMapper {

    public List<SideBarBlock> toDocsSideBarBlocks(List<SideBarBlockRequest> blocks) {
        return blocks.stream()
                .map(this::toSideBarBlock)
                .toList();
    }

    private SideBarBlock toSideBarBlock(SideBarBlockRequest request) {
        SideBarBlock sideBarBlock = SideBarBlock.builder()
                .id()
                .mappedId(request.id())
                .label(request.label())
                .module(request.module())
                .build();
        return sideBarBlock;
    }
}
