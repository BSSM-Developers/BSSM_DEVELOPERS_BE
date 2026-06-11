package com.example.bssm_dev.domain.docs.controller.command;

import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.common.util.HttpUtil;
import com.example.bssm_dev.domain.docs.service.command.DocsHealthCheckCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal")
public class DocsHealthCheckController {

    private final DocsHealthCheckCommandService docsHealthCheckCommandService;

    @PostMapping("/api-health-check")
    public ResponseEntity<ResponseDto<Void>> checkApiHealth() {
        docsHealthCheckCommandService.checkAll();
        return ResponseEntity.ok(HttpUtil.success("Health check completed"));
    }
}
