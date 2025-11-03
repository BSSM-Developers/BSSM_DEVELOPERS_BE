package com.example.bssm_dev.domain.docs.dto;

public record ApiDocumentData(
        Long apiId,
        ApiRequestData request,
        ApiResponseData response
) {
}
