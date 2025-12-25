package com.flighttracker.api;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;

@ConfigurationProperties(prefix = "opensky")
@Validated
public record OpenSkyProperties(
    @NotBlank String clientId,
    @NotBlank String clientSecret
) {}