package com.example.bssm_dev.domain.docs.controller.command;

import com.example.bssm_dev.common.annotation.CurrentUser;
import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.common.util.HttpUtil;
import com.example.bssm_dev.domain.docs.dto.request.AddDocsPageRequest;
import com.example.bssm_dev.domain.docs.service.command.DocsPageCommandService;
import com.example.bssm_dev.domain.user.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}

