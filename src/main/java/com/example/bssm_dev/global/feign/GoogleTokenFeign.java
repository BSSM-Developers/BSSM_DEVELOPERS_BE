package com.example.bssm_dev.global.feign;

import com.example.bssm_dev.domain.auth.dto.response.GoogleTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "googleToken",
        url = "https://oauth2.googleapis.com"
)
public interface GoogleTokenFeign {

    @PostMapping(value = "/token", consumes = "application/x-www-form-urlencoded")
    GoogleTokenResponse getToken(@RequestBody String body);
}
