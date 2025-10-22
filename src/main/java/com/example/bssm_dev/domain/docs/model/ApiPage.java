package com.example.bssm_dev.domain.docs.model;

import com.example.bssm_dev.domain.api.model.Api;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@EntityListeners(com.example.bssm_dev.domain.docs.listener.ApiPageListener.class)
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class ApiPage {
    @Id
    private Long docsPageId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "docs_page_id")
    private DocsPage docsPage;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "api_id", nullable = false)
    private Api api;


    public static ApiPage of(DocsPage docsPage, Api api) {
        return ApiPage.builder()
                .docsPage(docsPage)
                .api(api)
                .build();
    }

    public Long getApiID() {
        return this.api.getApiId();
    }
}
