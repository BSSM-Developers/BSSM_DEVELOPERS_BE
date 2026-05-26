package com.example.bssm_dev.domain.github.controller;

import com.example.bssm_dev.domain.github.service.GitHubWebhookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook/github")
@RequiredArgsConstructor
public class GitHubWebhookController {

    private final GitHubWebhookService gitHubWebhookService;

    @PostMapping
    public ResponseEntity<Void> receive(
            @RequestHeader("X-GitHub-Event") String event,
            @RequestHeader("X-Hub-Signature-256") String signature,
            @RequestBody String payload
    ) {
        gitHubWebhookService.handle(event, signature, payload);
        return ResponseEntity.ok().build();
    }
}
