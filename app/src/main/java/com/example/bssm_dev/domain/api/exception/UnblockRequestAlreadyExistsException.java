package com.example.bssm_dev.domain.api.exception;

import com.example.bssm_dev.exception.ErrorCode;
import com.example.bssm_dev.exception.GlobalException;

public class UnblockRequestAlreadyExistsException extends GlobalException {

    private UnblockRequestAlreadyExistsException() {
        super(ErrorCode.UNBLOCK_REQUEST_ALREADY_EXISTS);
    }

    public static UnblockRequestAlreadyExistsException raise() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final UnblockRequestAlreadyExistsException INSTANCE = new UnblockRequestAlreadyExistsException();
    }
}
