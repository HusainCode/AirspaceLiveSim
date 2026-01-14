package com.flighttracker.api;

import com.flighttracker.api.integration.opensky.OpenSkyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * API Service entry point.
 * Provides REST endpoints and WebSocket support for flight tracking.
 *
 * ApiApplication → starts app
 * StreamController → streams data
 * FlightService → calls + filters API
 * OpenSkyAuthClient → auth only
 * OpenSkyProperties → config only
 *
 */
@SpringBootApplication(scanBasePackages = {
        "com.flighttracker.api",
        "com.flighttracker.common"
})
@EnableConfigurationProperties(OpenSkyProperties.class)
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}
