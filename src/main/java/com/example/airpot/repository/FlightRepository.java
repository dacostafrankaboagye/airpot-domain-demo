package com.example.airpot.repository;

import com.example.airpot.domain.Flight;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Flight entities in MongoDB.
 * Extends MongoRepository to provide basic CRUD operations and custom query methods.
 *
 */
public interface FlightRepository extends MongoRepository<Flight, String> {
    /**
     * Finds a flight by its flight number.
     * 
     * @param flightNumber the unique flight number to search for
     * @return an Optional containing the flight if found, empty otherwise
     */
    Optional<Flight> findByFlightNumber(String flightNumber);

    Long deleteFlightByFlightNumber(String flightNumber);

    /**
     * Finds all flights departing from a specific origin.
     * 
     * @param origin the departure location
     * @return a list of flights from the specified origin
     */
    List<Flight> findByOrigin(String origin);
    /**
     * Finds all flights arriving at a specific destination.
     * 
     * @param destination the arrival location
     * @return a list of flights to the specified destination
     */
    List<Flight> findByDestination(String destination);

    /**
     * Finds flights within a specific departure time range.
     * 
     * @param start the start of the time range (inclusive)
     * @param end the end of the time range (inclusive)
     * @return a list of flights departing within the specified time range
     */
    @Query("{'scheduledDeparture': {$gte: ?0, $lte: ?1}}")
    List<Flight> findFlightsByDepartureTimeRange(LocalDateTime start, LocalDateTime end);

    /**
     * Finds flights for a specific route (origin to destination).
     * 
     * @param origin the departure location
     * @param destination the arrival location
     * @return a list of flights matching the specified route
     */
    @Query("{'origin': ?0, 'destination': ?1}")
    List<Flight> findByRoute(String origin, String destination);

}
