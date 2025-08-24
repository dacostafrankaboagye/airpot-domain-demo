package com.example.airpot.config;

import com.example.airpot.domain.Flight;
import com.example.airpot.domain.Passenger;
import com.example.airpot.domain.SeatAssignment;

import com.example.airpot.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Component responsible for initializing sample data in the database on application startup.
 * Only runs when the "test" profile is not active to avoid interfering with test data.
 *
 */
@Component
@RequiredArgsConstructor
@Profile("!test")  // this bean will NOT load if the "test" profile is active
public class DataInitializer implements CommandLineRunner{

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);
    private final FlightRepository flightRepository;

    /**
     * Executes on application startup to initialize sample data if the database is empty.
     * 
     * @param args command line arguments passed to the application
     * @throws Exception if an error occurs during data initialization
     */
    @Override
    public void run(String... args) throws Exception{
        if(flightRepository.count() == 0){
            log.info("Initializing data...");
            initializeSampleData();
            log.info("Sample data initialized successfully");
        }
    }

    /**
     * Creates and saves sample flight data with passengers and seat assignments.
     * Initializes three sample flights with different routes and passenger configurations.
     */
    private void initializeSampleData() {
        // create sample flights
        LocalDateTime now = LocalDateTime.now();


        // Flight 1: JFK to LAX
        Passenger johnDoe = Passenger.builder()
                .name("John Doe")
                .seatAssignment(new SeatAssignment("12A", "Economy"))
                .build();

        Passenger janeSmith = Passenger.builder()
                .name("Jane Smith")
                .seatAssignment( new SeatAssignment("12B", "Economy"))
                .build();


        Flight flight1 = Flight.builder()
                .flightNumber("UA101")
                .origin("JFK")
                .destination("LAX")
                .scheduledDeparture(now.plusHours(2))
                .scheduledArrival(now.plusHours(6))
                .passengers(
                        new ArrayList<>(List.of(johnDoe, janeSmith))
                )
                .build();


        // Flight 2: LAX to JFK
        Passenger aliceJohnson = Passenger.builder()
                .name("Alice Johnson")
                .seatAssignment(new SeatAssignment("15A", "Business"))
                .build();
        Passenger bobBrown = Passenger.builder()
                .name("Bob Brown")
                .seatAssignment(new SeatAssignment("15B", "Business"))
                .build();
        Flight flight2 = Flight.builder()
                .flightNumber("UA202")
                .origin("LAX")
                .destination("JFK")
                .scheduledDeparture(now.plusHours(4))
                .scheduledArrival(now.plusHours(8))
                .passengers(
                        new ArrayList<>(List.of(aliceJohnson, bobBrown))
                )
                .build();

        // Flight 3: ORD to MIA
        Passenger charlieDavis = Passenger.builder()
                .name("Charlie Davis")
                .seatAssignment(new SeatAssignment("18A", "First Class"))
                .build();
        Passenger dianaWhite = Passenger.builder()
                .name("Diana White")
                .seatAssignment(new SeatAssignment("18B", "First Class"))
                .build();
        Flight flight3 = Flight.builder()
                .flightNumber("AA303")
                .origin("ORD")
                .destination("MIA")
                .scheduledDeparture(now.plusHours(6))
                .scheduledArrival(now.plusHours(10))
                .passengers(
                        new ArrayList<>(List.of(charlieDavis, dianaWhite))
                )
                .build();

        flightRepository.saveAll(List.of(flight1, flight2, flight3));
        log.info("Created {} sample flights", flightRepository.count());

    }
}
