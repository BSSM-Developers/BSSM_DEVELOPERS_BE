package com.example.bssm_dev.domain.api.model.type;


import com.example.bssm_dev.domain.api.exception.InvalidApiUseReasonStateException;

public enum ApiUseState {
    PENDING,
    APPROVED,
    REJECTED;

    public static ApiUseState fromString(String stateParam) {
        try {
            return stateParam == null || stateParam.equalsIgnoreCase("ALL")
                    ? null
                    : ApiUseState.valueOf(stateParam.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw InvalidApiUseReasonStateException.raise();
        }
    }
}


