package com.example.bssm_dev.domain.api.exception;

import com.example.bssm_dev.exception.GlobalException;
import com.example.bssm_dev.exception.ErrorCode;

public class ApiTokenBlockedException extends GlobalException {
    private ApiTokenBlockedException() {
        super(ErrorCode.API_TOKEN_BLOCKED);
    }

    private static class Holder {
        private static final ApiTokenBlockedException INSTANCE = new ApiTokenBlockedException();
    }

    public static ApiTokenBlockedException raise() {
        return Holder.INSTANCE;
    }
}
