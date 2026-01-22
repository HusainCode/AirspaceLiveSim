package com.flighttracker.api.controller;

import com.flighttracker.api.domain.Flight;
import com.flighttracker.api.service.FlightService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * REST endpoint for individual flight data operations.
 * Handles requests for specific flight details, status, and metadata.
 */
@RestController
@RequestMapping("api/flights")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Flight> getFlightById(@PathVariable String id) {
        return flightService.getFlightById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<Map<String, String>> getFlightByStatus(@PathVariable String id) {
        String status = flightService.getFlightByStatus(id);
        if ("UNKNOWN".equals(status)) {
            return flightService.getFlightById(id)
                    .map(f -> ResponseEntity.ok(Map.of("flightId", id, "status", status)))
                    .orElse(ResponseEntity.notFound().build());
        }
        return ResponseEntity.ok(Map.of("flightId", id, "status", status));
    }

    @GetMapping("/{id}/metadata")
    public ResponseEntity<Map<String, Object>> getFlightMetadata(@PathVariable String id) {
        Map<String, Object> metadata = flightService.getFlightMetadata(id);
        if (metadata.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(metadata);
    }
}
