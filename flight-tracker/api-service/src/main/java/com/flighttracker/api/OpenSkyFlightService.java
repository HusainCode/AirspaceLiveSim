package com.flighttracker.api;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.List;
import java.util.Map;

// API calls live here

@Service
public class OpenSkyFlightService {

    private final WebClient webClient;

    public OpenSkyFlightService(OpenSkyAuthService authService) {
        this.webClient = authService.webClient();

    }
    
}
