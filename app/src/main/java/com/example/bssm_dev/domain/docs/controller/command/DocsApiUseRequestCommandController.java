package com.example.bssm_dev.domain.docs.controller.command;

import com.example.bssm_dev.common.annotation.CurrentUser;
import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.common.util.HttpUtil;
import com.example.bssm_dev.domain.docs.dto.request.CreateDocsApiUseRequest;
import com.example.bssm_dev.domain.docs.service.command.DocsApiUseRequestCommandService;
import com.example.bssm_dev.domain.user.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/docs")
public class DocsApiUseRequestCommandController {
    private final DocsApiUseRequestCommandService docsApiUseRequestCommandService;

    /**
     * Docs 전체 API 사용 신청
     */
    @PostMapping("/{docsId}/api-use-requests")
    public ResponseEntity<ResponseDto<Void>> applyForDocs(
            @PathVariable String docsId,
            @RequestParam Long apiTokenId,
            @Valid @RequestBody CreateDocsApiUseRequest request,
            @CurrentUser User user
    ) {
        docsApiUseRequestCommandService.applyForDocs(docsId, apiTokenId, request.useReason(), user);
        ResponseDto<Void> responseDto = HttpUtil.success("Successfully applied for docs API use");
        return ResponseEntity.ok(responseDto);
    }
}
