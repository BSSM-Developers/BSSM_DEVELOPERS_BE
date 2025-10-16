package com.example.bssm_dev.domain.api.exception;

import com.example.bssm_dev.global.error.exception.GlobalException;
import com.example.bssm_dev.global.error.exception.ErrorCode;

public class ApiUseReasonNotFoundException extends GlobalException {
    private ApiUseReasonNotFoundException() {
        super(ErrorCode.API_USE_REASON_NOT_FOUND);
    }

    private static class Holder {
        private static final ApiUseReasonNotFoundException INSTANCE = new ApiUseReasonNotFoundException();
    }

    public static ApiUseReasonNotFoundException raise() {
        return Holder.INSTANCE;
    }
}
