package com.example.bssm_dev.domain.docs.controller;

import com.example.bssm_dev.common.dto.CursorPage;
import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.common.util.HttpUtil;
import com.example.bssm_dev.domain.docs.dto.response.DocsDetailResponse;
import com.example.bssm_dev.domain.docs.dto.response.DocsListResponse;
import com.example.bssm_dev.domain.docs.service.DocsQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/docs")
public class DocsQueryController {
    private final DocsQueryService docsQueryService;

    @GetMapping
    public ResponseEntity<ResponseDto<CursorPage<DocsListResponse>>> getAllDocs(
            @RequestParam(required = false) Long cursor,
            @RequestParam(required = false, defaultValue = "20") Integer size
    ) {
        CursorPage<DocsListResponse> response = docsQueryService.getAllDocs(cursor, size);
        ResponseDto<CursorPage<DocsListResponse>> responseDto = HttpUtil.success("Successfully retrieved all docs", response);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{docsId}")
    public ResponseEntity<ResponseDto<DocsDetailResponse>> getDocsDetail(@PathVariable Long docsId) {
        DocsDetailResponse response = docsQueryService.getDocsDetail(docsId);
        ResponseDto<DocsDetailResponse> responseDto = HttpUtil.success("Successfully retrieved docs detail", response);
        return ResponseEntity.ok(responseDto);
    }
}
