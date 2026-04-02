package com.example.bssm_dev.domain.api.service.command;

import com.example.bssm_dev.domain.api.model.Api;
import com.example.bssm_dev.domain.api.model.ApiUseReason;
import com.example.bssm_dev.domain.api.model.ApiUsage;
import com.example.bssm_dev.domain.api.model.TryItToken;
import com.example.bssm_dev.domain.api.model.type.ApiUseState;
import com.example.bssm_dev.domain.api.repository.ApiUseReasonRepository;
import com.example.bssm_dev.domain.api.repository.ApiUsageRepository;
import com.example.bssm_dev.domain.api.repository.TryItTokenRepository;
import com.example.bssm_dev.global.config.properties.TryItProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional("transactionManager")
public class TryItTokenCommandService {
    private final TryItTokenRepository tryItTokenRepository;
    private final ApiUseReasonRepository apiUseReasonRepository;
    private final ApiUsageRepository apiUsageRepository;
    private final TryItProperties tryItProperties;
    private final PasswordEncoder passwordEncoder;

    public void createTryItToken(Api api) {
        String encodedSecretKey = passwordEncoder.encode(UUID.randomUUID().toString().replace("-", ""));

        TryItToken tryItToken = TryItToken.of(api, encodedSecretKey);

        tryItToken.addTokenOrigin(tryItProperties.getOrigin());

        TryItToken savedToken = tryItTokenRepository.save(tryItToken);

        ApiUseReason useReason = ApiUseReason.of(
                api.getCreator(),
                api,
                savedToken,
                "Try It",
                ApiUseState.APPROVED
        );
        ApiUseReason savedReason = apiUseReasonRepository.save(useReason);

        ApiUsage usage = ApiUsage.of(savedToken, api, savedReason, api.getName(), api.getEndpoint());
        apiUsageRepository.save(usage);
    }
}
