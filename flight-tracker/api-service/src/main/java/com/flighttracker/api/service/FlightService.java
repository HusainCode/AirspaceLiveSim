package com.flighttracker.api.service;

import com.flighttracker.api.integration.opensky.OpenSkyFlightProvider;
import org.springframework.stereotype.Service;

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

}
