package com.example.bssm_dev.domain.docs.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document(collection = "api_documents")
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class ApiDocument {
    @Id
    private String id;

    private Long apiId;

    private RequestInfo request;
    private ResponseInfo response;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestInfo {
        private String applicationType;
        private List<String> header;
        private List<String> pathParams;
        private List<String> queryParams;
        private Map<String, Object> body;
        private List<String> cookie;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseInfo {
        private String applicationType;
        private List<String> header;
        private Integer statusCode;
        private Map<String, Object> body;
        private List<String> cookie;
    }

    public static ApiDocument of(
            Long apiId,
            RequestInfo request,
            ResponseInfo response
    ) {
        return ApiDocument.builder()
                .apiId(apiId)
                .request(request)
                .response(response)
                .build();
    }
}
