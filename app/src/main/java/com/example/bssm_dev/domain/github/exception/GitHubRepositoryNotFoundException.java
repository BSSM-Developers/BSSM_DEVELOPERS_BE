package com.example.bssm_dev.domain.github.exception;

import com.example.bssm_dev.exception.ErrorCode;
import com.example.bssm_dev.exception.GlobalException;

public class GitHubRepositoryNotFoundException extends GlobalException {

    private GitHubRepositoryNotFoundException() {
        super(ErrorCode.GITHUB_REPOSITORY_NOT_FOUND);
    }

    static class Holder {
        private static final GitHubRepositoryNotFoundException INSTANCE = new GitHubRepositoryNotFoundException();
    }

    public static GitHubRepositoryNotFoundException raise() {
        return Holder.INSTANCE;
    }
}
