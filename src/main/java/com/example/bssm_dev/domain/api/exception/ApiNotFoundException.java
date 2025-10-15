package com.example.bssm_dev.domain.api.exception;

import com.example.bssm_dev.global.error.exception.GlobalException;
import com.example.bssm_dev.global.error.exception.ErrorCode;

public class ApiNotFoundException extends GlobalException {
    private ApiNotFoundException() {
        super(ErrorCode.API_NOT_FOUND);
    }

    private static class Holder {
        private static final ApiNotFoundException INSTANCE = new ApiNotFoundException();
    }

    public static ApiNotFoundException raise() {
        return Holder.INSTANCE;
    }
}
