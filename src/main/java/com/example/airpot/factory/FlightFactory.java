package com.example.airpot.factory;

import com.example.airpot.domain.Flight;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Factory class for creating Flight instances with validation.
 * Provides methods to create flights with custom or default scheduling.
 */
@Component
public class FlightFactory {

    /**
     * Creates a new Flight with specified scheduling details.
     *
     * @param flightNumber the unique flight identifier
     * @param origin the departure airport/location
     * @param destination the arrival airport/location
     * @param scheduledDeparture the scheduled departure time
     * @param scheduledArrival the scheduled arrival time
     * @return a new Flight instance
     * @throws IllegalArgumentException if departure is after arrival or if departure is in the past
     */
    public Flight createFlight(
            String flightNumber, String origin, String destination,
            LocalDateTime scheduledDeparture, LocalDateTime scheduledArrival
    ){

        if (scheduledDeparture.isAfter(scheduledArrival)) {
            throw new IllegalArgumentException("Departure time cannot be after arrival time");
        }

        if (scheduledDeparture.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot schedule flight in the past");
        }

        return Flight.builder()
                .flightNumber(flightNumber)
                .origin(origin)
                .destination(destination)
                .scheduledDeparture(scheduledDeparture)
                .scheduledArrival(scheduledArrival)
                .build();

    }

    /**
     * Creates a new Flight with default timing (departure now + 2 hours, arrival + 4 hours).
     *
     * @param flightNumber the unique flight identifier
     * @param origin the departure airport/location
     * @param destination the arrival airport/location
     * @return a new Flight instance with default scheduling
     */
    public Flight createFlight(
            String flightNumber, String origin, String destination
    ){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime departure = now.plusHours(2);
        LocalDateTime arrival = now.plusHours(4);

        return createFlight(flightNumber, origin, destination, departure, arrival);
    }
}
