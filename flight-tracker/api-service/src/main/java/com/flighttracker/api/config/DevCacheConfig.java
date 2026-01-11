package com.flighttracker.api.config;

import com.flighttracker.common.cache.FlightCache;
import com.flighttracker.common.cache.InMemoryFlightCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Development cache configuration.
 * Uses only in-memory caching (Caffeine) for both L1 and L2.
 * Suitable for local development and testing without Redis dependency.
 */
@Configuration
@Profile("dev")
public class DevCacheConfig {

    /**
     * Creates the local cache bean (L1).
     * In dev profile, this is backed by Caffeine.
     */
    @Bean("inMemoryFlightCache")
    public FlightCache inMemoryFlightCache(
            @Value("${flight.cache.local.max-size:10000}") int maxSize,
            @Value("${flight.cache.local.ttl-minutes:10}") int ttlMinutes) {
        return new InMemoryFlightCache(maxSize, ttlMinutes);
    }

    /**
     * Creates the remote cache bean (L2).
     * In dev profile, we use another in-memory cache instead of Redis.
     * This allows the two-tier caching architecture to work without Redis.
     */
    @Bean("redisFlightCache")
    public FlightCache redisFlightCache(
            @Value("${flight.cache.local.max-size:10000}") int maxSize,
            @Value("${flight.cache.local.ttl-minutes:10}") int ttlMinutes) {
        return new InMemoryFlightCache(maxSize, ttlMinutes);
    }
}
