package com.example.bssm_dev.domain.api.event.listner;

import com.example.bssm_dev.domain.api.event.ProxyLogEvent;
import com.example.bssm_dev.domain.api.log.repository.ProxyLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProxyLogEventListener {

    private final ProxyLogRepository proxyLogRepository;

    @EventListener
    public void save(ProxyLogEvent event) {
        proxyLogRepository.save(event.log())
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(
                        null,
                        error -> log.error("Failed to persist proxy log: {}", error.getMessage(), error)
                );
    }
}
