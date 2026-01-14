package com.flighttracker.api.controller;

import com.flighttracker.api.domain.Flight;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST endpoint for querying and searching flights by criteria.
 * Provides search capabilities across flight numbers, routes, airlines, etc.
 */
@RestController
@RequestMapping("/api/search")
public class SearchController {

    @GetMapping("/flights")
    public List<Flight> searchFlights(@RequestParam String query) {
        return List.of();
    }
}