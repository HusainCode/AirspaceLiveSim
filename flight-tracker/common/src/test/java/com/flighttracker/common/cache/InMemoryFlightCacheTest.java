package com.flighttracker.common.cache;

import com.flighttracker.common.model.Flight;
import com.flighttracker.common.model.FlightStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryFlightCacheTest {

  private InMemoryFlightCache cache;

  @BeforeEach
  void setUp() {
    cache = new InMemoryFlightCache(100, 10);
  }

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
  void put_stores_flight_by_flightNumber() {
    Flight flight = createFlight();

    StepVerifier.create(cache.put(flight.getFlightNumber(), flight)).verifyComplete();

    StepVerifier.create(cache.get(flight.getFlightNumber())).expectNext(flight).verifyComplete();
  }

  @Test
  void get_returns_empty_when_flight_missing() {
    StepVerifier.create(cache.get("UNKNOWN")).verifyComplete();
  }
  
  @Test
  void exists_reflects_cache_state() {
    Flight flight = createFlight();
    String key = flight.getFlightNumber();

    StepVerifier.create(cache.exists(key)).expectNext(false).verifyComplete();

    cache.put(key, flight).block();
    StepVerifier.create(cache.exists(key)).expectNext(true).verifyComplete();
  }
  
  @Test
  void delete_returns_true_only_if_entry_existed() {
    Flight flight = createFlight();
    String key = flight.getFlightNumber();

    cache.put(key, flight).block();

    StepVerifier.create(cache.delete(key)).expectNext(true).verifyComplete();

    StepVerifier.create(cache.delete(key)).expectNext(false).verifyComplete();
  }
  
  @Test
  void clear_removes_all_entries() {
    Flight flightA = createFlight();
    Flight flightB = createFlight();

    cache.put("A", flightA).block();
    cache.put("B", flightB).block();

    cache.clear().block();

    assertThat(cache.getStats().size()).isZero();
  }
}
