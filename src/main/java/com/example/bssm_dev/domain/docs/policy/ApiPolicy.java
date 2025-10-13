package com.example.bssm_dev.domain.docs.policy;

import com.example.bssm_dev.domain.docs.dto.request.CreateDocsPageRequest;

public class ApiPolicy {
    private static final String API = "api";

    public static boolean canBeApiPage(CreateDocsPageRequest pageRequest) {
        return API.equalsIgnoreCase(pageRequest.type()) && pageRequest.method() != null && pageRequest.endpoint() != null;
    }
}
