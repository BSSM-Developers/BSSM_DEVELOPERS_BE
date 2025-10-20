package com.example.bssm_dev.domain.docs.controller.command;
import com.example.bssm_dev.common.annotation.CurrentUser;
import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.common.util.HttpUtil;
import com.example.bssm_dev.domain.docs.dto.request.AddDocsSectionRequest;
import com.example.bssm_dev.domain.docs.service.command.DocsSectionCommandService;
import com.example.bssm_dev.domain.user.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @PathVariable("docsId") String docsId,
            @Valid @RequestBody AddDocsSectionRequest request,
            @CurrentUser User currentUser
    ) {
        docsSectionCommandService.addSection(docsId, request, currentUser);
        ResponseDto<Void> responseDto = HttpUtil.success("Successfully added docs section");
        return ResponseEntity.ok(responseDto);
    }
}
