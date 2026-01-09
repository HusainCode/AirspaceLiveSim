package com.flighttracker.common.cache;

import com.flighttracker.common.model.Flight;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * In-memory cache implementation using Caffeine.
 * Provides fast local caching with automatic eviction.
 */
@Profile("dev")
@Component("inMemoryFlightCache")
public class InMemoryFlightCache implements FlightCache {

    private static final Logger log = LoggerFactory.getLogger(InMemoryFlightCache.class);

    private final Cache<String, Flight> cache;

    public InMemoryFlightCache(
            @Value("${flight.cache.local.max-size:10000}") int maxSize,
            @Value("${flight.cache.local.ttl-minutes:10}") int ttlMinutes) {

        this.cache = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(Duration.ofMinutes(ttlMinutes))
                .recordStats() // Enable metrics for monitoring
                .evictionListener((key, value, cause) -> {
                    log.debug("Evicted flight {} from cache. Cause: {}", key, cause);
                })
                .build();

        log.info("InMemoryFlightCache initialized with maxSize={}, ttl={}min", maxSize, ttlMinutes);
    }

    @Override
    public Mono<Flight> get(String flightNumber) {
        if (flightNumber == null) {
            return Mono.empty();
        }
        return Mono.fromSupplier(() -> cache.getIfPresent(flightNumber));
    }

    @Override
    public Mono<Void> put(String flightNumber, Flight flight) {
        if (flightNumber == null || flight == null) {
            return Mono.empty();
        }
        return Mono.fromRunnable(() -> cache.put(flightNumber, flight));
    }

    @Override
    public Mono<Boolean> delete(String flightNumber) {
        if (flightNumber == null) {
            return Mono.just(false);
        }
        return Mono.fromSupplier(() -> {
            boolean existed = cache.getIfPresent(flightNumber) != null;
            cache.invalidate(flightNumber);
            return existed;
        });
    }

    @Override
    public Mono<Boolean> exists(String flightNumber) {
        if (flightNumber == null) {
            return Mono.just(false);
        }
        return Mono.fromSupplier(() -> cache.getIfPresent(flightNumber) != null);
    }

    @Override
    public Mono<Void> clear() {
        return Mono.fromRunnable(() -> {
            long sizeBefore = cache.estimatedSize();
            cache.invalidateAll();
            log.warn("Cache cleared. Removed approximately {} entries", sizeBefore);
        });
    }

    /**
     * Get cache statistics for monitoring.
     * Useful for observability and debugging.
     */
    public CacheStats getStats() {
        var stats = cache.stats();
        return new CacheStats(
                cache.estimatedSize(),
                stats.hitRate(),
                stats.missRate(),
                stats.evictionCount()
        );
    }

    /**
     * Value object for cache statistics.
     */
    public record CacheStats(
            long size,
            double hitRate,
            double missRate,
            long evictionCount
    ) {}
}
