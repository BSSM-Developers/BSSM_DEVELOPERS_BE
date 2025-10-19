package com.example.bssm_dev.domain.api.exception;

import com.example.bssm_dev.global.error.exception.ErrorCode;
import com.example.bssm_dev.global.error.exception.GlobalException;

public class ExternalApiException extends GlobalException {
    private ExternalApiException() {
        super(ErrorCode.EXTERNAL_API_ERROR);
    }

    private static class Holder {
        private static final ExternalApiException INSTANCE = new ExternalApiException();
    }

    public static ExternalApiException raise() {
        return Holder.INSTANCE;
    }
}
