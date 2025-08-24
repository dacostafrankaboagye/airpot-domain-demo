package com.example.airpot.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Domain entity representing a passenger in the airport system.
 * Contains passenger information including name and seat assignment.
 * 
 * @author Generated
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Passenger {

    /**
     * The unique identifier for the passenger.
     */
    @Id
    @EqualsAndHashCode.Include
    private String id;

    /**
     * The full name of the passenger.
     */
    @NotBlank(message = "Passenger name is required")
    @Size(min = 2, max = 100, message = "Passenger name must be between 2 and 100 characters")
    private String name;

    /**
     * The seat assignment for this passenger.
     */
    @Valid
    private SeatAssignment seatAssignment;
    
    /**
     * Timestamp when the passenger record was created.
     */
    @CreatedDate
    private LocalDateTime createdAt;
    
    /**
     * Timestamp when the passenger record was last modified.
     */
    @LastModifiedDate
    private LocalDateTime lastModifiedAt;
    
    /**
     * Checks if the passenger has a seat assignment.
     * 
     * @return true if seat assignment exists, false otherwise
     */
    public boolean hasSeatAssignment() {
        return seatAssignment != null;
    }
    
    /**
     * Updates the seat assignment for this passenger.
     * 
     * @param seatAssignment the new seat assignment
     * @throws IllegalArgumentException if seatAssignment is null
     */
    public void updateSeatAssignment(SeatAssignment seatAssignment) {
        Objects.requireNonNull(seatAssignment, "Seat assignment cannot be null");
        this.seatAssignment = seatAssignment;
    }
}
