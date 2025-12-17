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

    public FlightService(String flightId, String flightNumber, String callsign, String departureAirportIcao,
                  String destinationAirportIcao, String departureAirportName, String destinationAirportName,
                  String scheduledDepartureTime, String actualDepartureTime, String scheduledArrivalTime,
                  String estimatedArrivalTime, double latitude, double longitude, double altitude,
                  double speed, FlightStatus status, long lastUpdated) {
    }

    public static String getFlightId(String flightNumber) throws Exception {
        if (API_KEY == null || API_KEY.isBlank()) {
            throw IllegalStateException("Missing required API_KEY environment variable"); // stopped here! 
        }
    }

    public void setFlightId(String flightId) {
    }

    public String getFlightNumber() {
    }

    public void setFlightNumber(String flightNumber) {
    }

    public String getCallsign() {
    }

    public void setCallsign(String callsign) {
    }

    public String getDepartureAirportIcao() {
    }

    public void setDepartureAirportIcao(String departureAirportIcao) {
    }

    public String getDestinationAirportIcao() {
    }

    public void setDestinationAirportIcao(String destinationAirportIcao) {
    }

    public String getDepartureAirportName() {
    }

    public void setDepartureAirportName(String departureAirportName) {
    }

    public String getDestinationAirportName() {
    }

    public void setDestinationAirportName(String destinationAirportName) {
    }

    public String getScheduledDepartureTime() {
    }

    public void setScheduledDepartureTime(String scheduledDepartureTime) {
    }

    public String getActualDepartureTime() {
    }

    public void setActualDepartureTime(String actualDepartureTime) {
    }

    public String getScheduledArrivalTime() {
    }

    public void setScheduledArrivalTime(String scheduledArrivalTime) {
    }

    public String getEstimatedArrivalTime() {
    }

    public void setEstimatedArrivalTime(String estimatedArrivalTime) {
    }

    public double getLatitude() {
    }

    public void setLatitude(double latitude) {
    }

    public double getLongitude() {
    }

    public void setLongitude(double longitude) {
    }

    public double getAltitude() {
    }

    public void setAltitude(double altitude) {
    }

    public double getSpeed() {
    }

    public void setSpeed(double speed) {
    }

    public FlightStatus getStatus() {
    }

    public void setStatus(FlightStatus status) {
    }

    public long getLastUpdated() {
    }

    public void setLastUpdated(long lastUpdated) {
    }

    public boolean isInFlight() {
    }

    public boolean hasPosition() {
    }

    public boolean isDelayed() {
    }

    public long getFlightDuration() {
    }

    @Override
    public String toString() {
    }

    @Override
    public boolean equals(Object o) {
    }

    @Override
    public int hashCode() {
    }
}
