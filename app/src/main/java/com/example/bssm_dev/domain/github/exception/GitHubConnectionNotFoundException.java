package com.example.bssm_dev.domain.github.exception;

import com.example.bssm_dev.exception.ErrorCode;
import com.example.bssm_dev.exception.GlobalException;

public class GitHubConnectionNotFoundException extends GlobalException {

    private GitHubConnectionNotFoundException() {
        super(ErrorCode.GITHUB_CONNECTION_NOT_FOUND);
    }

    static class Holder {
        private static final GitHubConnectionNotFoundException INSTANCE = new GitHubConnectionNotFoundException();
    }

    public static GitHubConnectionNotFoundException raise() {
        return Holder.INSTANCE;
    }
}
