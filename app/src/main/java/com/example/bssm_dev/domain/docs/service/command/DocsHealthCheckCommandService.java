package com.example.bssm_dev.domain.docs.service.command;

import com.example.bssm_dev.domain.docs.model.Docs;
import com.example.bssm_dev.domain.docs.model.type.ServerStatus;
import com.example.bssm_dev.domain.docs.repository.DocsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocsHealthCheckCommandService {

    private static final int CONCURRENCY = 10;

    private final DocsRepository docsRepository;
    private final WebClient healthCheckWebClient;

    public void checkAll() {
        List<Docs> docsList = docsRepository.findAllActiveWithDomain();

        List<Docs> updated = Flux.fromIterable(docsList)
                .flatMap(docs -> checkDocs(docs), CONCURRENCY)
                .collectList()
                .block();

        if (updated != null) {
            docsRepository.saveAll(updated);
        }
    }

    private Mono<Docs> checkDocs(Docs docs) {
        String baseUrl = normalizeBaseUrl(docs.getDomain());
        return isHealthy(baseUrl + "/")
                .flatMap(rootOk -> {
                    if (rootOk) return Mono.just(ServerStatus.RUNNING);
                    return isHealthy(baseUrl + "/health")
                            .map(healthOk -> healthOk ? ServerStatus.RUNNING : ServerStatus.STOP);
                })
                .map(status -> {
                    docs.updateServerStatus(status);
                    log.info("Health check: docsId={}, domain={}, status={}", docs.getId(), docs.getDomain(), status);
                    return docs;
                });
    }

    private Mono<Boolean> isHealthy(String url) {
        return healthCheckWebClient.get()
                .uri(url)
                .retrieve()
                .toBodilessEntity()
                .map(response -> response.getStatusCode().is2xxSuccessful())
                .onErrorResume(e -> {
                    log.warn("Health check failed for url={}: {}", url, e.getMessage());
                    return Mono.just(false);
                });
    }

    private String normalizeBaseUrl(String domain) {
        String trimmed = domain.endsWith("/") ? domain.substring(0, domain.length() - 1) : domain;
        if (trimmed.startsWith("http://") || trimmed.startsWith("https://")) {
            return trimmed;
        }
        return "https://" + trimmed;
    }
}
