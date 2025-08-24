package com.example.airpot.controller;

import com.example.airpot.domain.Flight;
import com.example.airpot.domain.Passenger;
import com.example.airpot.domainservice.FlightService;
import com.example.airpot.dto.FlightRequest;
import com.example.airpot.factory.FlightFactory;
import com.example.airpot.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class FlightController {
    private final FlightService flightService;
    private final FlightFactory flightFactory;
    private final FlightRepository flightRepository;

    @PostMapping("/flights")
    public ResponseEntity<Flight> createFlight(@RequestBody FlightRequest flightRequest){

        Flight flight = flightFactory.createFlight(
                flightRequest.getFlightNumber(),
                flightRequest.getOrigin(),
                flightRequest.getDestination(),
                flightRequest.getScheduledDeparture(),
                flightRequest.getScheduledArrival()
        );

        flightRepository.save(flight);
        return ResponseEntity.ok(flight);

    }

    @PostMapping("/flights/{flightNumber}/passengers")
    public ResponseEntity<String> addPassengerToFlight(@PathVariable String flightNumber, @RequestBody Passenger passenger){
        try{
            flightService.addPassengerToFlight(flightNumber, passenger);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("Passenger added to flight successfully");

        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }

}
