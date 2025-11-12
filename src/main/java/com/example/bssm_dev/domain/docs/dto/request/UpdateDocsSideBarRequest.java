package com.example.bssm_dev.domain.docs.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UpdateDocsSideBarRequest(
        @NotNull(message = "sidebar blocks는 null일 수 없습니다")
        @Valid
        List<SideBarBlockRequest> blocks
) {
}
