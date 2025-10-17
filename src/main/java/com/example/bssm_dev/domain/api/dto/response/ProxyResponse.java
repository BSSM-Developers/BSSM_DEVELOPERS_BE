package com.example.bssm_dev.domain.api.dto.response;

public record ProxyResponse (
        Object response
) {
    public static ProxyResponse of(Object response) {
        return new ProxyResponse(response);
    }
}
