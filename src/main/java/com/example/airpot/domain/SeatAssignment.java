package com.example.airpot.domain;

import lombok.Data;

@Data
public class SeatAssignment {
    private String seatNumber;
    private String seatClass; // e.g. Economy, Business
}
