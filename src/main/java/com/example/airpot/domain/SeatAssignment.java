package com.example.airpot.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * Value object representing a seat assignment on a flight.
 * Contains seat number and class information with validation.
 * Immutable value object following DDD principles.
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatAssignment {
    
    /**
     * The seat number (e.g., "12A", "5B").
     */
    @NotBlank(message = "Seat number is required")
    @Pattern(regexp = "^[1-9][0-9]?[A-F]$", message = "Seat number must be in format like '12A' or '5B'")
    private String seatNumber;
    
    /**
     * The class of service for the seat.
     * Valid values: Economy, Business, First Class
     */
    @NotBlank(message = "Seat class is required")
    @Pattern(regexp = "^(Economy|Business|First Class)$", message = "Seat class must be Economy, Business, or First Class")
    private String seatClass;
    
    /**
     * Checks if this is an economy class seat.
     * 
     * @return true if seat class is Economy
     */
    public boolean isEconomy() {
        return "Economy".equals(seatClass);
    }
    
    /**
     * Checks if this is a business class seat.
     * 
     * @return true if seat class is Business
     */
    public boolean isBusiness() {
        return "Business".equals(seatClass);
    }
    
    /**
     * Checks if this is a first class seat.
     * 
     * @return true if seat class is First Class
     */
    public boolean isFirstClass() {
        return "First Class".equals(seatClass);
    }

    /**
     * Compares this seat assignment with another object for equality.
     * Two seat assignments are equal if they have the same seat number and class.
     * 
     * @param o the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeatAssignment that = (SeatAssignment) o;
        return Objects.equals(seatNumber, that.seatNumber) &&
                Objects.equals(seatClass, that.seatClass);
    }

    /**
     * Returns the hash code for this seat assignment.
     * 
     * @return hash code based on seat number and class
     */
    @Override
    public int hashCode() {
        return Objects.hash(seatNumber, seatClass);
    }
}
