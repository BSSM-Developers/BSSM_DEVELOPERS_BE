package com.example.bssm_dev.domain.docs.controller.command;

import com.example.bssm_dev.common.annotation.CurrentUser;
import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.common.util.HttpUtil;
import com.example.bssm_dev.domain.docs.dto.request.AddDocsPageRequest;
import com.example.bssm_dev.domain.docs.dto.request.AddApiDocsPageRequest;
import com.example.bssm_dev.domain.docs.dto.request.UpdateDocsPageRequest;
import com.example.bssm_dev.domain.docs.service.command.DocsPageCommandService;
import com.example.bssm_dev.domain.user.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/docs/{docsId}/section/{sectionId}/page")
public class DocsPageCommandController {
    private final DocsPageCommandService docsPageCommandService;

    /**
     * Docs page 추가
     **/
    @PostMapping
    public ResponseEntity<ResponseDto<Void>> addDocsPage(
            @PathVariable("docsId") Long docsId,
            @PathVariable("sectionId") Long sectionId,
            @Valid @RequestBody AddDocsPageRequest request,
            @CurrentUser User currentUser
    ) {
        docsPageCommandService.addPage(docsId, sectionId, request, currentUser);
        ResponseDto<Void> responseDto = HttpUtil.success("Successfully added docs page");
        return ResponseEntity.ok(responseDto);
    }

    /**
     * API Docs page 추가
     **/
    @PostMapping("/api")
    public ResponseEntity<ResponseDto<Void>> addApiDocsPage(
            @PathVariable("docsId") Long docsId,
            @PathVariable("sectionId") Long sectionId,
            @Valid @RequestBody AddApiDocsPageRequest request,
            @CurrentUser User currentUser
    ) {
        docsPageCommandService.addApiPage(docsId, sectionId, request, currentUser);
        ResponseDto<Void> responseDto = HttpUtil.success("Successfully added api docs page");
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Docs Page 순서 정렬
     **/
    @PatchMapping("/move")
    public ResponseEntity<ResponseDto<Void>> moveDocsPage(
            @PathVariable("docsId") Long docsId,
            @PathVariable("sectionId") Long sectionId,
            @RequestParam("sortedDocsPageIds") List<Long> sortedDocsPageIds,
            @CurrentUser User currentUser
    ) {
        docsPageCommandService.updateOrders(docsId, sectionId, sortedDocsPageIds, currentUser);
        ResponseDto<Void> responseDto = HttpUtil.success("Successfully updated docs page orders");
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Docs Page 삭제
     **/
    @DeleteMapping("/{pageId}")
    public ResponseEntity<ResponseDto<Void>> deleteDocsPage(
            @PathVariable("docsId") Long docsId,
            @PathVariable("sectionId") Long sectionId,
            @PathVariable("pageId") Long pageId,
            @CurrentUser User currentUser
    ) {
        docsPageCommandService.deletePage(docsId, sectionId, pageId, currentUser);
        ResponseDto<Void> responseDto = HttpUtil.success("Successfully deleted docs page");
        return ResponseEntity.ok(responseDto);
    }


    /**
     * Docs Page 수정 (Title, Description)
     **/
    @PatchMapping("/{pageId}")
    public ResponseEntity<ResponseDto<Void>> updateDocsPage(
            @PathVariable("docsId") Long docsId,
            @PathVariable("pageId") Long pageId,
            @Valid @RequestBody UpdateDocsPageRequest request,
            @CurrentUser User currentUser
    ) {
        docsPageCommandService.updatePage(docsId, pageId, request, currentUser);
        ResponseDto<Void> responseDto = HttpUtil.success("Successfully updated docs page");
        return ResponseEntity.ok(responseDto);
    }

}

