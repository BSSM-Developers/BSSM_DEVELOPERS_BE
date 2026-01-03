package com.example.bssm_dev.domain.api.exception;

import com.example.bssm_dev.exception.ErrorCode;
import com.example.bssm_dev.exception.GlobalException;

public class ApiTokenNotBlockedException extends GlobalException {

    private ApiTokenNotBlockedException() {
        super(ErrorCode.API_TOKEN_NOT_BLOCKED);
    }

    public static ApiTokenNotBlockedException raise() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final ApiTokenNotBlockedException INSTANCE = new ApiTokenNotBlockedException();
    }
}
