package com.example.bssm_dev.domain.api.controller.command;

import com.example.bssm_dev.domain.api.dto.request.UseApiRequest;
import com.example.bssm_dev.domain.api.dto.response.ProxyResponse;
import com.example.bssm_dev.domain.api.service.command.UseApiCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/use")
public class UseApiCommandController {
    private final UseApiCommandService useApiCommandService;

    @PostMapping("/{endpoint}")
    public ResponseEntity<ProxyResponse> useApi(
            @PathVariable String endpoint,
            @RequestHeader("bssm-dev-token")  String token,
            @RequestHeader("bssm-dev-secret")  String secretKey
    ) {
        ProxyResponse response = useApiCommandService.execute(secretKey, token, endpoint);
        return ResponseEntity.ok(response);
    }
}
