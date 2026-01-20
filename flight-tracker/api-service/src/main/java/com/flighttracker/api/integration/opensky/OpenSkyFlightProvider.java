package com.flighttracker.api.integration.opensky;

import com.flighttracker.api.domain.Flight;
import com.flighttracker.common.model.FlightStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Provider for fetching flight data from OpenSky Network API.
 * Handles all OpenSky HTTP calls, authentication, and data transformation.
 */
@Component
public class OpenSkyFlightProvider {

    private final OpenSkyAuthClient authClient;
    private final WebClient webClient;

    public OpenSkyFlightProvider(OpenSkyAuthClient authClient, WebClient.Builder webClientBuilder) {
        this.authClient = authClient;
        this.webClient = webClientBuilder
                .baseUrl("https://opensky-network.org/api")
                .build();
    }

    /**
     * Fetches all current flight states from OpenSky Network.
     * Returns a Mono that emits a list of Flight objects.
     */
    public Mono<List<Flight>> getAllFlights() {
        return webClient.get()
                .uri("/states/all")
                .headers(headers -> authClient.addAuthHeaders(headers))
                .retrieve()
                .bodyToMono(Map.class)
                .map(this::parseFlightStates)
                .onErrorReturn(new ArrayList<>());
    }

    /**
     * Fetches flights within a specific bounding box.
     */
    public Mono<List<Flight>> getFlightsInArea(double minLat, double maxLat, double minLon, double maxLon) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/states/all")
                        .queryParam("lamin", minLat)
                        .queryParam("lamax", maxLat)
                        .queryParam("lomin", minLon)
                        .queryParam("lomax", maxLon)
                        .build())
                .headers(headers -> authClient.addAuthHeaders(headers))
                .retrieve()
                .bodyToMono(Map.class)
                .map(this::parseFlightStates)
                .onErrorReturn(new ArrayList<>());
    }

    @SuppressWarnings("unchecked")
    private List<Flight> parseFlightStates(Map<String, Object> response) {
        List<Flight> flights = new ArrayList<>();
        List<List<Object>> states = (List<List<Object>>) response.get("states");

        if (states == null) {
            return flights;
        }

        for (List<Object> state : states) {
            Flight flight = parseStateVector(state);
            if (flight != null && flight.hasPosition()) {
                flights.add(flight);
            }
        }
        return flights;
    }

    /**
     * Parses an OpenSky state vector array into a Flight object.
     * State vector indices:
     * 0: icao24, 1: callsign, 2: origin_country, 3: time_position,
     * 4: last_contact, 5: longitude, 6: latitude, 7: baro_altitude,
     * 8: on_ground, 9: velocity, 10: true_track, 11: vertical_rate,
     * 12: sensors, 13: geo_altitude, 14: squawk, 15: spi, 16: position_source
     */
    private Flight parseStateVector(List<Object> state) {
        if (state == null || state.size() < 17) {
            return null;
        }

        Flight flight = new Flight();
        flight.setFlightId(getString(state, 0));
        flight.setCallsign(getString(state, 1));
        flight.setLongitude(getDouble(state, 5));
        flight.setLatitude(getDouble(state, 6));
        flight.setAltitude(getDouble(state, 7));
        flight.setSpeed(getDouble(state, 9));

        boolean onGround = getBoolean(state, 8);
        flight.setStatus(onGround ? FlightStatus.LANDED : FlightStatus.IN_FLIGHT);
        flight.setLastUpdated(System.currentTimeMillis());

        return flight;
    }

    private String getString(List<Object> state, int index) {
        Object val = state.get(index);
        return val != null ? val.toString().trim() : null;
    }

    private double getDouble(List<Object> state, int index) {
        Object val = state.get(index);
        if (val instanceof Number) {
            return ((Number) val).doubleValue();
        }
        return 0.0;
    }

    private boolean getBoolean(List<Object> state, int index) {
        Object val = state.get(index);
        if (val instanceof Boolean) {
            return (Boolean) val;
        }
        return false;
    }
}