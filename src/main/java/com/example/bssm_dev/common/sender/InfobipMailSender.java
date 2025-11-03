package com.example.bssm_dev.common.sender;

import com.example.bssm_dev.global.config.properties.InfobipProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class InfobipMailSender implements MailSender {
    private InfobipProperties infobipProperties;

    private final RestClient restClient = RestClient.builder()
            .baseUrl(infobipProperties.getUrl())
            .defaultHeader(HttpHeaders.AUTHORIZATION, "App " + infobipProperties.getApiKey())
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .build();

    @Override
    public void send(
            String toEmail,
            String title,
            String content
    ) {
        String requestBody = buildRequestBody(toEmail, title, content);

        String response = restClient.post()
                    .uri("/email/4/messages")
                    .body(requestBody)
                    .retrieve()
                    .body(String.class);

        System.out.println("Response: " + response);
    }

    private static String buildRequestBody(String toEmail, String title, String content) {
        String requestBody = """
        {
          "messages": [
            {
              "destinations": [
                {
                  "to": [
                    { "destination": "%s" }
                  ]
                }
              ],
              "sender": "comodo@selfserve.worlds-connected.co",
              "content": {
                "subject": "%s",
                "text": "%s"
              }
            }
          ]
        }
        """.formatted(toEmail, title, content);
        return requestBody;
    }
}
