package com.example.bssm_dev.domain.docs.dto.request;
import java.util.List;

public record CreateDocsSideBarRequest (
        List<SideBarBlockRequest> blocks
) {
}
