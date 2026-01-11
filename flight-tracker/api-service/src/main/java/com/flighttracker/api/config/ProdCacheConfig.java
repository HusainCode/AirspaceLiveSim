package com.flighttracker.api.config;

import com.flighttracker.common.cache.FlightCache;
import com.flighttracker.common.cache.InMemoryFlightCache;
import com.flighttracker.common.cache.RedisFlightCache;
import com.flighttracker.common.model.Flight;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Production cache configuration.
 * Uses in-memory cache (Caffeine) for L1 and Redis for L2.
 * Requires Redis connection to be available.
 */
@Configuration
@Profile("prod")
public class ProdCacheConfig {

    /**
     * Creates the local cache bean (L1).
     * In prod profile, this is backed by Caffeine for fast local access.
     */
    @Bean("inMemoryFlightCache")
    public FlightCache inMemoryFlightCache(
            @Value("${flight.cache.local.max-size:10000}") int maxSize,
            @Value("${flight.cache.local.ttl-minutes:10}") int ttlMinutes) {
        return new InMemoryFlightCache(maxSize, ttlMinutes);
    }

    /**
     * Creates the remote cache bean (L2).
     * In prod profile, this is backed by Redis for distributed caching.
     */
    @Bean("redisFlightCache")
    public FlightCache redisFlightCache(
            ReactiveRedisTemplate<String, Flight> redisTemplate,
            @Value("${flight.cache.redis.ttl-minutes:30}") int ttlMinutes,
            @Value("${flight.cache.redis.timeout-seconds:2}") int timeoutSeconds) {
        return new RedisFlightCache(redisTemplate, ttlMinutes, timeoutSeconds);
    }

    /**
     * Configures ReactiveRedisTemplate for Flight objects.
     * Uses JSON serialization for Flight values and String for keys.
     */
    @Bean
    public ReactiveRedisTemplate<String, Flight> reactiveRedisTemplate(
            ReactiveRedisConnectionFactory connectionFactory) {

        StringRedisSerializer keySerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<Flight> valueSerializer =
                new Jackson2JsonRedisSerializer<>(Flight.class);

        RedisSerializationContext<String, Flight> serializationContext =
                RedisSerializationContext.<String, Flight>newSerializationContext()
                        .key(keySerializer)
                        .value(valueSerializer)
                        .hashKey(keySerializer)
                        .hashValue(valueSerializer)
                        .build();

        return new ReactiveRedisTemplate<>(connectionFactory, serializationContext);
    }
}
