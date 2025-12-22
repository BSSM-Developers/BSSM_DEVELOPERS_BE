package com.example.bssm_dev.domain.docs.dto.response;

import java.util.List;

public record DocsSideBarResponse(
        String id,
        String docsId,
        List<SideBarBlockResponse> blocks
) {
}
