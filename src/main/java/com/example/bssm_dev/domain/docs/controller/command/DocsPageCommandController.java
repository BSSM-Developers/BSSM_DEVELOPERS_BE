package com.example.bssm_dev.domain.docs.controller.command;

import com.example.bssm_dev.common.annotation.CurrentUser;
import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.domain.docs.service.command.DocsPageCommandService;
import com.example.bssm_dev.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/docs/{docsId}/page")
public class DocsPageCommandController {
    private final DocsPageCommandService docsPageCommandService;

    /**
     * Docs page 추가
     **/
    public ResponseEntity<ResponseDto<Void>> addDocsPage(
            @PathVariable("docsId") String docsId,
            @CurrentUser User currentUser
    ) {
        // TODO: Implement addDocsPage
        return null;
    }
}
