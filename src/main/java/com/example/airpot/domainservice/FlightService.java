package com.example.airpot.domainservice;

import com.example.airpot.domain.Flight;
import com.example.airpot.domain.Passenger;
import com.example.airpot.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;

    public void addPassengerToFlight(String flightNumber, Passenger passenger){
        Flight flight = flightRepository.findByFlightNumber(flightNumber);
        boolean seatTaken = flight.getPassengers()
                .stream()
                .anyMatch(p->p.getSeatAssignment().equals(passenger.getSeatAssignment()));
        if(seatTaken) throw new IllegalArgumentException("seat already assigned");
        flight.addPassenger(passenger);
        flightRepository.save(flight);
    }
}
