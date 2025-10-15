package com.example.bssm_dev.domain.api.controller.command;

import com.example.bssm_dev.common.annotation.CurrentUser;
import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.common.util.HttpUtil;
import com.example.bssm_dev.domain.api.service.command.ApiTokenCommandService;
import com.example.bssm_dev.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/token")
public class ApiTokenCommandController {
    private final ApiTokenCommandService apiTokenCommandService;
    @PostMapping
    public ResponseEntity<ResponseDto<Void>> createApiToken(
            @CurrentUser User user
    ) {
        apiTokenCommandService.createApiToken(user);
        ResponseDto<Void> responseDto = HttpUtil.success("Successfully created API token");
        return ResponseEntity.ok(responseDto);
    }
}
