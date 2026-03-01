package com.example.bssm_dev.proxy.queue;

import reactor.core.publisher.Mono;

public interface RequestQueue {
    Mono<Boolean> tryAcquire(String clientId, double priority);
    Mono<Void> release();
}
