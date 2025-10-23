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


    public ApiDocument.RequestInfo toRequestInfo(com.example.bssm_dev.domain.docs.dto.request.ApiRequestData requestData) {
        if (requestData == null) {
            return null;
        }
        return ApiDocument.RequestInfo.builder()
                .applicationType(requestData.applicationType())
                .header(requestData.header())
                .pathParams(requestData.pathParams())
                .queryParams(requestData.queryParams())
                .body(requestData.body())
                .cookie(requestData.cookie())
                .build();
    }

    public ApiDocument.ResponseInfo toResponseInfo(com.example.bssm_dev.domain.docs.dto.request.ApiResponseData responseData) {
        if (responseData == null) {
            return null;
        }
        return ApiDocument.ResponseInfo.builder()
                .applicationType(responseData.applicationType())
                .header(responseData.header())
                .statusCode(responseData.statusCode())
                .body(responseData.body())
                .cookie(responseData.cookie())
                .build();
    }
}
