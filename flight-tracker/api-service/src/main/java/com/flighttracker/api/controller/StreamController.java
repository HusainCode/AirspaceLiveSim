package com.flighttracker.api.controller;

import com.flighttracker.api.domain.Flight;
import com.flighttracker.api.service.FlightService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.MediaType;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

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

    private final FlightService flightService;

    public StreamController(FlightService flightService) {
        this.flightService = flightService;
    }

    /**
     * Streams all flight positions globally at 5-second intervals.
     * OpenSky API has rate limits, so we use a longer interval.
     */
    @GetMapping(value = "/stream/flights", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<List<Flight>> streamFlights() {
        return flightService.streamFlightUpdates(Duration.ofSeconds(5));
    }

    /**
     * Streams flight positions within a geographic bounding box.
     * Useful for tracking flights in a specific region.
     */
    @GetMapping(value = "/stream/flights/area", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<List<Flight>> streamFlightsInArea(
            @RequestParam double minLat,
            @RequestParam double maxLat,
            @RequestParam double minLon,
            @RequestParam double maxLon) {
        return flightService.streamFlightsInArea(
                Duration.ofSeconds(5), minLat, maxLat, minLon, maxLon);
    }
}