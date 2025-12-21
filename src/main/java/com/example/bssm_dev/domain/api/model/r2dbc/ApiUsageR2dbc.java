package com.example.bssm_dev.domain.api.model.r2dbc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("api_usage")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiUsageR2dbc {
    @Column("api_token_id")
    private Long apiTokenId;

    @Column("api_id")
    private String apiId;

    @Column("api_use_reason_id")
    private String apiUseReasonId;

    @Column("name")
    private String name;

    @Column("endpoint")
    private String endpoint;

    // API 도메인을 가져오기 위해서는 Api 테이블 조인이 필요
    // 이 필드는 조인 결과를 받을 용도
    @Column("domain")
    private String domain;

    @Column("method")
    private String method;

    public String getDomain() {
        return domain;
    }

    public String getMethod() {
        return method;
    }
}
