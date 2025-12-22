package com.example.bssm_dev.domain.api.exception;

import com.example.bssm_dev.exception.GlobalException;
import com.example.bssm_dev.exception.ErrorCode;

public class ApiUsageEndpointAlreadyExistsException extends GlobalException {
    private ApiUsageEndpointAlreadyExistsException() {
        super(ErrorCode.API_USAGE_ENDPOINT_ALREADY_EXISTS);
    }

    private static class Holder {
        private static final ApiUsageEndpointAlreadyExistsException INSTANCE = new ApiUsageEndpointAlreadyExistsException();
    }

    public static ApiUsageEndpointAlreadyExistsException raise() {
        return Holder.INSTANCE;
    }
}
