package com.example.bssm_dev.domain.auth.exception;

import com.example.bssm_dev.global.error.exception.ErrorCode;
import com.example.bssm_dev.global.error.exception.GlobalException;

public class InvalidTokenException extends GlobalException {

    private InvalidTokenException() {
        super(ErrorCode.INVALID_TOKEN);
    }

    static class Holder {
        private static final InvalidTokenException INSTANCE = new InvalidTokenException();
    }

    public static InvalidTokenException raise() {
        return Holder.INSTANCE;
    }
}