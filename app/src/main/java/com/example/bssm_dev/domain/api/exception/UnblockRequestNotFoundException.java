package com.example.bssm_dev.domain.api.exception;

import com.example.bssm_dev.exception.ErrorCode;
import com.example.bssm_dev.exception.GlobalException;

public class UnblockRequestNotFoundException extends GlobalException {

    private UnblockRequestNotFoundException() {
        super(ErrorCode.UNBLOCK_REQUEST_NOT_FOUND);
    }

    public static UnblockRequestNotFoundException raise() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final UnblockRequestNotFoundException INSTANCE = new UnblockRequestNotFoundException();
    }
}
