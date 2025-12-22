package com.example.bssm_dev.domain.docs.controller.query;

import com.example.bssm_dev.common.dto.ResponseDto;
import com.example.bssm_dev.common.util.HttpUtil;
import com.example.bssm_dev.domain.docs.dto.response.DocsSideBarResponse;
import com.example.bssm_dev.domain.docs.service.query.DocsSideBarQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/docs/{docsId}/sidebar")
public class DocsSideBarQueryController {
    private final DocsSideBarQueryService docsSIdeBarQueryService;


    /**
     * SideBar 조회 by docsId
     */
    @GetMapping
    public ResponseEntity<ResponseDto<DocsSideBarResponse>> getSideBar(
            @PathVariable("docsId") String docsId
    ) {
        DocsSideBarResponse docsSideBarResponse = docsSIdeBarQueryService.getSideBar(docsId);
        ResponseDto<DocsSideBarResponse> responseDto = HttpUtil.success("successfully find sidebar", docsSideBarResponse);
        return ResponseEntity.ok(responseDto);
    }
}
