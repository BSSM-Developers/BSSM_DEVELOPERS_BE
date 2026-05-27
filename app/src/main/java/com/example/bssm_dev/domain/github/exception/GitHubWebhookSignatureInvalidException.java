package com.example.bssm_dev.domain.github.exception;

import com.example.bssm_dev.exception.ErrorCode;
import com.example.bssm_dev.exception.GlobalException;

public class GitHubWebhookSignatureInvalidException extends GlobalException {

    private GitHubWebhookSignatureInvalidException() {
        super(ErrorCode.GITHUB_WEBHOOK_SIGNATURE_INVALID);
    }

    static class Holder {
        private static final GitHubWebhookSignatureInvalidException INSTANCE = new GitHubWebhookSignatureInvalidException();
    }

    public static GitHubWebhookSignatureInvalidException raise() {
        return Holder.INSTANCE;
    }
}
