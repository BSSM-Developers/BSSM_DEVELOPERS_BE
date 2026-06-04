package com.example.bssm_dev.domain.api.exception;

import com.example.bssm_dev.exception.GlobalException;
import com.example.bssm_dev.exception.ErrorCode;

public class UnauthorizedApiTokenAccessException extends GlobalException {
    private UnauthorizedApiTokenAccessException() {
        super(ErrorCode.UNAUTHORIZED_API_TOKEN_ACCESS);
    }

    private static class Holder {
        private static final UnauthorizedApiTokenAccessException INSTANCE = new UnauthorizedApiTokenAccessException();
    }

    public static UnauthorizedApiTokenAccessException raise() {
        return Holder.INSTANCE;
    }
}
