package com.example.airpot.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Passenger {
    private Long id;
    private String name;
    private SeatAssignment seatAssignment;
}
