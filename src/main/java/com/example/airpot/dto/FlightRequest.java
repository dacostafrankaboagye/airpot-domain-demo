package com.example.airpot.dto;

import lombok.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for flight creation and update requests.
 * Contains all necessary information to create or modify a flight.
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightRequest {
    
    /**
     * The unique flight number identifier.
     */
    @NotBlank(message = "Flight number is required")
    @Size(min = 2, max = 10, message = "Flight number must be between 2 and 10 characters")
    private String flightNumber;
    
    /**
     * The departure airport or location.
     */
    @NotBlank(message = "Origin is required")
    @Size(min = 3, max = 50, message = "Origin must be between 3 and 50 characters")
    private String origin;
    
    /**
     * The arrival airport or location.
     */
    @NotBlank(message = "Destination is required")
    @Size(min = 3, max = 50, message = "Destination must be between 3 and 50 characters")
    private String destination;
    
    /**
     * The scheduled departure date and time.
     */
    @NotNull(message = "Scheduled departure is required")
    private LocalDateTime scheduledDeparture;
    
    /**
     * The scheduled arrival date and time.
     */
    @NotNull(message = "Scheduled arrival is required")
    private LocalDateTime scheduledArrival;
}
