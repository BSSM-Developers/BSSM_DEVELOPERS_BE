package com.example.bssm_dev.domain.docs.event;

import com.example.bssm_dev.domain.docs.dto.ApiDocumentData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class DocsCreatedEvent {
    private final List<ApiDocumentData> apiDocuments;
}
