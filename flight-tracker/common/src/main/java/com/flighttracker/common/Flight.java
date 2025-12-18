package com.flighttracker.common;


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

    public Flight(String flightId, String flightNumber, String callsign, String departureAirportIcao,
                  String destinationAirportIcao, String departureAirportName, String destinationAirportName,
                  String scheduledDepartureTime, String actualDepartureTime, String scheduledArrivalTime,
                  String estimatedArrivalTime, double latitude, double longitude, double altitude,
                  double speed, FlightStatus status, long lastUpdated) {
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
