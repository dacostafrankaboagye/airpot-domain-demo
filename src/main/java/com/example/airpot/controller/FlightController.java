package com.example.airpot.controller;

import com.example.airpot.domain.Flight;
import com.example.airpot.domain.Passenger;
import com.example.airpot.domain.SeatAssignment;
import com.example.airpot.domainservice.FlightService;
import com.example.airpot.dto.FlightRequest;
import com.example.airpot.dto.PassengerRequest;
import com.example.airpot.factory.FlightFactory;
import com.example.airpot.repository.FlightRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller for managing flight operations.
 * Provides endpoints for creating, retrieving, updating, and deleting flights,
 * as well as managing passengers on flights.
 *
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/flights")
public class FlightController {
    private final FlightService flightService;
    private final FlightFactory flightFactory;
    private final FlightRepository flightRepository;

    /**
     * Creates a new flight.
     * 
     * @param flightRequest the flight details to create
     * @return ResponseEntity containing the created flight with HTTP 201 status
     */
    @PostMapping
    public ResponseEntity<Flight> createFlight(@Valid @RequestBody FlightRequest flightRequest){

        Flight flight = flightFactory.createFlight(
                flightRequest.getFlightNumber(),
                flightRequest.getOrigin(),
                flightRequest.getDestination(),
                flightRequest.getScheduledDeparture(),
                flightRequest.getScheduledArrival()
        );

        Flight savedFlight = flightRepository.save(flight);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFlight);

    }

    /**
     * Retrieves all flights.
     * 
     * @return ResponseEntity containing a list of all flights
     */
    @GetMapping
    public ResponseEntity<List<Flight>> getAllFlights(){
        List<Flight> flights = flightRepository.findAll();
        return ResponseEntity.ok(flights);
    }

    /**
     * Retrieves a specific flight by its flight number.
     * 
     * @param flightNumber the unique flight number
     * @return ResponseEntity containing the flight if found, or 404 if not found
     */
    @GetMapping("/{flightNumber}")
    public ResponseEntity<Flight> getFlightByNumber(@PathVariable String flightNumber){
        Flight flight = flightService.getFlightWithPassengers(flightNumber);
        if(flight != null){
            return ResponseEntity.ok(flight);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Adds a passenger to a specific flight.
     * 
     * @param flightNumber the flight number to add the passenger to
     * @param passengerRequest the passenger details including seat assignment
     * @return ResponseEntity with success message
     */
    @PostMapping("/{flightNumber}/passengers")
    public ResponseEntity<String> addPassengerToFlight(
            @PathVariable String flightNumber,
            @Valid @RequestBody PassengerRequest passengerRequest
            ){
        SeatAssignment seatAssignment = new SeatAssignment(passengerRequest.getSeatNumber(), passengerRequest.getSeatClass());
        Passenger passenger = Passenger.builder()
                .name(passengerRequest.getName())
                .seatAssignment(seatAssignment)
                .build();

        flightService.addPassengerToFlight(flightNumber, passenger);

        return ResponseEntity.ok("Passenger added successfully");

    }

    /**
     * Removes a passenger from a specific flight.
     * 
     * @param flightNumber the flight number to remove the passenger from
     * @param passengerId the unique identifier of the passenger to remove
     * @return ResponseEntity with success message or 404 if passenger not found
     */
    @DeleteMapping("/{flightNumber}/passengers/{passengerId}")
    public ResponseEntity<String> removePassengerFromFlight(
            @PathVariable String flightNumber,
            @PathVariable String passengerId
    ){
        boolean removed = flightService.removePassengerFromFlight(flightNumber, passengerId);
        if (removed) {
            return ResponseEntity.ok("Passenger removed successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Retrieves flights for a specific route.
     * 
     * @param origin the departure location
     * @param destination the arrival location
     * @return ResponseEntity containing a list of flights matching the route
     */
    @GetMapping("/route")
    public ResponseEntity<List<Flight>> getFlightsByRoute(
            @RequestParam String origin, @RequestParam String destination
    ) {
        List<Flight> flights = flightService.findFlightsByRoute(origin, destination);
        return ResponseEntity.ok(flights);
    }

    /**
     * Retrieves flights within a specific departure time range.
     * 
     * @param start the start of the time range (inclusive)
     * @param end the end of the time range (inclusive)
     * @return ResponseEntity containing a list of flights departing within the specified range
     */
    @GetMapping("/departures")
    public ResponseEntity<List<Flight>> getFlightsByDepartureRange(
            @RequestParam LocalDateTime start, @RequestParam LocalDateTime end
    ) {
        List<Flight> flights = flightService.findFlightsByDepartureRange(start, end);
        return ResponseEntity.ok(flights);
    }

    /**
     * Deletes a flight by its flight number.
     * 
     * @param flightNumber the unique flight number to delete
     * @return ResponseEntity with success message or 404 if flight not found
     */
    @DeleteMapping("/{flightNumber}")
    public ResponseEntity<String> deleteFlight(@PathVariable String flightNumber) {
        if (flightService.flightExists(flightNumber)) {
            flightRepository.deleteById(flightNumber);
            return ResponseEntity.ok("Flight deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
