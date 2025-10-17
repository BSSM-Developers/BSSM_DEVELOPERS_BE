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

    @PostMapping("/{token}/{endpoint}")
    public ResponseEntity<ProxyResponse> useApi(@Valid @RequestBody UseApiRequest useApiRequest, @PathVariable String token, @PathVariable String endpoint) {
        ProxyResponse response = useApiCommandService.execute(useApiRequest, token, endpoint);
        return ResponseEntity.ok(response);
    }
}
