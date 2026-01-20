package com.flighttracker.api.integration.opensky;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Manages OAuth2 authentication with OpenSky Network API.
 * Handles token acquisition, in-memory caching, and automatic refresh
 * to ensure valid credentials are always available for API calls.
 */
@Service
public class OpenSkyAuthClient {

    private static final Logger log = LoggerFactory.getLogger(OpenSkyAuthClient.class);
    private static final Duration TOKEN_VALIDITY = Duration.ofMinutes(28);
    private static final String AUTH_URI = "/auth/realms/opensky-network/protocol/openid-connect/token";

    private final WebClient webClient;
    private final OpenSkyProperties properties;
    private final Lock lock = new ReentrantLock();

    private volatile String accessToken;
    private volatile Instant expiresAt;

    public OpenSkyAuthClient(WebClient.Builder webClientBuilder, OpenSkyProperties properties) {
        this.webClient = webClientBuilder
                .baseUrl("https://auth.opensky-network.org")
                .build();
        this.properties = properties;
    }

    public String getValidToken() {
        if (accessToken == null || isExpired()) {
            lock.lock();
            try {
                if (accessToken == null || isExpired()) {
                    refreshToken();
                }
            } finally {
                lock.unlock();
            }
        }
        return accessToken;
    }

    private boolean isExpired() {
        boolean expired = expiresAt == null || Instant.now().isAfter(expiresAt);
        log.debug("Token expired: {}", expired);
        return expired;
    }

    private void refreshToken() {
        log.info("Refreshing OpenSky access token");

        Map<String, Object> response = webClient.post()
                .uri(AUTH_URI)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "client_credentials")
                        .with("client_id", properties.clientId())
                        .with("client_secret", properties.clientSecret()))
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        if (response == null) {
            throw new IllegalStateException("Failed to retrieve access token from OpenSky");
        }

        this.accessToken = (String) response.get("access_token");
        this.expiresAt = Instant.now().plus(TOKEN_VALIDITY);

        log.info("Token refreshed successfully, expires at {}", expiresAt);
    }

    /**
     * Adds the authorization header to the given HttpHeaders.
     */
    public void addAuthHeaders(org.springframework.http.HttpHeaders headers) {
        String token = getValidToken();
        if (token != null) {
            headers.setBearerAuth(token);
        }
    }
}
