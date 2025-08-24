package com.example.airpot.factory;

import com.example.airpot.domain.Flight;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class FlightFactory {

    public Flight createFlight(
            String flightNumber, String origin, String destination,
            LocalDateTime departure, LocalDateTime arrival
    ){

        return Flight.builder()
                .flightNumber(flightNumber)
                .origin(origin)
                .destination(destination)
                .scheduledDeparture(departure)
                .scheduledArrival(arrival)
                .build();

    }
}
