package com.example.bssm_dev.domain.signup.model.type;

public enum SignUpFormState {
    PENDING, APPROVED, REJECTED;

    public static SignUpFormState fromString(String state) {
        if (state == null || state.isBlank()) {
            return null;
        }
        
        try {
            return SignUpFormState.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw com.example.bssm_dev.domain.signup.exception.InvalidSignupStateException.raise();
        }
    }
}
