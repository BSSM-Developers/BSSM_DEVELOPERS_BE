package com.example.bssm_dev.domain.github.exception;

import com.example.bssm_dev.exception.ErrorCode;
import com.example.bssm_dev.exception.GlobalException;

public class GitHubRepositoryUnauthorizedException extends GlobalException {

    private GitHubRepositoryUnauthorizedException() {
        super(ErrorCode.GITHUB_REPOSITORY_UNAUTHORIZED);
    }

    static class Holder {
        private static final GitHubRepositoryUnauthorizedException INSTANCE = new GitHubRepositoryUnauthorizedException();
    }

    public static GitHubRepositoryUnauthorizedException raise() {
        return Holder.INSTANCE;
    }
}
