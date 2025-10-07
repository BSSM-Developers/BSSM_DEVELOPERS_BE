package com.example.bssm_dev.domain.auth.exception;

import com.example.bssm_dev.global.error.exception.ErrorCode;
import com.example.bssm_dev.global.error.exception.GlobalException;

public class RefreshTokenNotFoundException extends GlobalException {

    private RefreshTokenNotFoundException() {
        super(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
    }

    static class Holder {
        private static final RefreshTokenNotFoundException INSTANCE = new RefreshTokenNotFoundException();
    }

    public static RefreshTokenNotFoundException raise() {
        return Holder.INSTANCE;
    }
}