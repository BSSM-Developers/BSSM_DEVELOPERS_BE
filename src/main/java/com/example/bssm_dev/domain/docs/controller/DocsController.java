package com.example.bssm_dev.domain.docs.controller;

import com.example.bssm_dev.common.annotation.CurrentUser;
import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.common.util.HttpUtil;
import com.example.bssm_dev.domain.docs.dto.request.CreateDocsRequest;
import com.example.bssm_dev.domain.docs.dto.response.DocsResponse;
import com.example.bssm_dev.domain.docs.service.DocsService;
import com.example.bssm_dev.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/docs")
public class DocsController {
    private final DocsService docsService;

    @PostMapping
    public ResponseEntity<ResponseDto<DocsResponse>> createDocs(
            @RequestBody CreateDocsRequest request,
            @CurrentUser User user
            ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = (String) authentication.getPrincipal();
        
        DocsResponse response = docsService.createDocs(request, user);
        ResponseDto<DocsResponse> responseDto = HttpUtil.success("Successfully created docs", response);
        return ResponseEntity.ok(responseDto);
    }
}
