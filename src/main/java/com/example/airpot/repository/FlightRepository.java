package com.example.airpot.repository;

import com.example.airpot.domain.Flight;

public interface FlightRepository {
    Flight findByFlightNumber(String flightNumber);
    void save(Flight flight);
}
