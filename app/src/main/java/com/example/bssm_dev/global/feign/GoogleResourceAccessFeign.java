package com.example.bssm_dev.global.feign;

import com.example.bssm_dev.domain.auth.dto.response.GoogleUserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="google-resource-server-approve", url = "https://www.googleapis.com/oauth2/v2")
public interface GoogleResourceAccessFeign {

    @GetMapping("/userinfo")
    GoogleUserResponse accessGoogle(@RequestHeader("Authorization") String code);



}
