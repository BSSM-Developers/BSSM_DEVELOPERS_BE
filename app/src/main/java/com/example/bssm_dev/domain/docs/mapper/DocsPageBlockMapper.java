package com.example.bssm_dev.domain.docs.mapper;

import com.example.bssm_dev.common.util.RandomNumberGenerateUtil;
import com.example.bssm_dev.domain.docs.dto.request.DocsPageBlockRequest;
import com.example.bssm_dev.domain.docs.model.DocsPageBlock;
import com.example.bssm_dev.domain.docs.model.type.DocsModule;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DocsPageBlockMapper {

    public List<DocsPageBlock> toDocsPageBlocks(List<DocsPageBlockRequest> blocks) {
        return blocks.stream()
                .map(this::toDocsPageBlock)
                .toList();
    }

    private DocsPageBlock toDocsPageBlock(DocsPageBlockRequest docsPageBlockRequest) {
        DocsPageBlock docsPageBlock = DocsPageBlock.builder()
                .id(RandomNumberGenerateUtil.generate(5))
                .mappedId(docsPageBlockRequest.id())
                .module(DocsModule.fromString(docsPageBlockRequest.module()))
                .content(docsPageBlockRequest.content())
                .build();
        return docsPageBlock;
    }
}
