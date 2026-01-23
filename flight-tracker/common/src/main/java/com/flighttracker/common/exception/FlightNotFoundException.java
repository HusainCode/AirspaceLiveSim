package com.flighttracker.common.exception;

public class FlightNotFoundException extends RuntimeException {

    private final String flightId;

    public FlightNotFoundException(String flightId) {
        super("Flight not found: " + flightId);
        this.flightId = flightId;
    }

    public FlightNotFoundException(String flightId, String message) {
        super(message);
        this.flightId = flightId;
    }

    public FlightNotFoundException(String flightId, String message, Throwable cause) {
        super(message, cause);
        this.flightId = flightId;
    }

    public String getFlightId() {
        return flightId;
    }
}
