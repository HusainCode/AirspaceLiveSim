package com.flighttracker.common.cache;

import com.flighttracker.common.model.Flight;
import reactor.core.publisher.Mono;

/**
 * Reactive cache interface for Flight objects.
 * <p>
 * Key format: Must be normalized flight numbers (uppercase, no spaces).
 * Example: "AA123", "UA456"
 */
public interface FlightCache {

    /**
     * Retrieves a flight from cache.
     *
     * @param flightNumber the flight identifier
     * @return Mono containing the flight if cached, empty Mono otherwise
     */
    Mono<Flight> get(String flightNumber);

    /**
     * Stores a flight in cache.
     *
     * @param flightNumber the flight identifier
     * @param flight the flight data to cache
     * @return Mono that completes when the operation finishes
     */
    Mono<Void> put(String flightNumber, Flight flight);

    /**
     * Removes a flight from cache.
     * Use when flight data becomes stale or invalid.
     *
     * @param flightNumber the flight identifier to remove
     * @return Mono<Boolean> true if key existed and was deleted, false if key didn't exist
     */
    Mono<Boolean> delete(String flightNumber);

    /**
     * Checks if a flight exists in cache without fetching it.
     * More efficient than get() when you only need to check presence.
     *
     * @param flightNumber the flight identifier
     * @return Mono<Boolean> true if cached, false otherwise
     */
    Mono<Boolean> exists(String flightNumber);

    /**
     * Removes all entries from cache.
     * Use sparingly - typically for testing or emergency cache invalidation.
     *
     * @return Mono that completes when cache is cleared
     */
    Mono<Void> clear();

}
