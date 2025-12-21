package com.flighttracker.common.service;

import com.flighttracker.common.cache.FlightCache;
import com.flighttracker.common.model.Flight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Two-tier caching service (L1: in-memory, L2: Redis).
 * Provides fast local access with distributed consistency.
 *
 * Cache strategy:
 * - Reads: Check local first, fallback to Redis, backfill local on hit
 * - Writes: Write to both caches in parallel (write-through)
 * - Deletes: Remove from both caches
 */
@Service
public class FlightCacheService {

    private static final Logger log = LoggerFactory.getLogger(FlightCacheService.class);

    private final FlightCache localCache; // L1: Caffeine (fast, local)
    private final FlightCache remoteCache; // L2: Redis (shared, persistent)

    public FlightCacheService(
            @Qualifier("inMemoryFlightCache") FlightCache localCache,
            @Qualifier("redisFlightCache") FlightCache remoteCache) {
        this.localCache = localCache;
        this.remoteCache = remoteCache;
        log.info("FlightCacheService initialized with two-tier caching (local + Redis)");
    }

    /**
     * Gets a flight from cache using two-tier strategy:
     * 1. Check local cache (L1) - fastest
     * 2. If miss, check Redis (L2)
     * 3. If Redis hit, backfill local cache for next request
     * 4. If both miss, return empty
     */
    public Mono<Flight> get(String flightNumber) {
        if (flightNumber == null) {
            return Mono.empty();
        }

        return localCache.get(flightNumber)
                .doOnNext(flight -> log.debug("L1 cache HIT for flight {}", flightNumber))
                .switchIfEmpty(
                        remoteCache.get(flightNumber)
                                .doOnNext(flight -> log.debug("L2 cache HIT for flight {} (backfilling L1)", flightNumber))
                                .flatMap(flight -> localCache.put(flightNumber, flight)
                                        .doOnError(e -> log.warn("Failed to backfill L1 cache for {}: {}", flightNumber, e.getMessage()))
                                        .onErrorResume(e -> Mono.empty()) // Don't fail if backfill fails
                                        .thenReturn(flight))
                                .switchIfEmpty(Mono.defer(() -> {
                                    log.debug("Cache MISS (L1 + L2) for flight {}", flightNumber);
                                    return Mono.empty();
                                }))
                );
    }

    /**
     * Writes flight to both cache tiers in parallel.
     * Uses write-through strategy for consistency.
     * Redis write failures are logged but don't fail the operation.
     */
    public Mono<Void> put(String flightNumber, Flight flight) {
        if (flightNumber == null || flight == null) {
            return Mono.empty();
        }

        return Mono.when(
                localCache.put(flightNumber, flight)
                        .doOnError(e -> log.error("L1 cache PUT failed for {}: {}", flightNumber, e.getMessage())),
                remoteCache.put(flightNumber, flight)
                        .doOnError(e -> log.warn("L2 cache PUT failed for {} (L1 still cached): {}", flightNumber, e.getMessage()))
        ).doOnSuccess(v -> log.debug("Cached flight {} in both L1 and L2", flightNumber));
    }

    /**
     * Deletes flight from both cache tiers.
     * Used when flight data becomes stale or invalid.
     * Returns true if deleted from at least one tier.
     */
    public Mono<Boolean> delete(String flightNumber) {
        if (flightNumber == null) {
            return Mono.just(false);
        }

        return Mono.zip(
                localCache.delete(flightNumber).onErrorReturn(false),
                remoteCache.delete(flightNumber).onErrorReturn(false)
        ).map(tuple -> {
            boolean deletedFromL1 = tuple.getT1();
            boolean deletedFromL2 = tuple.getT2();
            boolean anyDeleted = deletedFromL1 || deletedFromL2;

            if (anyDeleted) {
                log.debug("Deleted flight {} from cache (L1: {}, L2: {})", flightNumber, deletedFromL1, deletedFromL2);
            }

            return anyDeleted;
        });
    }

    /**
     * Checks if flight exists in either cache tier.
     * Checks local first for performance.
     */
    public Mono<Boolean> exists(String flightNumber) {
        if (flightNumber == null) {
            return Mono.just(false);
        }

        return localCache.exists(flightNumber)
                .flatMap(existsInLocal -> {
                    if (Boolean.TRUE.equals(existsInLocal)) {
                        return Mono.just(true); // Found in L1, no need to check L2
                    }
                    return remoteCache.exists(flightNumber).onErrorReturn(false);
                });
    }

    /**
     * Clears both cache tiers.
     * USE WITH CAUTION - impacts all service instances.
     */
    public Mono<Void> clear() {
        log.warn("Clearing ALL flight caches (L1 + L2)");
        return Mono.when(
                localCache.clear().onErrorResume(e -> {
                    log.error("Failed to clear L1 cache: {}", e.getMessage());
                    return Mono.empty();
                }),
                remoteCache.clear().onErrorResume(e -> {
                    log.error("Failed to clear L2 cache: {}", e.getMessage());
                    return Mono.empty();
                })
        );
    }
}
