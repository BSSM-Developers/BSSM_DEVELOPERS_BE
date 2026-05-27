package com.example.bssm_dev.global.feign;

import com.example.bssm_dev.domain.github.dto.request.EndpointParseRequest;
import com.example.bssm_dev.domain.github.dto.response.EndpointParseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "endpointParser", url = "${endpoint-parser.url}")
public interface EndpointParserFeign {

    @PostMapping("/parse")
    EndpointParseResponse parse(@RequestBody EndpointParseRequest request);
}
