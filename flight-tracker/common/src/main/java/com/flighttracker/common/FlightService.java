package com.flighttracker.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

// https://rapidapi.com/aedbx-aedbx/api/aerodatabox/playground/apiendpoint_97755564-247f-411f-bef2-ad26a453e389
// 1 GET /flights/airports/icao/{airport}/departures
// 2 GET /flights/airports/icao/{airport}/arrivals
// 3 GET /flights/{flight-number}/status

@Service
public class FlightService {

    // private static final Logger LOGGER = Logger.getLogger(null)
    private static final String API_KEY = System.getenv("API_KEY");
    private static final String API_HOST = "aerodatabox.p.rapidapi.com";

    public static String getFlightId(String flightNumber) throws Exception {
        if (API_KEY == null || API_KEY.isBlank()) {
            throw IllegalStateException("Missing required API_KEY environment variable"); // stopped here!
        }
    }
}
