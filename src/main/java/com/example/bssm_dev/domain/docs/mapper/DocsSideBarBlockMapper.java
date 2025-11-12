package com.example.bssm_dev.domain.docs.mapper;

import com.example.bssm_dev.common.util.RandomNumberGenerateUtil;
import com.example.bssm_dev.domain.docs.dto.request.SideBarBlockRequest;
import com.example.bssm_dev.domain.docs.model.SideBarBlock;
import com.example.bssm_dev.domain.docs.model.type.SideBarModule;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DocsSideBarBlockMapper {

    public List<SideBarBlock> toDocsSideBarBlocks(List<SideBarBlockRequest> blocks) {
        return blocks.stream()
                .map(this::toSideBarBlock)
                .toList();
    }

    private SideBarBlock toSideBarBlock(SideBarBlockRequest request) {
        SideBarBlock sideBarBlock = SideBarBlock.builder()
                .id(RandomNumberGenerateUtil.generate(5))
                .mappedId(request.id())
                .label(request.label())
                .module(SideBarModule.fromString(request.module()))
                .childrenItems(
                        request.childrenItems() == null ? null :
                        request.childrenItems().stream()
                                .map(this::toSideBarBlock)
                                .toList()
                )
                .method(
                        request.method() == null && SideBarModule.isApi(request.module())
                                ? null
                                : request.method()
                )
                .build();
        return sideBarBlock;
    }
}
