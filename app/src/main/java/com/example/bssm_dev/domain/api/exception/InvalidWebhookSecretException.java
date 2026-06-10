package com.example.bssm_dev.domain.api.exception;

import com.example.bssm_dev.exception.ErrorCode;
import com.example.bssm_dev.exception.GlobalException;

public class InvalidWebhookSecretException extends GlobalException {
    private InvalidWebhookSecretException() {
        super(ErrorCode.INVALID_WEBHOOK_SECRET);
    }

    private static class Holder {
        private static final InvalidWebhookSecretException INSTANCE = new InvalidWebhookSecretException();
    }

    public static InvalidWebhookSecretException raise() {
        return Holder.INSTANCE;
    }
}
