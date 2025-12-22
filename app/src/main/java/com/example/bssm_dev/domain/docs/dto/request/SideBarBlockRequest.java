package com.example.bssm_dev.domain.docs.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SideBarBlockRequest(

        @NotNull(message = "id는 필수 값입니다.")
        String id,
        @NotNull(message = "label은 필수 값입니다.")
        String label,
        @NotNull(message = "module은 필수 값입니다.")
        String module,
        List<SideBarBlockRequest> childrenItems,
        String method // label = api면 method 존재
) {
}
