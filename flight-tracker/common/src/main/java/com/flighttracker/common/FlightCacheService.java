@Service
public class FlightCacheService {

    private final FlightCache localCache; // Caffeine
    private final FlightCache remoteCache; // Redis

    public FlightCacheService(
            @Qualifier("inMemoryFlightCache") FlightCache localCache,
            @Qualifier("redisFlightCache") FlightCache remoteCache) {
        this.localCache = localCache;
        this.remoteCache = remoteCache;
    }

    public Mono<Flight> get(String flightNumber) {
        return localCache.get(flightNumber)
                .switchIfEmpty(
                        remoteCache.get(flightNumber)
                                .flatMap(flight -> localCache.put(flightNumber, flight)
                                        .thenReturn(flight)));
    }
}
