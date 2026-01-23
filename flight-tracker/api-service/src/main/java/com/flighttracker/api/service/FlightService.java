package com.flighttracker.api.service;

import com.flighttracker.api.domain.Flight;
import com.flighttracker.api.integration.opensky.OpenSkyFlightProvider;
import com.flighttracker.common.model.FlightStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Orchestrates flight data operations.
 * Delegates to providers for external data retrieval.
 */
@Service
public class FlightService {

    private final OpenSkyFlightProvider openSkyProvider;

    public FlightService(OpenSkyFlightProvider openSkyProvider) {
        this.openSkyProvider = openSkyProvider;
    }

    /**
     * Returns a Flux that emits lists of current flight positions at regular intervals.
     * Each emission contains the latest snapshot of all tracked flights.
     */
    public Flux<List<Flight>> streamFlightUpdates(Duration interval) {
        return Flux.interval(interval)
                .flatMap(tick -> openSkyProvider.getAllFlights());
    }

    /**
     * Returns a Flux of flight updates within a specific geographic area.
     */
    public Flux<List<Flight>> streamFlightsInArea(Duration interval,
                                                   double minLat, double maxLat,
                                                   double minLon, double maxLon) {
        return Flux.interval(interval)
                .flatMap(tick -> openSkyProvider.getFlightsInArea(minLat, maxLat, minLon, maxLon));
    }

    /**
     * Retrieves a single flight by its ID (icao24 code).
     * Searches through all current flights from OpenSky.
     */
    public Optional<Flight> getFlightById(String id) {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }

        List<Flight> flights = openSkyProvider.getAllFlights().block();
        if (flights == null) {
            return Optional.empty();
        }

        return flights.stream()
                .filter(f -> id.equalsIgnoreCase(f.getFlightId()) || id.equalsIgnoreCase(f.getCallsign()))
                .findFirst();
    }

    /**
     * Returns the status of a flight as a string.
     */
    public String getFlightByStatus(String id) {
        return getFlightById(id)
                .map(Flight::getStatus)
                .map(FlightStatus::name)
                .orElse("UNKNOWN");
    }

    /**
     * Returns flight metadata including position, timing, and route information.
     */
    public Map<String, Object> getFlightMetadata(String id) {
        return getFlightById(id)
                .map(flight -> Map.<String, Object>of(
                        "flightId", nullSafe(flight.getFlightId()),
                        "callsign", nullSafe(flight.getCallsign()),
                        "status", nullSafe(flight.getStatus()),
                        "position", Map.of(
                                "latitude", flight.getLatitude(),
                                "longitude", flight.getLongitude(),
                                "altitude", flight.getAltitude()
                        ),
                        "speed", flight.getSpeed(),
                        "lastUpdated", flight.getLastUpdated(),
                        "departure", Map.of(
                                "airportIcao", nullSafe(flight.getDepartureAirportIcao()),
                                "airportName", nullSafe(flight.getDepartureAirportName()),
                                "scheduledTime", nullSafe(flight.getScheduledDepartureTime()),
                                "actualTime", nullSafe(flight.getActualDepartureTime())
                        ),
                        "arrival", Map.of(
                                "airportIcao", nullSafe(flight.getDestinationAirportIcao()),
                                "airportName", nullSafe(flight.getDestinationAirportName()),
                                "scheduledTime", nullSafe(flight.getScheduledArrivalTime()),
                                "estimatedTime", nullSafe(flight.getEstimatedArrivalTime())
                        )
                ))
                .orElse(Map.of());
    }

    public List<Flight> searchFlights(String query) {
        if (query == null || query.isBlank()) {
            return Collections.emptyList();
        }

        List<Flight> flights = openSkyProvider.getAllFlights().block();
        if (flights == null) {
            return Collections.emptyList();
        }

        String lowerQuery = query.toLowerCase();
        return flights.stream()
                .filter(f -> matchesFlight(f, lowerQuery))
                .toList();
    }

    public List<Flight> searchByCallsign(String callsign) {
        if (callsign == null || callsign.isBlank()) {
            return Collections.emptyList();
        }

        List<Flight> flights = openSkyProvider.getAllFlights().block();
        if (flights == null) {
            return Collections.emptyList();
        }

        String lowerCallsign = callsign.toLowerCase();
        return flights.stream()
                .filter(f -> f.getCallsign() != null && f.getCallsign().toLowerCase().contains(lowerCallsign))
                .toList();
    }

    public List<Flight> searchByDepartureAirport(String icao) {
        if (icao == null || icao.isBlank()) {
            return Collections.emptyList();
        }

        List<Flight> flights = openSkyProvider.getAllFlights().block();
        if (flights == null) {
            return Collections.emptyList();
        }

        String upperIcao = icao.toUpperCase();
        return flights.stream()
                .filter(f -> upperIcao.equals(f.getDepartureAirportIcao()))
                .toList();
    }

    public List<Flight> searchByArrivalAirport(String icao) {
        if (icao == null || icao.isBlank()) {
            return Collections.emptyList();
        }

        List<Flight> flights = openSkyProvider.getAllFlights().block();
        if (flights == null) {
            return Collections.emptyList();
        }

        String upperIcao = icao.toUpperCase();
        return flights.stream()
                .filter(f -> upperIcao.equals(f.getDestinationAirportIcao()))
                .toList();
    }

    public List<Flight> searchByAirport(String icao) {
        if (icao == null || icao.isBlank()) {
            return Collections.emptyList();
        }

        List<Flight> flights = openSkyProvider.getAllFlights().block();
        if (flights == null) {
            return Collections.emptyList();
        }

        String upperIcao = icao.toUpperCase();
        return flights.stream()
                .filter(f -> upperIcao.equals(f.getDepartureAirportIcao()) ||
                             upperIcao.equals(f.getDestinationAirportIcao()))
                .toList();
    }

    private boolean matchesFlight(Flight flight, String query) {
        return containsIgnoreCase(flight.getFlightId(), query) ||
               containsIgnoreCase(flight.getCallsign(), query) ||
               containsIgnoreCase(flight.getFlightNumber(), query) ||
               containsIgnoreCase(flight.getDepartureAirportIcao(), query) ||
               containsIgnoreCase(flight.getDestinationAirportIcao(), query) ||
               containsIgnoreCase(flight.getDepartureAirportName(), query) ||
               containsIgnoreCase(flight.getDestinationAirportName(), query);
    }

    private boolean containsIgnoreCase(String str, String query) {
        return str != null && str.toLowerCase().contains(query);
    }

    private Object nullSafe(Object value) {
        return value != null ? value : "";
    }
}
