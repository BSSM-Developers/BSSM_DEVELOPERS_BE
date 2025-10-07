package com.example.bssm_dev.domain.signup.exception;

import com.example.bssm_dev.global.error.exception.ErrorCode;
import com.example.bssm_dev.global.error.exception.GlobalException;

public class UnauthorizedSignupAccessException extends GlobalException {

    private UnauthorizedSignupAccessException() {
        super(ErrorCode.UNAUTHORIZED_SIGNUP_ACCESS);
    }

    static class Holder {
        private static final UnauthorizedSignupAccessException INSTANCE = new UnauthorizedSignupAccessException();
    }

    public static UnauthorizedSignupAccessException raise() {
        return Holder.INSTANCE;
    }
}
