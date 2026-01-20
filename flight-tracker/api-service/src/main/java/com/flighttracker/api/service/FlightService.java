package com.flighttracker.api.service;

import com.flighttracker.api.domain.Flight;
import com.flighttracker.api.integration.opensky.OpenSkyFlightProvider;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

/**
 * Orchestrates flight data operations.
 * Delegates to providers for external data retrieval.
 */
@Service
public class FlightService {

    private final OpenSkyFlightProvider openSkyProvider;

    public FlightService(OpenSkyFlightProvider openSkyProvider) {
        this.openSkyProvider = openSkyProvider;
    }

    /**
     * Returns a Flux that emits lists of current flight positions at regular intervals.
     * Each emission contains the latest snapshot of all tracked flights.
     */
    public Flux<List<Flight>> streamFlightUpdates(Duration interval) {
        return Flux.interval(interval)
                .flatMap(tick -> openSkyProvider.getAllFlights());
    }

    /**
     * Returns a Flux of flight updates within a specific geographic area.
     */
    public Flux<List<Flight>> streamFlightsInArea(Duration interval,
                                                   double minLat, double maxLat,
                                                   double minLon, double maxLon) {
        return Flux.interval(interval)
                .flatMap(tick -> openSkyProvider.getFlightsInArea(minLat, maxLat, minLon, maxLon));
    }
}
