package com.flighttracker.common.cache;

import com.flighttracker.common.model.Flight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * Redis-backed cache implementation for distributed caching.
 * Provides shared cache across multiple service instances.
 * Gracefully degrades on Redis failures (returns empty instead of crashing).
 */
@Profile("prod")
@Component("redisFlightCache")
public class RedisFlightCache implements FlightCache {

    private static final Logger log = LoggerFactory.getLogger(RedisFlightCache.class);
    private static final String KEY_PREFIX = "flight:";

    private final ReactiveValueOperations<String, Flight> valueOps;
    private final ReactiveRedisTemplate<String, Flight> redisTemplate;
    private final Duration ttl;
    private final Duration timeout;

    public RedisFlightCache(
            ReactiveRedisTemplate<String, Flight> redisTemplate,
            @Value("${flight.cache.redis.ttl-minutes:30}") int ttlMinutes,
            @Value("${flight.cache.redis.timeout-seconds:2}") int timeoutSeconds) {

        this.redisTemplate = redisTemplate;
        this.valueOps = redisTemplate.opsForValue();
        this.ttl = Duration.ofMinutes(ttlMinutes);
        this.timeout = Duration.ofSeconds(timeoutSeconds);

        log.info("RedisFlightCache initialized with ttl={}min, timeout={}s", ttlMinutes, timeoutSeconds);
    }

    @Override
    public Mono<Flight> get(String flightNumber) {
        if (flightNumber == null) {
            return Mono.empty();
        }

        String key = buildKey(flightNumber);
        return valueOps.get(key)
                .timeout(timeout)
                .doOnError(e -> log.warn("Redis GET failed for key {}: {}", key, e.getMessage()))
                .onErrorResume(this::isRedisError, e -> {
                    log.debug("Returning empty due to Redis error", e);
                    return Mono.empty(); // Graceful degradation
                });
    }

    @Override
    public Mono<Void> put(String flightNumber, Flight flight) {
        if (flightNumber == null || flight == null) {
            return Mono.empty();
        }

        String key = buildKey(flightNumber);
        return valueOps.set(key, flight, ttl)
                .timeout(timeout)
                .doOnSuccess(success -> {
                    if (Boolean.TRUE.equals(success)) {
                        log.debug("Cached flight {} in Redis with TTL {}min", flightNumber, ttl.toMinutes());
                    }
                })
                .doOnError(e -> log.error("Redis SET failed for key {}: {}", key, e.getMessage()))
                .onErrorResume(this::isRedisError, e -> {
                    log.warn("Failed to cache flight {} in Redis, continuing anyway", flightNumber);
                    return Mono.just(false); // Don't fail the request if Redis is down
                })
                .then();
    }

    @Override
    public Mono<Boolean> delete(String flightNumber) {
        if (flightNumber == null) {
            return Mono.just(false);
        }

        String key = buildKey(flightNumber);
        return redisTemplate.delete(key)
                .map(count -> count > 0)
                .timeout(timeout)
                .doOnSuccess(deleted -> {
                    if (Boolean.TRUE.equals(deleted)) {
                        log.debug("Deleted flight {} from Redis", flightNumber);
                    }
                })
                .doOnError(e -> log.warn("Redis DELETE failed for key {}: {}", key, e.getMessage()))
                .onErrorResume(this::isRedisError, e -> Mono.just(false));
    }

    @Override
    public Mono<Boolean> exists(String flightNumber) {
        if (flightNumber == null) {
            return Mono.just(false);
        }

        String key = buildKey(flightNumber);
        return redisTemplate.hasKey(key)
                .timeout(timeout)
                .doOnError(e -> log.warn("Redis EXISTS failed for key {}: {}", key, e.getMessage()))
                .onErrorResume(this::isRedisError, e -> Mono.just(false));
    }

    @Override
    public Mono<Void> clear() {
        return redisTemplate.keys(KEY_PREFIX + "*")
                .flatMap(redisTemplate::delete)
                .count()
                .timeout(Duration.ofSeconds(30)) // Longer timeout for bulk operation
                .doOnSuccess(count -> log.warn("Cleared {} flight entries from Redis", count))
                .doOnError(e -> log.error("Failed to clear Redis cache: {}", e.getMessage()))
                .then();
    }

    /**
     * Builds Redis key with namespace prefix.
     * Example: "flight:AA123"
     */
    private String buildKey(String flightNumber) {
        return KEY_PREFIX + flightNumber;
    }

    /**
     * Determines if an error is Redis-related (connection, timeout, etc.)
     */
    private boolean isRedisError(Throwable e) {
        return e instanceof RedisConnectionFailureException
                || e instanceof java.util.concurrent.TimeoutException
                || e.getCause() instanceof RedisConnectionFailureException;
    }
}
