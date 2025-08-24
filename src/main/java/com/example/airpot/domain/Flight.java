package com.example.airpot.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Flight {
    private String flightNumber;
    private String origin;
    private String destination;
    private LocalDateTime scheduledDeparture;
    private LocalDateTime scheduledArrival;
    private List<Passenger> passengers = new ArrayList<>();

    private final List<Object> events  = new ArrayList<>();

    @DomainEvents
    Collection<Object> domainEvents(){
        return events;
    }

    @AfterDomainEventPublication
    void clearDomainEvents(){
        events.clear();
    }

    public void addPassenger(Passenger passenger){
        this.passengers.add(passenger);
    }


}
