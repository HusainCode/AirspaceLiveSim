package com.flighttracker.api.integration.opensky;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;

/**
 * Configuration properties record that binds and validates OpenSky API credentials
 * from application configuration (application.yml or environment variables).
 * Ensures required credentials are present at application startup.
 */
@ConfigurationProperties(prefix = "opensky")
@Validated
public record OpenSkyProperties(
    @NotBlank String clientId,
    @NotBlank String clientSecret
) {}