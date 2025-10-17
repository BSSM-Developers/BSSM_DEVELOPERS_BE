package com.example.bssm_dev.domain.api.exception;

import com.example.bssm_dev.global.error.exception.GlobalException;
import com.example.bssm_dev.global.error.exception.ErrorCode;

public class InvalidSecretKeyException extends GlobalException {
    private InvalidSecretKeyException() {
        super(ErrorCode.INVALID_SECRET_KEY);
    }

    private static class Holder {
        private static final InvalidSecretKeyException INSTANCE = new InvalidSecretKeyException();
    }

    public static InvalidSecretKeyException raise() {
        return Holder.INSTANCE;
    }
}
