package com.example.bssm_dev.domain.api.exception;

import com.example.bssm_dev.global.error.exception.GlobalException;
import com.example.bssm_dev.global.error.exception.ErrorCode;

public class ApiUsageAlreadyExistsException extends GlobalException {
    private ApiUsageAlreadyExistsException() {
        super(ErrorCode.API_USAGE_ALREADY_EXISTS);
    }

    private static class Holder {
        private static final ApiUsageAlreadyExistsException INSTANCE = new ApiUsageAlreadyExistsException();
    }

    public static ApiUsageAlreadyExistsException raise() {
        return Holder.INSTANCE;
    }
}
