package com.flighttracker.api.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.MediaType;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

/**
 * Server-Sent Events (SSE) endpoint that maintains long-lived connections
 * to push real-time flight position updates to clients.
 *
 * Flow overview:
 * 1) User clicks once (button click or page load)
 * 2) Browser opens ONE connection to /stream/flights
 * 3) Server keeps the connection open using Server-Sent Events (SSE)
 * 4) Server continuously pushes flight position updates
 * 5) Frontend receives updates and moves the plane on the map until it lands
 *
 * This stream requires:
 * 1) A long-lived connection (SSE/WebFlux keeps the HTTP connection open)
 * 2) A data source that emits flight updates over time (timer or live API feed)
 * 3) A client that stays connected and listens for events (browser EventSource)
 *
 * As long as the client is connected, this method will keep pushing
 * updated flight positions until the stream is closed.
 */
@RestController
public class StreamController {

    @GetMapping(value = "/stream/flights", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Map<String, Object>> streamFlights() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(tick -> Map.of(
                        "flightId", "FAKE-" + tick,
                        "lat", 30.0 + tick * 0.001,
                        "lon", -97.0 + tick * 0.001,
                        "timestamp", Instant.now().toString()));
    }
}