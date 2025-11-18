package com.example.bssm_dev.domain.docs.controller.command;

import com.example.bssm_dev.common.annotation.CurrentUser;
import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.common.util.HttpUtil;
import com.example.bssm_dev.domain.docs.dto.request.UpdateDocsSideBarRequest;
import com.example.bssm_dev.domain.docs.service.command.DocsSideBarCommandService;
import com.example.bssm_dev.domain.user.model.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/docs/{docsId}/sidebar")
public class DocsSideBarCommandController {
    private final DocsSideBarCommandService docsSideBarCommandService;

    /**
     * SideBar의 sideBarBlocks 수정
     */
    @PutMapping
    public ResponseEntity<ResponseDto<Void>> updateSideBar(
            @PathVariable String docsId,
            @Valid @RequestBody UpdateDocsSideBarRequest request,
            @CurrentUser User user
    ) {
        docsSideBarCommandService.update(docsId, request, user);
        ResponseDto<Void> responseDto = HttpUtil.success("Successfully updated sidebar");
        return ResponseEntity.ok(responseDto);
    }
}
