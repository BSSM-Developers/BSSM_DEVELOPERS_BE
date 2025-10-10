package com.example.bssm_dev.domain.auth.validator;
import org.springframework.stereotype.Component;

@Component
public class EmailValidator {

    private static final String BSSM_EMAIL_DOMAIN = "@bssm.hs.kr";

    public boolean isBssmEmail(String email) {
        return email != null && email.endsWith(BSSM_EMAIL_DOMAIN);
    }
}
