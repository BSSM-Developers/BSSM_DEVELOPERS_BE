package com.example.bssm_dev.domain.docs.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SideBarBlockResponse(
        String id,
        String mappedId,
        String label,
        String module,
        List<SideBarBlockResponse> childrenItems
) {
}
