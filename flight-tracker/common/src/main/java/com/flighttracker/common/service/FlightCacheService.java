package com.flighttracker.common.service;

import com.flighttracker.common.cache.FlightCache;
import com.flighttracker.common.model.Flight;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class FlightCacheService {

    private final FlightCache localCache; // Caffeine
    private final FlightCache remoteCache; // Redis

    public FlightCacheService(
            @Qualifier("inMemoryFlightCache") FlightCache localCache,
            @Qualifier("redisFlightCache") FlightCache remoteCache) {
        this.localCache = localCache;
        this.remoteCache = remoteCache;
    }

    public Mono<Flight> get(String flightNumber) {
        return localCache.get(flightNumber)
                .switchIfEmpty(
                        remoteCache.get(flightNumber)
                                .flatMap(flight -> localCache.put(flightNumber, flight)
                                        .thenReturn(flight)));
    }

    public Mono<Void> put(String flightNumber, Flight flight) {
        return Mono.when(
                localCache.put(flightNumber, flight),
                remoteCache.put(flightNumber, flight));
    }
}
