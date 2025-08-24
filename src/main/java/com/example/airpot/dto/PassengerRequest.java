package com.example.airpot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for passenger creation and update requests.
 * Contains passenger information including name and seat assignment details.
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PassengerRequest {
    
    /**
     * The full name of the passenger.
     */
    @NotBlank(message = "Passenger name is required")
    @Size(min = 2, max = 100, message = "Passenger name must be between 2 and 100 characters")
    private String name;

    /**
     * The seat number assignment (e.g., "12A", "15B").
     */
    @Pattern(regexp = "^[1-9][0-9]?[A-F]$", message = "Seat number must be in format like '12A' or '5B'")
    @NotBlank(message = "Seat number is required")
    private String seatNumber;

    /**
     * The class of service for the seat.
     * Valid values: Economy, Business, First Class
     */
    @Pattern(regexp = "^(Economy|Business|First Class)$", message = "Seat class must be Economy, Business, or First Class")
    @NotBlank(message = "Seat number is required")
    private String seatClass;
}
