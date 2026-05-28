package com.example.bssm_dev.domain.docs.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record AddCustomDocsPageRequest(
        @NotNull @Valid CreateDocsPageRequest page,
        @NotNull @Valid SideBarBlockRequest sidebarBlock
) {}
