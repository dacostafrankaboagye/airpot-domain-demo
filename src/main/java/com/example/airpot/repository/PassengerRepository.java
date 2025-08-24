package com.example.airpot.repository;

import com.example.airpot.domain.Passenger;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Passenger entities in MongoDB.
 * Extends MongoRepository to provide basic CRUD operations and custom query methods
 * for passenger-specific searches including seat assignments.
 *
 */
@Repository
public interface PassengerRepository extends MongoRepository<Passenger, String> {
    
    /**
     * Finds passengers by their name.
     * 
     * @param name the passenger name to search for
     * @return a list of passengers with the specified name
     */
    List<Passenger> findByName(String name);

    /**
     * Finds a passenger by their seat number.
     * Uses MongoDB query to search within the nested seatAssignment object.
     * 
     * @param seatNumber the seat number to search for (e.g., "12A")
     * @return an Optional containing the passenger if found, empty otherwise
     */
    @Query("{'seatAssignment.seatNumber': ?0}")
    Optional<Passenger> findBySeatNumber(String seatNumber);

    /**
     * Finds all passengers by their seat class.
     * Uses MongoDB query to search within the nested seatAssignment object.
     * 
     * @param seatClass the seat class to search for (Economy, Business, First Class)
     * @return a list of passengers in the specified seat class
     */
    @Query("{'seatAssignment.seatClass': ?0}")
    List<Passenger> findBySeatClass(String seatClass);
}
