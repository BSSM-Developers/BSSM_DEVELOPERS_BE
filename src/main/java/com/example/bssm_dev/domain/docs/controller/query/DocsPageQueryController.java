package com.example.bssm_dev.domain.docs.controller.query;

import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.common.util.HttpUtil;
import com.example.bssm_dev.domain.docs.dto.response.DocsPageResponse;
import com.example.bssm_dev.domain.docs.service.query.DocsPageQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/docs/{docsId}/page/{mappedId}")
public class DocsPageQueryController {
    private final DocsPageQueryService docsPageQueryService;

    /**
     * Docs Page 조회
     */
    @GetMapping
    public ResponseEntity<ResponseDto<DocsPageResponse>> getDocsPageByDocsId(
            @PathVariable("docsId") String docsId,
            @PathVariable("mappedId") String mappedId
    ) {
        DocsPageResponse docsPageResponse = docsPageQueryService.getPage(docsId, mappedId);
        ResponseDto<DocsPageResponse> responseDto = HttpUtil.success("Successfully retrieved docs page", docsPageResponse);
        return ResponseEntity.ok(responseDto);
    }


}
