package com.example.bssm_dev.domain.api.controller.command;

import com.example.bssm_dev.common.annotation.CurrentUser;
import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.common.util.HttpUtil;
import com.example.bssm_dev.domain.api.dto.request.ApiUsageEndpointUpdateRequest;
import com.example.bssm_dev.domain.api.service.command.ApiUsageCommandService;
import com.example.bssm_dev.domain.user.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/{apiId}/{apiTokenId}/usage")
public class ApiUsageCommandController {
    private final ApiUsageCommandService apiUsageCommandService;

    @PatchMapping("/endpoint")
    public ResponseEntity<ResponseDto<Void>> changeEndpoint(
            @PathVariable Long apiId,
            @PathVariable Long apiTokenId,
            @CurrentUser User user,
            @Valid ApiUsageEndpointUpdateRequest apiUsageEndpointUpdateRequest
    ) {
        apiUsageCommandService.changeEndpoint(apiId, apiTokenId, user, apiUsageEndpointUpdateRequest);
        ResponseDto<Void> responseDto = HttpUtil.success("Successfully changed API use endpoint");
        return ResponseEntity.ok(responseDto);
    }
}
