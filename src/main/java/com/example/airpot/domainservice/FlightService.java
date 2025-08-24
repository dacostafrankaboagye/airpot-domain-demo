package com.example.airpot.domainservice;

import com.example.airpot.domain.Flight;
import com.example.airpot.domain.Passenger;
import com.example.airpot.repository.FlightRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Domain service for managing flight operations and business logic.
 * Handles passenger management, flight queries, and maintains data consistency.
 *
 */
@Service
@RequiredArgsConstructor
@Transactional
@Validated
@Slf4j
public class FlightService {

    private final FlightRepository flightRepository;

    /**
     * Adds a passenger to a specific flight.
     * Validates seat availability and saves the updated flight.
     * 
     * @param flightNumber the unique flight number
     * @param passenger the passenger to add with seat assignment
     * @throws IllegalArgumentException if flight is not found or seat is taken
     * @throws IllegalArgumentException if parameters are null or invalid
     */
    public void addPassengerToFlight(
            @NotBlank(message = "Flight number is required") String flightNumber, 
            @Valid @NotNull(message = "Passenger is required") Passenger passenger) {
        
        Objects.requireNonNull(flightNumber, "Flight number cannot be null");
        Objects.requireNonNull(passenger, "Passenger cannot be null");
        
        log.debug("Adding passenger {} to flight {}", passenger.getName(), flightNumber);
        
        Flight flight = flightRepository
                .findByFlightNumber(flightNumber)
                .orElseThrow(() -> new IllegalArgumentException("Flight not found: " + flightNumber));

        flight.addPassenger(passenger);
        flightRepository.save(flight);
        
        log.info("Successfully added passenger {} to flight {}", passenger.getName(), flightNumber);
    }

    /**
     * Removes a passenger from a specific flight.
     * 
     * @param flightNumber the unique flight number
     * @param passengerId the unique passenger identifier
     * @return true if passenger was removed, false if not found
     * @throws IllegalArgumentException if flight is not found or parameters are invalid
     */
    public boolean removePassengerFromFlight(
            @NotBlank(message = "Flight number is required") String flightNumber, 
            @NotBlank(message = "Passenger ID is required") String passengerId) {
        
        Objects.requireNonNull(flightNumber, "Flight number cannot be null");
        Objects.requireNonNull(passengerId, "Passenger ID cannot be null");
        
        log.debug("Removing passenger {} from flight {}", passengerId, flightNumber);
        
        Flight flight = flightRepository
                .findByFlightNumber(flightNumber)
                .orElseThrow(() -> new IllegalArgumentException("Flight not found: " + flightNumber));

        boolean removed = flight.removePassenger(passengerId);
        if (removed) {
            flightRepository.save(flight);
            log.info("Successfully removed passenger {} from flight {}", passengerId, flightNumber);
        } else {
            log.warn("Passenger {} not found on flight {}", passengerId, flightNumber);
        }

        return removed;
    }

    /**
     * Retrieves a flight with all its passengers.
     * 
     * @param flightNumber the unique flight number
     * @return the flight with passenger information
     * @throws IllegalArgumentException if flight is not found
     */
    @Transactional(readOnly = true)
    public Flight getFlightWithPassengers(
            @NotBlank(message = "Flight number is required") String flightNumber) {
        
        Objects.requireNonNull(flightNumber, "Flight number cannot be null");
        
        return flightRepository
                .findByFlightNumber(flightNumber)
                .orElseThrow(() -> new IllegalArgumentException("Flight not found: " + flightNumber));
    }

    /**
     * Finds flights for a specific route.
     * 
     * @param origin the departure location
     * @param destination the arrival location
     * @return list of flights matching the route
     * @throws IllegalArgumentException if parameters are invalid
     */
    @Transactional(readOnly = true)
    public List<Flight> findFlightsByRoute(
            @NotBlank(message = "Origin is required") String origin, 
            @NotBlank(message = "Destination is required") String destination) {
        
        Objects.requireNonNull(origin, "Origin cannot be null");
        Objects.requireNonNull(destination, "Destination cannot be null");
        
        log.debug("Finding flights from {} to {}", origin, destination);
        
        return flightRepository.findByRoute(origin, destination);
    }

    /**
     * Finds flights within a specific departure time range.
     * 
     * @param start the start of the time range (inclusive)
     * @param end the end of the time range (inclusive)
     * @return list of flights departing within the specified range
     * @throws IllegalArgumentException if parameters are invalid or start is after end
     */
    @Transactional(readOnly = true)
    public List<Flight> findFlightsByDepartureRange(
            @NotNull(message = "Start time is required") LocalDateTime start, 
            @NotNull(message = "End time is required") LocalDateTime end) {
        
        Objects.requireNonNull(start, "Start time cannot be null");
        Objects.requireNonNull(end, "End time cannot be null");
        
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start time must be before or equal to end time");
        }
        
        log.debug("Finding flights departing between {} and {}", start, end);
        
        return flightRepository.findFlightsByDepartureTimeRange(start, end);
    }

    /**
     * Checks if a flight exists by flight number.
     * 
     * @param flightNumber the unique flight number to check
     * @return true if flight exists, false otherwise
     * @throws IllegalArgumentException if flight number is invalid
     */
    @Transactional(readOnly = true)
    public boolean flightExists(
            @NotBlank(message = "Flight number is required") String flightNumber) {
        
        Objects.requireNonNull(flightNumber, "Flight number cannot be null");
        
        return flightRepository.findByFlightNumber(flightNumber).isPresent();
    }

}
