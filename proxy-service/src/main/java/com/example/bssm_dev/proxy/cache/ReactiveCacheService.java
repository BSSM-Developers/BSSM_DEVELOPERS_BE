package com.example.bssm_dev.proxy.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(CacheProperties.class)
public class ReactiveCacheService {
    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;
    private final ObjectMapper objectMapper;
    private final CacheProperties cacheProperties;

    public <T> Mono<T> getCacheOrFetch(String cacheKey, Supplier<Mono<T>> dbFetcher, Class<T> type) {
        return getCacheOrFetch(cacheKey, dbFetcher, type, cacheProperties.getTtl());
    }

    public <T> Mono<T> getCacheOrFetch(String cacheKey, Supplier<Mono<T>> dbFetcher, Class<T> type, Duration ttl) {
        return reactiveRedisTemplate.opsForValue().get(cacheKey)
                .flatMap(cached -> {
                    try {
                        T value = objectMapper.readValue(cached, type);
                        log.debug("Cache hit for key: {}", cacheKey);
                        return Mono.just(value);
                    } catch (Exception e) {
                        log.warn("Failed to deserialize cache for key: {}", cacheKey, e);
                        return Mono.empty();
                    }
                })
                .switchIfEmpty(
                        dbFetcher.get()
                                .flatMap(value -> {
                                    try {
                                        String json = objectMapper.writeValueAsString(value);
                                        return reactiveRedisTemplate.opsForValue()
                                                .set(cacheKey, json, ttl)
                                                .doOnSuccess(v -> log.debug("Cache set for key: {}", cacheKey))
                                                .thenReturn(value);
                                    } catch (Exception e) {
                                        log.warn("Failed to serialize value for cache key: {}", cacheKey, e);
                                        return Mono.just(value);
                                    }
                                })
                );
    }

    public <T> Flux<T> getCacheOrFetchList(String cacheKey, Supplier<Flux<T>> dbFetcher, Class<T> elementType) {
        return getCacheOrFetchList(cacheKey, dbFetcher, elementType, cacheProperties.getTtl());
    }

    public <T> Flux<T> getCacheOrFetchList(String cacheKey, Supplier<Flux<T>> dbFetcher, Class<T> elementType, Duration ttl) {
        return reactiveRedisTemplate.opsForValue().get(cacheKey)
                .flatMapMany(cached -> {
                    try {
                        List<T> values = objectMapper.readValue(cached, 
                                objectMapper.getTypeFactory().constructCollectionType(List.class, elementType));
                        log.debug("Cache hit for list key: {}", cacheKey);
                        return Flux.fromIterable(values);
                    } catch (Exception e) {
                        log.warn("Failed to deserialize cache for list key: {}", cacheKey, e);
                        return Flux.empty();
                    }
                })
                .switchIfEmpty(
                        dbFetcher.get()
                                .collectList()
                                .flatMapMany(values -> {
                                    try {
                                        String json = objectMapper.writeValueAsString(values);
                                        return reactiveRedisTemplate.opsForValue()
                                                .set(cacheKey, json, ttl)
                                                .doOnSuccess(v -> log.debug("Cache set for list key: {}", cacheKey))
                                                .thenMany(Flux.fromIterable(values));
                                    } catch (Exception e) {
                                        log.warn("Failed to serialize list for cache key: {}", cacheKey, e);
                                        return Flux.fromIterable(values);
                                    }
                                })
                );
    }

    public Mono<Boolean> evict(String cacheKey) {
        return reactiveRedisTemplate.delete(cacheKey)
                .map(count -> count > 0)
                .doOnSuccess(deleted -> {
                    if (deleted) {
                        log.debug("Cache evicted for key: {}", cacheKey);
                    }
                });
    }

    public Mono<Long> evictByPattern(String pattern) {
        return reactiveRedisTemplate.keys(pattern)
                .collectList()
                .flatMap(keys -> {
                    if (keys.isEmpty()) {
                        return Mono.just(0L);
                    }
                    return reactiveRedisTemplate.delete(keys.toArray(new String[0]))
                            .doOnSuccess(count -> log.debug("Evicted {} keys matching pattern: {}", count, pattern));
                });
    }
}
