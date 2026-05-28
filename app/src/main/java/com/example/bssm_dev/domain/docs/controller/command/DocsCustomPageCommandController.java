package com.example.bssm_dev.domain.docs.controller.command;

import com.example.bssm_dev.common.annotation.CurrentUser;
import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.common.util.HttpUtil;
import com.example.bssm_dev.domain.docs.dto.request.AddCustomDocsPageRequest;
import com.example.bssm_dev.domain.docs.service.command.DocsPageCommandService;
import com.example.bssm_dev.domain.user.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/docs/{docsId}/page")
public class DocsCustomPageCommandController {
    private final DocsPageCommandService docsPageCommandService;

    /**
     * 커스텀 Docs에 페이지 추가 (content 또는 reference)
     */
    @PostMapping
    public ResponseEntity<ResponseDto<Void>> addPage(
            @PathVariable String docsId,
            @Valid @RequestBody AddCustomDocsPageRequest request,
            @CurrentUser User user
    ) {
        docsPageCommandService.addPage(docsId, request, user);
        return ResponseEntity.ok(HttpUtil.success("Successfully added docs page"));
    }

    /**
     * 커스텀 Docs 페이지 삭제
     */
    @DeleteMapping("/{mappedId}")
    public ResponseEntity<ResponseDto<Void>> deletePage(
            @PathVariable String docsId,
            @PathVariable String mappedId,
            @CurrentUser User user
    ) {
        docsPageCommandService.deletePage(docsId, mappedId, user);
        return ResponseEntity.ok(HttpUtil.success("Successfully deleted docs page"));
    }
}
