package com.example.airpot.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Domain entity representing a flight in the airport system.
 * Contains flight details, passenger information, and business logic for passenger management.
 * Implements domain events for integration with other bounded contexts.
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"events", "passengers"})
@Document(collection = "flights")
public class Flight {

    /**
     * Technical identifier used by MongoDB.
     * Flight number remains a unique business key but is not the primary @Id,
     * so that @CreatedDate/@LastModifiedDate auditing works correctly and
     * Mongo can manage persistence independently of domain keys.
     */
    @Id
    private String id; // Mongo-generated ObjectId - just so that the createdAt will not be null

    @NotBlank(message = "Flight number is required")
    @Size(min = 2, max = 10, message = "Flight number must be between 2 and 10 characters")
    @EqualsAndHashCode.Include
    @Indexed
    private String flightNumber;

    /**
     * The departure airport or location.
     */
    @NotBlank(message = "Origin is required")
    @Size(min = 3, max = 50, message = "Origin must be between 3 and 50 characters")
    @Indexed
    private String origin;

    /**
     * The arrival airport or location.
     */
    @NotBlank(message = "Destination is required")
    @Size(min = 3, max = 50, message = "Destination must be between 3 and 50 characters")
    @Indexed
    private String destination;

    /**
     * The scheduled departure date and time.
     */
    @NotNull(message = "Scheduled departure is required")
    @Future(message = "Scheduled departure must be in the future")
    @Indexed
    private LocalDateTime scheduledDeparture;

    /**
     * The scheduled arrival date and time.
     */
    @NotNull(message = "Scheduled arrival is required")
    @Future(message = "Scheduled arrival must be in the future")
    private LocalDateTime scheduledArrival;

    /**
     * List of passengers on this flight.
     */
    @Valid
    @Builder.Default
    private List<Passenger> passengers = new ArrayList<>();

    /**
     * Domain events for this flight entity.
     */
    @Builder.Default
    private final List<Object> events = new ArrayList<>();
    
    /**
     * Timestamp when the flight was created.
     */
    @CreatedDate
    private LocalDateTime createdAt;
    
    /**
     * Timestamp when the flight was last modified.
     */
    @LastModifiedDate
    private LocalDateTime lastModifiedAt;

    /**
     * Returns the domain events for this flight.
     * 
     * @return collection of domain events
     */
    @DomainEvents
    Collection<Object> domainEvents() {
        return events;
    }

    /**
     * Clears domain events after they have been published.
     */
    @AfterDomainEventPublication
    void clearDomainEvents() {
        events.clear();
    }

    /**
     * Adds a passenger to this flight.
     * Validates that the seat is not already taken if a seat assignment is provided.
     * 
     * @param passenger the passenger to add
     * @throws IllegalArgumentException if the seat is already assigned
     * @throws IllegalArgumentException if passenger is null
     */
    public void addPassenger(Passenger passenger) {
        Objects.requireNonNull(passenger, "Passenger cannot be null");
        
        if (passenger.getSeatAssignment() != null) {
            boolean isSeatTaken = passengers.stream()
                    .anyMatch(p -> p.getSeatAssignment() != null && 
                             p.getSeatAssignment().equals(passenger.getSeatAssignment()));
            if (isSeatTaken) {
                throw new IllegalArgumentException("Seat " + passenger.getSeatAssignment().getSeatNumber() + " is already assigned");
            }
        }
        this.passengers.add(passenger);
    }

    /**
     * Removes a passenger from this flight by passenger ID.
     * 
     * @param passengerId the unique identifier of the passenger to remove
     * @return true if the passenger was removed, false if not found
     * @throws IllegalArgumentException if passengerId is null or blank
     */
    public boolean removePassenger(String passengerId) {
        if (passengerId == null || passengerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Passenger ID cannot be null or empty");
        }
        return passengers.removeIf(p -> Objects.equals(p.getId(), passengerId));
    }

    /**
     * Returns the total number of passengers on this flight.
     * 
     * @return the passenger count
     */
    public int getPassengerCount() {
        return passengers.size();
    }
    
    /**
     * Validates that the arrival time is after the departure time.
     * 
     * @throws IllegalStateException if arrival is before or equal to departure
     */
    public void validateFlightTimes() {
        if (scheduledArrival != null && scheduledDeparture != null && 
            !scheduledArrival.isAfter(scheduledDeparture)) {
            throw new IllegalStateException("Scheduled arrival must be after scheduled departure");
        }
    }


}
