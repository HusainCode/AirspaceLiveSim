package com.flighttracker.api;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.MediaType;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

/// https://opensky-network.org/
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
