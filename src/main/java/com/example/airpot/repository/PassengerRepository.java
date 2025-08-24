package com.example.airpot.repository;

import com.example.airpot.domain.Passenger;

public interface PassengerRepository {
    Passenger findById(Long id);
    void save(Passenger passenger);
}
