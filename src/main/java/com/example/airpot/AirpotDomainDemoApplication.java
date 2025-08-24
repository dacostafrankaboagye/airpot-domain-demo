package com.example.airpot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing // for auditing callbacks to fire.
public class AirpotDomainDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AirpotDomainDemoApplication.class, args);
    }

}
