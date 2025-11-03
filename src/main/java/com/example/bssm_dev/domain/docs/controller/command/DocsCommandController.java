package com.example.bssm_dev.domain.docs.controller.command;

import com.example.bssm_dev.common.annotation.CurrentUser;
import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.common.util.HttpUtil;
import com.example.bssm_dev.domain.docs.dto.request.CreateCustomDocsRequest;
import com.example.bssm_dev.domain.docs.dto.request.CreateOriginalDocsRequest;
import com.example.bssm_dev.domain.docs.dto.request.UpdateDocsRequest;
import com.example.bssm_dev.domain.docs.service.command.DocsCommandService;
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
    @PostMapping("/original")
    public ResponseEntity<ResponseDto<Void>> createOriginalDocs(
            @Valid @RequestBody CreateOriginalDocsRequest request,
            @CurrentUser User user
    ) {
        docsService.createOriginalDocs(request, user);
        ResponseDto<Void> responseDto = HttpUtil.success("Successfully created docs");
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 커스텀 Docs 생성 (빈 독스)
     */
    @PostMapping("/custom")
    public ResponseEntity<ResponseDto<Void>> createCustomDocs(
            @RequestBody @Valid CreateCustomDocsRequest request,
            @CurrentUser User user
    ) {
        docsService.createCustomDocs(request, user);
        ResponseDto<Void> responseDto = HttpUtil.success("Successfully created custom docs");
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Docs 삭제
     */
    @DeleteMapping("/{docsId}")
    public ResponseEntity<ResponseDto<Void>> deleteDocs(
            @PathVariable String docsId,
            @CurrentUser User user
    ) {
        docsService.deleteDocs(docsId, user);
        ResponseDto<Void> responseDto = HttpUtil.success("Successfully deleted docs");
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Docs 수정 (Title, Description, Domain, RepositoryUrl)
     */
    @PatchMapping("/{docsId}")
    public ResponseEntity<ResponseDto<Void>> updateDocs(
            @PathVariable String docsId,
            @Valid
            @RequestBody UpdateDocsRequest request,
            @CurrentUser User user
    ) {
        docsService.updateDocs(docsId, request, user);
        ResponseDto<Void> responseDto = HttpUtil.success("Successfully updated docs");
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Docs Auto Approval 토글 변경
     */
    @PatchMapping("/{docsId}/auto-approval")
    public ResponseEntity<ResponseDto<Void>> updateDocsAutoApproval(
            @PathVariable String docsId,
            @CurrentUser User user
    ) {
        docsService.updateDocsAutoApproval(docsId, user);
        ResponseDto<Void> responseDto = HttpUtil.success("Successfully updated docs");
        return ResponseEntity.ok(responseDto);
    }

}
