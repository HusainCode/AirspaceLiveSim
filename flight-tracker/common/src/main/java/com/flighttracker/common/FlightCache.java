// Redis can go here

public interface FlightCache {
    Mono<Flight> get(String flightNumber);

    Mono<Void> put(String flightNumber, Flight flight);
}
