package com.example.bssm_dev.domain.docs.controller.command;
import com.example.bssm_dev.common.annotation.CurrentUser;
import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.common.util.HttpUtil;
import com.example.bssm_dev.domain.docs.dto.request.AddDocsSectionRequest;
import com.example.bssm_dev.domain.docs.dto.request.UpdateDocsSectionTitleRequest;
import com.example.bssm_dev.domain.docs.service.command.DocsSectionCommandService;
import com.example.bssm_dev.domain.user.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/docs/{docsId}/section")
public class DocsSectionCommandController {
    private final DocsSectionCommandService docsSectionCommandService;

    /**
     * Docs Section 추가
    **/
    @PostMapping
    public ResponseEntity<ResponseDto<Void>> addDocsSection(
            @PathVariable("docsId") Long docsId,
            @Valid @RequestBody AddDocsSectionRequest request,
            @CurrentUser User currentUser
    ) {
        docsSectionCommandService.addSection(docsId, request, currentUser);
        ResponseDto<Void> responseDto = HttpUtil.success("Successfully added docs section");
        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/move")
    public ResponseEntity<ResponseDto<Void>> moveDocsSection(
            @PathVariable("docsId") Long docsId,
            @RequestParam("sortedDocsSectionIds") List<Long> sortedDocsSectionIds,
            @CurrentUser User currentUser
    ) {
        docsSectionCommandService.updateOrders(docsId, sortedDocsSectionIds, currentUser);
        ResponseDto<Void> responseDto = HttpUtil.success("Successfully updated docs section orders");
        return ResponseEntity.ok(responseDto);
    }


    /**
     * Docs Section 삭제
     **/
    @DeleteMapping("/{sectionId}")
    public ResponseEntity<ResponseDto<Void>> deleteDocsSection(
            @PathVariable("docsId") Long docsId,
            @PathVariable("sectionId") Long sectionId,
            @CurrentUser User currentUser
    ) {
        docsSectionCommandService.deleteSection(docsId, sectionId, currentUser);
        ResponseDto<Void> responseDto = HttpUtil.success("Successfully deleted docs section");
        return ResponseEntity.ok(responseDto);
    }


    /**
     * Docs Section Title 변경
     **/
    @PatchMapping("/{sectionId}/title")
    public ResponseEntity<ResponseDto<Void>> updateDocsSectionTitle(
            @PathVariable("docsId") Long docsId,
            @PathVariable("sectionId") Long sectionId,
            @Valid @RequestBody UpdateDocsSectionTitleRequest request,
            @CurrentUser User currentUser
    ) {
        docsSectionCommandService.updateTitle(docsId, sectionId, request, currentUser);
        ResponseDto<Void> responseDto = HttpUtil.success("Successfully updated docs section title");
        return ResponseEntity.ok(responseDto);
    }

}
