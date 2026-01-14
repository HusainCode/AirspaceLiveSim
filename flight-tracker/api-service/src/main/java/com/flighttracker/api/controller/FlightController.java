package com.flighttracker.api.controller;

import com.flighttracker.api.model.Flight;
import com.flighttracker.api.service.FlightService;

import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public Optional<Flight> getFlightById(@PathVariable String id) {
        return flightService.getFlightById(id);
    }

    @GetMapping("/{id}/status")
    public String getFlightByStatus(@PathVariable String id) {
        return flightService.getFlightByStatus(id);
    }

    @GetMapping("/{id}/metadata")
    public Object getFlightMetadata(@PathVariable String id) {
        return flightService.getFlightMetadata(id);
    }

}
