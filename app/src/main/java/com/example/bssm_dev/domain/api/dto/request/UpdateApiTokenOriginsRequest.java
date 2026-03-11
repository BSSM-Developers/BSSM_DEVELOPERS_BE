package com.example.bssm_dev.domain.api.dto.request;

import java.util.List;

public record UpdateApiTokenOriginsRequest(
        List<String> origins
) {
}
