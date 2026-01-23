package com.flighttracker.common.dto;

import com.flighttracker.common.model.FlightStatus;

public class FlightResponse {

    private String flightId;
    private String flightNumber;
    private String callsign;
    private String departureAirportIcao;
    private String destinationAirportIcao;
    private String departureAirportName;
    private String destinationAirportName;
    private String scheduledDepartureTime;
    private String actualDepartureTime;
    private String scheduledArrivalTime;
    private String estimatedArrivalTime;
    private PositionDto position;
    private double speed;
    private FlightStatus status;
    private long lastUpdated;

    public FlightResponse() {
    }

    public FlightResponse(String flightId, String flightNumber, String callsign,
                          String departureAirportIcao, String destinationAirportIcao,
                          String departureAirportName, String destinationAirportName,
                          String scheduledDepartureTime, String actualDepartureTime,
                          String scheduledArrivalTime, String estimatedArrivalTime,
                          PositionDto position, double speed, FlightStatus status, long lastUpdated) {
        this.flightId = flightId;
        this.flightNumber = flightNumber;
        this.callsign = callsign;
        this.departureAirportIcao = departureAirportIcao;
        this.destinationAirportIcao = destinationAirportIcao;
        this.departureAirportName = departureAirportName;
        this.destinationAirportName = destinationAirportName;
        this.scheduledDepartureTime = scheduledDepartureTime;
        this.actualDepartureTime = actualDepartureTime;
        this.scheduledArrivalTime = scheduledArrivalTime;
        this.estimatedArrivalTime = estimatedArrivalTime;
        this.position = position;
        this.speed = speed;
        this.status = status;
        this.lastUpdated = lastUpdated;
    }

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getCallsign() {
        return callsign;
    }

    public void setCallsign(String callsign) {
        this.callsign = callsign;
    }

    public String getDepartureAirportIcao() {
        return departureAirportIcao;
    }

    public void setDepartureAirportIcao(String departureAirportIcao) {
        this.departureAirportIcao = departureAirportIcao;
    }

    public String getDestinationAirportIcao() {
        return destinationAirportIcao;
    }

    public void setDestinationAirportIcao(String destinationAirportIcao) {
        this.destinationAirportIcao = destinationAirportIcao;
    }

    public String getDepartureAirportName() {
        return departureAirportName;
    }

    public void setDepartureAirportName(String departureAirportName) {
        this.departureAirportName = departureAirportName;
    }

    public String getDestinationAirportName() {
        return destinationAirportName;
    }

    public void setDestinationAirportName(String destinationAirportName) {
        this.destinationAirportName = destinationAirportName;
    }

    public String getScheduledDepartureTime() {
        return scheduledDepartureTime;
    }

    public void setScheduledDepartureTime(String scheduledDepartureTime) {
        this.scheduledDepartureTime = scheduledDepartureTime;
    }

    public String getActualDepartureTime() {
        return actualDepartureTime;
    }

    public void setActualDepartureTime(String actualDepartureTime) {
        this.actualDepartureTime = actualDepartureTime;
    }

    public String getScheduledArrivalTime() {
        return scheduledArrivalTime;
    }

    public void setScheduledArrivalTime(String scheduledArrivalTime) {
        this.scheduledArrivalTime = scheduledArrivalTime;
    }

    public String getEstimatedArrivalTime() {
        return estimatedArrivalTime;
    }

    public void setEstimatedArrivalTime(String estimatedArrivalTime) {
        this.estimatedArrivalTime = estimatedArrivalTime;
    }

    public PositionDto getPosition() {
        return position;
    }

    public void setPosition(PositionDto position) {
        this.position = position;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public FlightStatus getStatus() {
        return status;
    }

    public void setStatus(FlightStatus status) {
        this.status = status;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public static class PositionDto {
        private double latitude;
        private double longitude;
        private double altitude;

        public PositionDto() {
        }

        public PositionDto(double latitude, double longitude, double altitude) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.altitude = altitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getAltitude() {
            return altitude;
        }

        public void setAltitude(double altitude) {
            this.altitude = altitude;
        }
    }
}
