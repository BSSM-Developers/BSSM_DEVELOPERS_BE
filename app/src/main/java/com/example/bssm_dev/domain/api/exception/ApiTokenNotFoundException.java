package com.example.bssm_dev.domain.api.exception;

import com.example.bssm_dev.exception.GlobalException;
import com.example.bssm_dev.exception.ErrorCode;

public class ApiTokenNotFoundException extends GlobalException {
    private ApiTokenNotFoundException() {
        super(ErrorCode.API_TOKEN_NOT_FOUND);
    }

    private static class Holder {
        private static final ApiTokenNotFoundException INSTANCE = new ApiTokenNotFoundException();
    }

    public static ApiTokenNotFoundException raise() {
        return Holder.INSTANCE;
    }
}
