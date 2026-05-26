package com.example.bssm_dev.domain.github.exception;

import com.example.bssm_dev.exception.ErrorCode;
import com.example.bssm_dev.exception.GlobalException;

public class GitHubAppNotInstalledException extends GlobalException {

    private GitHubAppNotInstalledException() {
        super(ErrorCode.GITHUB_APP_NOT_INSTALLED);
    }

    static class Holder {
        private static final GitHubAppNotInstalledException INSTANCE = new GitHubAppNotInstalledException();
    }

    public static GitHubAppNotInstalledException raise() {
        return Holder.INSTANCE;
    }
}
