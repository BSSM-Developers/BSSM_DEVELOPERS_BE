package com.example.bssm_dev.domain.signup.exception;

import com.example.bssm_dev.global.error.exception.ErrorCode;
import com.example.bssm_dev.global.error.exception.GlobalException;

public class InvalidSignupStateException extends GlobalException {

    private InvalidSignupStateException() {
        super(ErrorCode.INVALID_SIGNUP_STATE);
    }

    static class Holder {
        private static final InvalidSignupStateException INSTANCE = new InvalidSignupStateException();
    }

    public static InvalidSignupStateException raise() {
        return Holder.INSTANCE;
    }
}
