package com.flighttracker.common.model;

public class FlightPosition {

    private double latitude;
    private double longitude;
    private double altitude;
    private double heading;
    private double speed;
    private long timestamp;

    public FlightPosition() {
    }

    public FlightPosition(double latitude, double longitude, double altitude, double heading, double speed, long timestamp) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.heading = heading;
        this.speed = speed;
        this.timestamp = timestamp;
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

    public double getHeading() {
        return heading;
    }

    public void setHeading(double heading) {
        this.heading = heading;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isValid() {
        return latitude >= -90 && latitude <= 90 && longitude >= -180 && longitude <= 180;
    }

    @Override
    public String toString() {
        return "FlightPosition{" +
                "lat=" + latitude +
                ", lon=" + longitude +
                ", alt=" + altitude +
                ", heading=" + heading +
                ", speed=" + speed +
                '}';
    }
}
