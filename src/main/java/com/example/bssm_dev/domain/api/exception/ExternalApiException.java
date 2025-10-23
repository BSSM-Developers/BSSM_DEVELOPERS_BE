package com.example.bssm_dev.domain.api.exception;

import com.example.bssm_dev.global.error.exception.ErrorCode;
import com.example.bssm_dev.global.error.exception.GlobalException;
import lombok.Getter;

@Getter
public class ExternalApiException extends RuntimeException {
    private final ErrorCode errorCode = ErrorCode.EXTERNAL_API_ERROR;
    private final String errorMsg;

    private ExternalApiException(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public static ExternalApiException raise(String errorMsg) {
        return new ExternalApiException(errorMsg);
    }
}
