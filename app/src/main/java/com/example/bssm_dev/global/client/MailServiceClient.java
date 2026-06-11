package com.example.bssm_dev.global.client;

import com.example.bssm_dev.global.config.properties.MailServiceProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@Slf4j
public class MailServiceClient {

    private final RestClient restClient;

    public MailServiceClient(MailServiceProperties properties) {
        this.restClient = RestClient.builder()
                .baseUrl(properties.getUrl())
                .build();
    }

    public void send(String to, String subject, String body) {
        try {
            String requestBody = """
                    {"to":"%s","subject":"%s","body":"%s"}
                    """.formatted(escape(to), escape(subject), escape(body));

            restClient.post()
                    .uri("/mail/send")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            log.error("메일 발송 실패: to={}, subject={}", to, subject, e);
        }
    }

    private String escape(String value) {
        return value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "");
    }
}
