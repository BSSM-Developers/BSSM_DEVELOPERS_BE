package com.example.bssm_dev.domain.docs.mapper;

import com.example.bssm_dev.domain.api.model.Api;
import com.example.bssm_dev.domain.docs.dto.request.*;
import com.example.bssm_dev.domain.docs.dto.response.*;
import com.example.bssm_dev.domain.docs.dto.response.ApiDetailResponse;
import com.example.bssm_dev.domain.docs.model.Docs;
import com.example.bssm_dev.domain.docs.model.type.DocumentType;
import com.example.bssm_dev.domain.docs.model.type.PageType;
import com.example.bssm_dev.domain.docs.policy.ApiPolicy;
import com.example.bssm_dev.domain.user.model.User;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DocsMapper {

    public Docs toOriginalDocs(CreateOriginalDocsRequest request, User creator) {
        Docs docs = Docs.builder()
                .title(request.title())
                .repositoryUrl(request.repositoryUrl())
                .description(request.description())
                .domain(request.domain())
                .type(DocumentType.ORIGINAL)
                .auto_approval(request.autoApproval())
                .writerId(creator.getUserId())
                .build();
        return docs;
    }
}
