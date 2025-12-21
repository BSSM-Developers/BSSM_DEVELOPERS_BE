package com.example.bssm_dev.domain.api.requester.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public final class ResponseEntitySanitizer {

    private ResponseEntitySanitizer() {
    }

    public static ResponseEntity<byte[]> sanitize(ResponseEntity<byte[]> entity) {
        HttpHeaders filtered = new HttpHeaders();
        entity.getHeaders().forEach((name, values) -> {
            boolean shouldForward = !(
                    HeaderFilterUtil.isHopByHopHeader(name) || HeaderFilterUtil.isExcludedResponseHeader(name)
            );
            if (shouldForward) {
                filtered.put(name, values);
            }
        });
        return new ResponseEntity<>(entity.getBody(), filtered, entity.getStatusCode());
    }
}
