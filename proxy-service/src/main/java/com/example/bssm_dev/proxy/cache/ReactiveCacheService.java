package com.example.bssm_dev.proxy.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
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

    private Cache<String, Object> localCache;

    @PostConstruct
    void initLocalCache() {
        localCache = Caffeine.newBuilder()
                .maximumSize(cacheProperties.getLocalMaxSize())
                .expireAfterWrite(cacheProperties.getLocalTtl())
                .build();
    }

    public <T> Mono<T> getCacheOrFetch(String cacheKey, Supplier<Mono<T>> dbFetcher, Class<T> type) {
        return getCacheOrFetch(cacheKey, dbFetcher, type, cacheProperties.getTtl());
    }

    public <T> Mono<T> getCacheOrFetch(String cacheKey, Supplier<Mono<T>> dbFetcher, Class<T> type, Duration ttl) {
        Object local = localCache.getIfPresent(cacheKey);
        if (local != null) {
            log.debug("L1 cache hit: {}", cacheKey);
            return Mono.just(type.cast(local));
        }

        return reactiveRedisTemplate.opsForValue().get(cacheKey)
                .flatMap(cached -> {
                    try {
                        T value = objectMapper.readValue(cached, type);
                        log.debug("L2 cache hit: {}", cacheKey);
                        localCache.put(cacheKey, value);
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
                                                .doOnSuccess(v -> {
                                                    localCache.put(cacheKey, value);
                                                    log.debug("Cache set: {}", cacheKey);
                                                })
                                                .thenReturn(value);
                                    } catch (Exception e) {
                                        log.warn("Failed to serialize for cache key: {}", cacheKey, e);
                                        return Mono.just(value);
                                    }
                                })
                );
    }

    public <T> Flux<T> getCacheOrFetchList(String cacheKey, Supplier<Flux<T>> dbFetcher, Class<T> elementType) {
        return getCacheOrFetchList(cacheKey, dbFetcher, elementType, cacheProperties.getTtl());
    }

    public <T> Flux<T> getCacheOrFetchList(String cacheKey, Supplier<Flux<T>> dbFetcher, Class<T> elementType, Duration ttl) {
        Object local = localCache.getIfPresent(cacheKey);
        if (local != null) {
            log.debug("L1 cache hit (list): {}", cacheKey);
            @SuppressWarnings("unchecked")
            List<T> list = (List<T>) local;
            return Flux.fromIterable(list);
        }

        return reactiveRedisTemplate.opsForValue().get(cacheKey)
                .flatMapMany(cached -> {
                    try {
                        List<T> values = objectMapper.readValue(cached,
                                objectMapper.getTypeFactory().constructCollectionType(List.class, elementType));
                        log.debug("L2 cache hit (list): {}", cacheKey);
                        localCache.put(cacheKey, values);
                        return Flux.fromIterable(values);
                    } catch (Exception e) {
                        log.warn("Failed to deserialize list cache for key: {}", cacheKey, e);
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
                                                .doOnSuccess(v -> {
                                                    localCache.put(cacheKey, values);
                                                    log.debug("Cache set (list): {}", cacheKey);
                                                })
                                                .thenMany(Flux.fromIterable(values));
                                    } catch (Exception e) {
                                        log.warn("Failed to serialize list for cache key: {}", cacheKey, e);
                                        return Flux.fromIterable(values);
                                    }
                                })
                );
    }

    public Mono<Boolean> evict(String cacheKey) {
        localCache.invalidate(cacheKey);
        return reactiveRedisTemplate.delete(cacheKey)
                .map(count -> count > 0)
                .doOnSuccess(deleted -> {
                    if (deleted) {
                        log.debug("Cache evicted: {}", cacheKey);
                    }
                });
    }

    public Mono<Long> evictByPattern(String pattern) {
        String prefix = pattern.replace("*", "");
        localCache.asMap().keySet().stream()
                .filter(key -> key.startsWith(prefix))
                .forEach(localCache::invalidate);

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
