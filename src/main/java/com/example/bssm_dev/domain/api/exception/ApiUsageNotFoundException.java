package com.example.bssm_dev.domain.api.exception;

import com.example.bssm_dev.global.error.exception.GlobalException;
import com.example.bssm_dev.global.error.exception.ErrorCode;

public class ApiUsageNotFoundException extends GlobalException {
    private ApiUsageNotFoundException() {
        super(ErrorCode.API_USAGE_NOT_FOUND);
    }

    private static class Holder {
        private static final ApiUsageNotFoundException INSTANCE = new ApiUsageNotFoundException();
    }

    public static ApiUsageNotFoundException raise() {
        return Holder.INSTANCE;
    }
}
