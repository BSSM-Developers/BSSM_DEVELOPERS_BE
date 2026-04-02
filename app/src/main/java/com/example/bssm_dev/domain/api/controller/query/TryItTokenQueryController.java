package com.example.bssm_dev.domain.api.controller.query;

import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.common.util.HttpUtil;
import com.example.bssm_dev.domain.api.dto.response.TryItTokenResponse;
import com.example.bssm_dev.domain.api.service.query.TryItTokenQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/apis")
public class TryItTokenQueryController {
    private final TryItTokenQueryService tryItTokenQueryService;

    /**
     * API Try It 토큰 조회
     */
    @GetMapping("/{apiId}/try-it-token")
    public ResponseEntity<ResponseDto<TryItTokenResponse>> getTryItToken(
            @PathVariable String apiId
    ) {
        TryItTokenResponse response = tryItTokenQueryService.findByApiId(apiId);
        return ResponseEntity.ok(HttpUtil.success("Successfully retrieved try-it token", response));
    }
}
