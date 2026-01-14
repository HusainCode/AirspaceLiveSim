package com.flighttracker.api.integration.opensky;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Provider for fetching flight data from OpenSky Network API.
 * Handles all OpenSky HTTP calls, authentication, and data transformation.
 */
@Component
public class OpenSkyFlightProvider {

    private final OpenSkyAuthClient authClient;
    private final WebClient webClient;

    public OpenSkyFlightProvider(OpenSkyAuthClient authClient, WebClient.Builder webClientBuilder) {
        this.authClient = authClient;
        this.webClient = webClientBuilder
                .baseUrl("https://opensky-network.org/api")
                .build();
    }

}