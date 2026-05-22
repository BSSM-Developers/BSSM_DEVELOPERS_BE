package com.example.bssm_dev.domain.api.executor;

import com.example.bssm_dev.common.util.DomainValidator;
import com.example.bssm_dev.domain.api.model.r2dbc.ApiUsageR2dbc;
import com.example.bssm_dev.domain.api.model.type.MethodType;
import com.example.bssm_dev.domain.api.model.vo.RequestInfo;
import com.example.bssm_dev.domain.api.requester.impl.RestRequester;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class ApiRequestExecutor {

    public Mono<ResponseEntity<byte[]>> request(ApiUsageR2dbc apiUsage, RequestInfo requestInfo) {
        DomainValidator.validate(apiUsage.getDomain());
        return dispatch(
                RestRequester.of(apiUsage.getDomain()),
                requestInfo.endpoint(),
                MethodType.valueOf(apiUsage.getMethod()),
                requestInfo.body(),
                requestInfo.headers()
        );
    }

    public Mono<ResponseEntity<byte[]>> request(String endpoint, String method, String domain, RequestInfo requestInfo) {
        DomainValidator.validate(domain);
        return dispatch(
                RestRequester.of(domain),
                endpoint,
                MethodType.valueOf(method),
                requestInfo.body(),
                requestInfo.headers()
        );
    }

    private Mono<ResponseEntity<byte[]>> dispatch(
            RestRequester requester,
            String endpoint,
            MethodType methodType,
            Object body,
            Map<String, String> headers
    ) {
        return switch (methodType) {
            case GET -> requester.get(endpoint, headers);
            case POST -> requester.post(endpoint, body, headers);
            case PUT -> requester.put(endpoint, body, headers);
            case PATCH -> requester.patch(endpoint, body, headers);
            case DELETE -> requester.delete(endpoint, headers);
        };
    }
}
