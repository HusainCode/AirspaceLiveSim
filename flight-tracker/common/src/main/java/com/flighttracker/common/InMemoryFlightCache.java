package com.flighttracker.common;

import reactor.core.publisher.Mono;

public interface FlightCache {

    Mono<Flight> get(String flightNumber);

    Mono<Void> put(String flightNumber, Flight flight);
}
