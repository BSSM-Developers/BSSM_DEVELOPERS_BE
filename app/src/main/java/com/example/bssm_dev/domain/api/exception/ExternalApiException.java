package com.example.bssm_dev.domain.api.exception;

import com.example.bssm_dev.exception.ErrorCode;
import lombok.Getter;

@Getter
public class ExternalApiException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String errorMsg;
    private final Integer upstreamStatusCode;
    private final String upstreamBody;

    private ExternalApiException(ErrorCode errorCode, String errorMsg, Integer upstreamStatusCode, String upstreamBody) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.upstreamStatusCode = upstreamStatusCode;
        this.upstreamBody = upstreamBody;
    }

    public static ExternalApiException raise(String errorMsg) {
        return new ExternalApiException(ErrorCode.EXTERNAL_API_ERROR, errorMsg, null, null);
    }

    public static ExternalApiException raise(int upstreamStatusCode, String upstreamBody, String errorMsg) {
        return new ExternalApiException(null, errorMsg, upstreamStatusCode, upstreamBody);
    }
}
