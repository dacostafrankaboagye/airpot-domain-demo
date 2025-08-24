package com.example.airpot.repository;

import com.example.airpot.domain.Flight;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRepository {
    Flight findByFlightNumber(String flightNumber);
    void save(Flight flight);
}
