package com.example.bssm_dev.proxy.queue;

import reactor.core.publisher.Mono;
import reactor.core.Disposable;
import reactor.core.publisher.MonoSink;

import java.time.Duration;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class SemaphoreRequestQueue implements RequestQueue {
    private final Semaphore semaphore;
    private final Duration timeout;
    private final ConcurrentLinkedQueue<Waiter> waiters = new ConcurrentLinkedQueue<>();

    public SemaphoreRequestQueue(int maxInflight, Duration timeout) {
        this.semaphore = new Semaphore(maxInflight);
        this.timeout = timeout;
    }

    @Override
    public Mono<Boolean> tryAcquire() {
        return Mono.defer(() -> {
            if (semaphore.tryAcquire()) {
                return Mono.just(true);
            }
            return Mono.<Boolean>create(sink -> {
                Waiter waiter = new Waiter(sink);
                waiters.offer(waiter);

                Disposable timeoutTask = Mono.delay(timeout)
                        .subscribe(ignored -> {
                            if (waiter.tryComplete(false)) {
                                waiters.remove(waiter);
                            }
                        });
                waiter.setTimeoutTask(timeoutTask);

                sink.onCancel(() -> {
                    if (waiter.tryComplete(false)) {
                        waiters.remove(waiter);
                    }
                });
            });
        });
    }

    @Override
    public Mono<Void> release() {
        return Mono.fromRunnable(() -> {
            while (true) {
                Waiter waiter = waiters.poll();
                if (waiter == null) {
                    semaphore.release();
                    return;
                }
                if (waiter.tryComplete(true)) {
                    return;
                }
            }
        });
    }

    private static final class Waiter {
        private final AtomicBoolean completed = new AtomicBoolean(false);
        private final MonoSink<Boolean> sink;
        private Disposable timeoutTask;

        private Waiter(MonoSink<Boolean> sink) {
            this.sink = sink;
        }

        private void setTimeoutTask(Disposable timeoutTask) {
            this.timeoutTask = timeoutTask;
        }

        private boolean tryComplete(boolean value) {
            if (!completed.compareAndSet(false, true)) {
                return false;
            }
            if (timeoutTask != null) {
                timeoutTask.dispose();
            }
            sink.success(value);
            return true;
        }
    }
}
