package com.example.bssm_dev.domain.docs.listener;

import com.example.bssm_dev.common.util.BeanUtil;
import com.example.bssm_dev.domain.docs.service.command.ApiDocumentCommandService;
import jakarta.persistence.PreRemove;

public class ApiPageListener {

    @PreRemove
    public void onPreRemove(ApiPage apiPage) {
        ApiDocumentCommandService apiDocumentCommandService =
                BeanUtil.getBean(ApiDocumentCommandService.class);

        Long apiId = apiPage.getApiID();
        apiDocumentCommandService.deleteByApiId(apiId);
    }
}
