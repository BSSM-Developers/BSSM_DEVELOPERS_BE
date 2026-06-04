package com.example.bssm_dev.domain.docs.controller.command;

import com.example.bssm_dev.common.annotation.CurrentUser;
import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.common.util.HttpUtil;
import com.example.bssm_dev.domain.docs.dto.request.UpdateDocsPageRequest;
import com.example.bssm_dev.domain.docs.service.command.DocsPageCommandService;
import com.example.bssm_dev.domain.user.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/docs/{docsId}/page/{mappedId}")
public class DocsPageCommandController {
    private final DocsPageCommandService docsPageCommandService;

    /**
     * DocsPage의 docsBlocks 수정
     */
    @PutMapping
    public ResponseEntity<ResponseDto<Void>> updateDocsPage(
            @PathVariable String docsId,
            @PathVariable String mappedId,
            @Valid @RequestBody UpdateDocsPageRequest request,
            @CurrentUser User user
    ) {
        docsPageCommandService.update(docsId, mappedId, request, user);
        ResponseDto<Void> responseDto = HttpUtil.success("Successfully updated docs page");
        return ResponseEntity.ok(responseDto);
    }
}
