package com.flighttracker.common.cache;

import com.flighttracker.common.model.Flight;

import java.beans.Transient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

public class InMemoryFlightCacheTest {

    private InMemoryFlightCache cache;

    @BeforeEach
    void setUp() {
        cache = new InMemoryFlightCache(100, 10);
    }

    // Helper method
    private Flight createFlight() {
        return new Flight(
                "id-1",
                "AA123",
                "CALL123",
                "KJFK",
                "KLAX",
                "JFK",
                "LAX",
                "10:00",
                "10:05",
                "13:00",
                "13:10",
                40.0,
                -73.0,
                30000,
                450,
                FlightStatus.IN_FLIGHT,
                System.currentTimeMillis());
    }
    @Test
    void shouldStoreAndRetrieveFlight() {
        Flight flight = createFlight();

        StepVerifier.create(cache.put(flight))
                .verifyComplete();

    }

}
