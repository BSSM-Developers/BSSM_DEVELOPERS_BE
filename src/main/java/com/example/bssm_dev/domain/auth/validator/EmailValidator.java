package com.example.bssm_dev.domain.auth.validator;
import com.example.bssm_dev.domain.auth.exception.InvalidBssmEmailException;
import org.springframework.stereotype.Component;

@Component
public class EmailValidator {

    public void isBssmEmail(String email) {
        boolean isBssmEmail = email != null && email.endsWith("@bssm.hs.kr");
        // bssm.hs.kr 형식이 아니면 오류 발생
        if (!isBssmEmail) throw InvalidBssmEmailException.raise();

    }
}
