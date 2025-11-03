package com.example.bssm_dev.domain.docs.policy;

import com.example.bssm_dev.domain.docs.model.type.PageType;

public class ApiPolicy {
    private static final PageType API = PageType.API;

    public static boolean canBeApiPage(CreateDocsPageRequest pageRequest) {
        return String.valueOf(API).equalsIgnoreCase(pageRequest.type()) && pageRequest.method() != null && pageRequest.endpoint() != null;
    }
}