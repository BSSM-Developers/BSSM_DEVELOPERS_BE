package com.example.bssm_dev.domain.github.exception;

import com.example.bssm_dev.exception.ErrorCode;
import com.example.bssm_dev.exception.GlobalException;

public class GitHubTokenExchangeFailedException extends GlobalException {

    private GitHubTokenExchangeFailedException() {
        super(ErrorCode.GITHUB_TOKEN_EXCHANGE_FAIL);
    }

    static class Holder {
        private static final GitHubTokenExchangeFailedException INSTANCE = new GitHubTokenExchangeFailedException();
    }

    public static GitHubTokenExchangeFailedException raise() {
        return Holder.INSTANCE;
    }
}
