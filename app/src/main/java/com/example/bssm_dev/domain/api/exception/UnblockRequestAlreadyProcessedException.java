package com.example.bssm_dev.domain.api.exception;

import com.example.bssm_dev.exception.ErrorCode;
import com.example.bssm_dev.exception.GlobalException;

public class UnblockRequestAlreadyProcessedException extends GlobalException {

    private UnblockRequestAlreadyProcessedException() {
        super(ErrorCode.UNBLOCK_REQUEST_ALREADY_PROCESSED);
    }

    public static UnblockRequestAlreadyProcessedException raise() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static final UnblockRequestAlreadyProcessedException INSTANCE = new UnblockRequestAlreadyProcessedException();
    }
}
