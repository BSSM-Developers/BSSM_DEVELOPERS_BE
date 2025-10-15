package com.example.bssm_dev.domain.docs.mapper;

import com.example.bssm_dev.domain.docs.dto.ApiDocumentData;
import com.example.bssm_dev.domain.docs.model.ApiDocument;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApiDocumentMapper {
    public List<ApiDocument> toApiDocumentList(List<ApiDocumentData> apiDocuments) {
        return apiDocuments.stream()
                .map(this::toApiDocument)
                .toList();
    }


    public ApiDocument toApiDocument(ApiDocumentData data) {
        ApiDocument.RequestInfo requestInfo = null;
        if (data.request() != null) {
            requestInfo = ApiDocument.RequestInfo.builder()
                    .applicationType(data.request().applicationType())
                    .header(data.request().header())
                    .pathParams(data.request().pathParams())
                    .queryParams(data.request().queryParams())
                    .body(data.request().body())
                    .cookie(data.request().cookie())
                    .build();
        }

        ApiDocument.ResponseInfo responseInfo = null;
        if (data.response() != null) {
            responseInfo = ApiDocument.ResponseInfo.builder()
                    .applicationType(data.response().applicationType())
                    .header(data.response().header())
                    .statusCode(data.response().statusCode())
                    .body(data.response().body())
                    .cookie(data.response().cookie())
                    .build();
        }

        return ApiDocument.of(
                data.apiId(),
                requestInfo,
                responseInfo
        );
    }
}
