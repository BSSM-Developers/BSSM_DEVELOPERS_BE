package com.example.bssm_dev.proxy.queue;

import reactor.core.publisher.Mono;

public interface RequestQueue {
    Mono<Boolean> tryAcquire();
    Mono<Void> release();
}
