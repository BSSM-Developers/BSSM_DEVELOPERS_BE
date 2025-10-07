package com.example.bssm_dev.domain.user.exception;

import com.example.bssm_dev.global.error.exception.ErrorCode;
import com.example.bssm_dev.global.error.exception.GlobalException;

public class UserNotFoundException extends GlobalException {

    private UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }

    static class Holder {
        private static final UserNotFoundException INSTANCE = new UserNotFoundException();
    }

    public static UserNotFoundException raise() {
        return Holder.INSTANCE;
    }
}
