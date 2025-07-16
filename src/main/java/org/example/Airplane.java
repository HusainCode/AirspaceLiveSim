package org.example;

public class Airplane {
    public String id;
    public double lat, lon, altitude, speed, heading;

    public Airplane(String id, double lat, double lon, double altitude, double speed, double heading) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.altitude = altitude;
        this.speed = speed;
        this.heading = heading;
    }

    @Override
    public String toString() {
        return String.format(
                // Returns a human readable string representing the airplane’s current state.
                // Includes ID, latitude, longitude, altitude, speed, and heading, all nicely formatted.
                "Airplane{id='%s', lat=%.4f, lon=%.4f, alt=%.1f, speed=%.1f, heading=%.1f}",
                id, lat, lon, altitude, speed, heading
        );
    }
}
