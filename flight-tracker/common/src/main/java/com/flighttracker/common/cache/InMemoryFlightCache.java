package com.flighttracker.common;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component("InMemoryFlightCache")
public class InMemoryFlightCache implements FlightCache {

    private final Cache<String, Flight> cache = Caffeine.newBuilder()
            .maximumSize(10_000)
            .expireAfterWrite(Duration.ofMinutes(10))
            .build();

    @Override
    public Mono<Flight> get(String flightNumber){
        return Mono.fromSupplier(() -> cache.getIfPresent(flightNumber));
    }

    @Override
    public Mono<Void> put(String flightNumber, Flight flight) {
        return Mono.fromRunnable(() -> cach.put(flightNumber, flight));
    }

}
