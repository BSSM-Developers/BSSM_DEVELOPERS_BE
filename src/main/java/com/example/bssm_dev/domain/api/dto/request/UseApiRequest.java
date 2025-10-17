package com.example.bssm_dev.domain.api.dto.request;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.PathVariable;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UseApiRequest (
        @NotNull
        @NotBlank
        String secretKey
) {
}
