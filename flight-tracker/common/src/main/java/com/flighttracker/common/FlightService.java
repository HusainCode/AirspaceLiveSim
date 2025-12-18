package com.flighttracker.common;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.logging.Logger;

import javax.print.DocFlavor.STRING;

import org.json.JSONObject;


// https://rapidapi.com/aedbx-aedbx/api/aerodatabox/playground/apiendpoint_97755564-247f-411f-bef2-ad26a453e389
// 1 GET /flights/airports/icao/{airport}/departures
// 2 GET /flights/airports/icao/{airport}/arrivals
// 3 GET /flights/{flight-number}/status

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
