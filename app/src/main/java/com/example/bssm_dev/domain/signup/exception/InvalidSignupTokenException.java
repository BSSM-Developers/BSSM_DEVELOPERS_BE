package com.example.bssm_dev.domain.signup.exception;

import com.example.bssm_dev.exception.ErrorCode;
import com.example.bssm_dev.exception.GlobalException;

public class InvalidSignupTokenException extends GlobalException {

    private InvalidSignupTokenException() {
        super(ErrorCode.INVALID_SIGNUP_TOKEN);
    }

    static class Holder {
        private static final InvalidSignupTokenException INSTANCE = new InvalidSignupTokenException();
    }

    public static InvalidSignupTokenException raise() {
        return Holder.INSTANCE;
    }
}
