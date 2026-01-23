package com.flighttracker.api.controller;

import com.flighttracker.api.domain.Flight;
import com.flighttracker.api.service.FlightService;
import com.flighttracker.common.dto.FlightResponse;
import com.flighttracker.common.dto.FlightSearchResponse;
import com.flighttracker.common.exception.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final FlightService flightService;

    public SearchController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/flights")
    public ResponseEntity<FlightSearchResponse> searchFlights(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        if (query == null || query.isBlank()) {
            throw new BadRequestException("query", "Search query cannot be empty");
        }

        if (size > 100) {
            size = 100;
        }

        List<Flight> allFlights = flightService.searchFlights(query);

        int totalResults = allFlights.size();
        int fromIndex = Math.min(page * size, totalResults);
        int toIndex = Math.min(fromIndex + size, totalResults);

        List<FlightResponse> pagedFlights = allFlights.subList(fromIndex, toIndex).stream()
                .map(this::toFlightResponse)
                .toList();

        FlightSearchResponse response = new FlightSearchResponse(
                pagedFlights,
                totalResults,
                page,
                size,
                query
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/flights/callsign")
    public ResponseEntity<List<FlightResponse>> searchByCallsign(@RequestParam String callsign) {
        if (callsign == null || callsign.isBlank()) {
            throw new BadRequestException("callsign", "Callsign cannot be empty");
        }

        List<FlightResponse> flights = flightService.searchByCallsign(callsign).stream()
                .map(this::toFlightResponse)
                .toList();

        return ResponseEntity.ok(flights);
    }

    @GetMapping("/flights/airport")
    public ResponseEntity<List<FlightResponse>> searchByAirport(
            @RequestParam String icao,
            @RequestParam(defaultValue = "both") String type) {

        if (icao == null || icao.isBlank()) {
            throw new BadRequestException("icao", "Airport ICAO code cannot be empty");
        }

        List<Flight> flights = switch (type.toLowerCase()) {
            case "departure" -> flightService.searchByDepartureAirport(icao);
            case "arrival" -> flightService.searchByArrivalAirport(icao);
            default -> flightService.searchByAirport(icao);
        };

        List<FlightResponse> response = flights.stream()
                .map(this::toFlightResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    private FlightResponse toFlightResponse(Flight flight) {
        FlightResponse response = new FlightResponse();
        response.setFlightId(flight.getFlightId());
        response.setFlightNumber(flight.getFlightNumber());
        response.setCallsign(flight.getCallsign());
        response.setDepartureAirportIcao(flight.getDepartureAirportIcao());
        response.setDestinationAirportIcao(flight.getDestinationAirportIcao());
        response.setDepartureAirportName(flight.getDepartureAirportName());
        response.setDestinationAirportName(flight.getDestinationAirportName());
        response.setScheduledDepartureTime(flight.getScheduledDepartureTime());
        response.setActualDepartureTime(flight.getActualDepartureTime());
        response.setScheduledArrivalTime(flight.getScheduledArrivalTime());
        response.setEstimatedArrivalTime(flight.getEstimatedArrivalTime());
        response.setPosition(new FlightResponse.PositionDto(
                flight.getLatitude(),
                flight.getLongitude(),
                flight.getAltitude()
        ));
        response.setSpeed(flight.getSpeed());
        response.setStatus(flight.getStatus());
        response.setLastUpdated(flight.getLastUpdated());
        return response;
    }
}
