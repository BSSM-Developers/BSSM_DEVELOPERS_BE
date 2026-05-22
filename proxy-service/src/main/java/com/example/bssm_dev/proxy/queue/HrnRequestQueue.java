package com.example.bssm_dev.proxy.queue;

import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * HRN(Highest Response Ratio Next) 기반 Request Queue 구현.
 * 사용자의 실패 횟수에 따른 priority와 대기 시간을 고려하여 스케줄링합니다.
 * 
 * Response Ratio = (대기시간 + 서비스시간) / 서비스시간
 * 여기서는 간단히: effectivePriority = basePriority + (waitTimeSeconds * 0.1)
 */
public class HrnRequestQueue implements RequestQueue {
    private final Semaphore semaphore;
    private final Duration timeout;
    private final PriorityBlockingQueue<Waiter> waiters;

    public HrnRequestQueue(int maxInflight, Duration timeout) {
        this.semaphore = new Semaphore(maxInflight);
        this.timeout = timeout;
        this.waiters = new PriorityBlockingQueue<>();
    }

    @Override
    public Mono<Boolean> tryAcquire(String clientId, double priority) {
        return Mono.defer(() -> {
            if (semaphore.tryAcquire()) {
                return Mono.just(true);
            }
            return Mono.<Boolean>create(sink -> {
                Waiter waiter = new Waiter(sink, clientId, priority);
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
        return Mono.<Void>fromRunnable(() -> {
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
        }).subscribeOn(Schedulers.boundedElastic());
    }

    private static final class Waiter implements Comparable<Waiter> {
        private final AtomicBoolean completed = new AtomicBoolean(false);
        private final MonoSink<Boolean> sink;
        private final String clientId;
        private final double basePriority;
        private final Instant enqueueTime;
        private Disposable timeoutTask;

        private Waiter(MonoSink<Boolean> sink, String clientId, double basePriority) {
            this.sink = sink;
            this.clientId = clientId;
            this.basePriority = basePriority;
            this.enqueueTime = Instant.now();
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

        /**
         * HRN 알고리즘에 따른 effective priority 계산.
         * 높은 priority와 오래 기다린 요청이 먼저 처리됩니다.
         */
        private double getEffectivePriority() {
            long waitTimeMillis = Duration.between(enqueueTime, Instant.now()).toMillis();
            double waitTimeSeconds = waitTimeMillis / 1000.0;
            // HRN: 대기시간이 길수록 priority 증가 (aging 효과)
            return basePriority + (waitTimeSeconds * 0.1);
        }

        @Override
        public int compareTo(Waiter other) {
            // 높은 effective priority가 먼저 (내림차순)
            return Double.compare(other.getEffectivePriority(), this.getEffectivePriority());
        }
    }
}
