package com.example.bssm_dev.domain.docs.controller;

import com.example.bssm_dev.common.annotation.CurrentUser;
import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.common.util.HttpUtil;
import com.example.bssm_dev.domain.docs.dto.request.CreateDocsRequest;
import com.example.bssm_dev.domain.docs.dto.response.DocsResponse;
import com.example.bssm_dev.domain.docs.service.DocsCommandService;
import com.example.bssm_dev.domain.user.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/docs")
public class DocsCommandController {
    private final DocsCommandService docsService;

    /**
     * 오리지널 Docs 생성
     */
    @PostMapping
    public ResponseEntity<ResponseDto<Void>> createDocs(
            @Valid @RequestBody CreateDocsRequest request,
            @CurrentUser User user
    ) {
        docsService.createDocs(request, user);
        ResponseDto<Void> responseDto = HttpUtil.success("Successfully created docs");
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Docs 삭제
     */
    @DeleteMapping("/{docsId}")
    public ResponseEntity<ResponseDto<Void>> deleteDocs(
            @PathVariable Long docsId,
            @CurrentUser User user
    ) {
        docsService.deleteDocs(docsId, user);
        ResponseDto<Void> responseDto = HttpUtil.success("Successfully deleted docs");
        return ResponseEntity.ok(responseDto);
    }
}
