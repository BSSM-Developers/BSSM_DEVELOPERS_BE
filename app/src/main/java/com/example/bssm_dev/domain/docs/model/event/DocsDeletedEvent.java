package com.example.bssm_dev.domain.docs.model.event;

import java.util.List;

public record DocsDeletedEvent(List<String> apiIds) {

    public static DocsDeletedEvent from(List<String> apiIds) {
        return new DocsDeletedEvent(apiIds);
    }
}
