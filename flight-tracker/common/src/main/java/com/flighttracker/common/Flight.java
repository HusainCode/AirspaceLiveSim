package com.flighttracker.common;


// https://rapidapi.com/aedbx-aedbx/api/aerodatabox/playground/apiendpoint_97755564-247f-411f-bef2-ad26a453e389
// 1 GET /flights/airports/icao/{airport}/departures
// 2 GET /flights/airports/icao/{airport}/arrivals
// 3 GET /flights/{flight-number}/status

public class Flight {
    private String flightId;              // unique ID (usually icao24 or AeroDataBox id)
    private String flightNumber;          
    private String callsign;

    // ICAO = 4-letter international airport code (ex: KJFK, KLAX, EGLL)
    // Used by all flight APIs and aviation systems
    private String departureAirportIcao;
    private String destinationAirportIcao;

    private String departureAirportName;
    private String destinationAirportName;

    private String scheduledDepartureTime;
    private String actualDepartureTime;

    private String scheduledArrivalTime;
    private String estimatedArrivalTime;

    private double latitude;
    private double longitude;
    private double altitude;
    private double speed;

    private FlightStatus status;
    private long lastUpdated;

}
