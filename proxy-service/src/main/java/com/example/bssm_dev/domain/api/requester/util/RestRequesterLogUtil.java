package com.example.bssm_dev.domain.api.requester.util;

import com.example.bssm_dev.domain.api.exception.ExternalApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
public final class RestRequesterLogUtil {

    private RestRequesterLogUtil() {
    }

    public static void logSuccess(String method, String endpoint, ResponseEntity<byte[]> response) {
        log.info("========== [External API Response] ==========");
        log.info("Method: {}", method);
        log.info("Endpoint: {}", endpoint);
        log.info("Status: {}", response.getStatusCode().value());
        log.info("Response Body Length: {}", response.getBody() != null ? response.getBody().length : 0);
        log.info("=============================================");
    }

    public static void logRequest(String method, String endpoint, Object body, java.util.Map<String, String> headers) {
        log.info("========== [External API Request] ==========");
        log.info("Method: {}", method);
        log.info("Endpoint: {}", endpoint);
        log.info("Request Headers: {}", headers);
        if (body == null) {
            log.info("Request Body: (none)");
        } else {
            log.info("Request Body: {}", body);
        }
    }

    public static void logErrorResponse(String method, String endpoint, int status, String body) {
        log.error("========== [External API Error] ==========");
        log.error("Method: {}", method);
        log.error("Endpoint: {}", endpoint);
        log.error("Status: {}", status);
        log.error("Response Body: {}", body);
        log.error("==========================================");
    }

    public static void logError(String method, String endpoint, Exception e) {
        log.error("========== [External API Error] ==========");
        log.error("Method: {}", method);
        log.error("Endpoint: {}", endpoint);
        if (e instanceof WebClientResponseException webClientEx) {
            log.error("Status: {}", webClientEx.getStatusCode());
            log.error("Response Body: {}", webClientEx.getResponseBodyAsString());
        } else {
            log.error("Error: {}", e.getMessage(), e);
        }
        log.error("==========================================");
    }

    public static ExternalApiException wrapExternal(Exception e) {
        if (e instanceof WebClientResponseException webClientEx) {
            return ExternalApiException.raise(
                    webClientEx.getStatusCode().value(),
                    webClientEx.getResponseBodyAsString(),
                    webClientEx.getMessage()
            );
        }
        return ExternalApiException.raise(e.getMessage());
    }
}
