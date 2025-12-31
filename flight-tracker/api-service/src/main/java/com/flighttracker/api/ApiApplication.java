package com.flighttracker.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * API Service entry point.
 * Provides REST endpoints and WebSocket support for flight tracking.
 *
 * ApiApplication → starts app
 * StreamController → streams data
 * OpenSkyFlightService → calls + filters API
 * OpenSkyAuthService → auth only
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
