package com.example.bssm_dev.domain.docs.dto;

import com.example.bssm_dev.domain.docs.dto.request.ApiRequestData;
import com.example.bssm_dev.domain.docs.dto.request.ApiResponseData;

public record ApiDocumentData(
        Long apiId,
        ApiRequestData request,
        ApiResponseData response
) {
}
