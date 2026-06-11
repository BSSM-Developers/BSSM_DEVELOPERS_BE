package com.example.bssm_dev.domain.docs.service.command;

import com.example.bssm_dev.domain.docs.model.Docs;
import com.example.bssm_dev.domain.docs.model.type.ServerStatus;
import com.example.bssm_dev.domain.docs.repository.DocsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocsHealthCheckCommandService {

    private static final int CONCURRENCY = 10;
    private static final Duration TOTAL_TIMEOUT = Duration.ofMinutes(2);

    private final DocsRepository docsRepository;
    private final WebClient healthCheckWebClient;

    public void checkAll() {
        List<Docs> docsList = docsRepository.findAllActiveWithDomain();

        List<Docs> changed = Flux.fromIterable(docsList)
                .flatMap(docs -> checkAndUpdateIfChanged(docs), CONCURRENCY)
                .collectList()
                .block(TOTAL_TIMEOUT);

        if (changed != null && !changed.isEmpty()) {
            saveAll(changed);
        }
    }

    @Transactional("mongoTransactionManager")
    public void saveAll(List<Docs> docs) {
        docsRepository.saveAll(docs);
    }

    private Mono<Docs> checkAndUpdateIfChanged(Docs docs) {
        ServerStatus previousStatus = docs.getServerStatus();
        String baseUrl = normalizeBaseUrl(docs.getDomain());

        return determineStatus(baseUrl)
                .filter(newStatus -> newStatus != previousStatus)
                .map(newStatus -> {
                    docs.updateServerStatus(newStatus);
                    log.info("Health check status changed: docsId={}, domain={}, {} -> {}",
                            docs.getId(), docs.getDomain(), previousStatus, newStatus);
                    return docs;
                });
    }

    private Mono<ServerStatus> determineStatus(String baseUrl) {
        return isReachable(baseUrl + "/")
                .flatMap(rootOk -> {
                    if (rootOk) return Mono.just(ServerStatus.RUNNING);
                    return isReachable(baseUrl + "/health")
                            .map(healthOk -> healthOk ? ServerStatus.RUNNING : ServerStatus.STOP);
                });
    }

    private Mono<Boolean> isReachable(String url) {
        return healthCheckWebClient.get()
                .uri(url)
                .exchangeToMono(response -> {
                    boolean reachable = !response.statusCode().isError()
                            || response.statusCode().is4xxClientError();
                    return Mono.just(reachable);
                })
                .onErrorResume(e -> {
                    log.warn("Health check unreachable: url={}, cause={}", url, e.getMessage());
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
