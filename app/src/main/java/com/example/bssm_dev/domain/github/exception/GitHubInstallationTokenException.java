package com.example.bssm_dev.domain.github.exception;

import com.example.bssm_dev.exception.ErrorCode;
import com.example.bssm_dev.exception.GlobalException;

public class GitHubInstallationTokenException extends GlobalException {

    private GitHubInstallationTokenException() {
        super(ErrorCode.GITHUB_INSTALLATION_TOKEN_FAIL);
    }

    static class Holder {
        private static final GitHubInstallationTokenException INSTANCE = new GitHubInstallationTokenException();
    }

    public static GitHubInstallationTokenException raise() {
        return Holder.INSTANCE;
    }
}
