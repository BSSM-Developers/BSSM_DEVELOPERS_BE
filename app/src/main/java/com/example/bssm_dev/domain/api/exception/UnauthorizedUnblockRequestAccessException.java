package com.example.bssm_dev.domain.api.exception;

import com.example.bssm_dev.exception.ErrorCode;
import com.example.bssm_dev.exception.GlobalException;

public class UnauthorizedUnblockRequestAccessException extends GlobalException {

    private UnauthorizedUnblockRequestAccessException() {
        super(ErrorCode.UNAUTHORIZED_UNBLOCK_REQUEST_ACCESS);
    }

    public static UnauthorizedUnblockRequestAccessException raise() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final UnauthorizedUnblockRequestAccessException INSTANCE = new UnauthorizedUnblockRequestAccessException();
    }
}
