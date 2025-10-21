package com.example.bssm_dev.domain.docs.controller.query;

import com.example.bssm_dev.common.dto.CursorPage;
import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.common.util.HttpUtil;
import com.example.bssm_dev.domain.docs.dto.response.DocsDetailResponse;
import com.example.bssm_dev.domain.docs.dto.response.DocsListResponse;
import com.example.bssm_dev.common.annotation.CurrentUser;
import com.example.bssm_dev.domain.docs.service.query.DocsQueryService;
import com.example.bssm_dev.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/docs")
public class DocsQueryController {
    private final DocsQueryService docsQueryService;

    /**
     * 문서 조회 by 커서 기반 페이지네이션
     */
    @GetMapping
    public ResponseEntity<ResponseDto<CursorPage<DocsListResponse>>> getAllDocs(
            @RequestParam(required = false) Long cursor,
            @RequestParam(required = false, defaultValue = "20") Integer size
    ) {
        CursorPage<DocsListResponse> response = docsQueryService.getAllDocs(cursor, size);
        ResponseDto<CursorPage<DocsListResponse>> responseDto = HttpUtil.success("Successfully retrieved all docs", response);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 내가 작성한 문서 조회 by 커서 기반 페이지네이션
     */
    @GetMapping("/my")
    public ResponseEntity<ResponseDto<CursorPage<DocsListResponse>>> getMyDocs(
            @RequestParam(required = false) Long cursor,
            @RequestParam(required = false, defaultValue = "20") Integer size,
            @CurrentUser User currentUser
    ) {
        CursorPage<DocsListResponse> response = docsQueryService.getMyDocs(currentUser.getUserId(), cursor, size);
        ResponseDto<CursorPage<DocsListResponse>> responseDto = HttpUtil.success("Successfully retrieved my docs", response);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * 문서 상세 조회
     */
    @GetMapping("/{docsId}")
    public ResponseEntity<ResponseDto<DocsDetailResponse>> getDocsDetail(@PathVariable Long docsId) {
        DocsDetailResponse response = docsQueryService.getDocsDetail(docsId);
        ResponseDto<DocsDetailResponse> responseDto = HttpUtil.success("Successfully retrieved docs detail", response);
        return ResponseEntity.ok(responseDto);
    }
}
