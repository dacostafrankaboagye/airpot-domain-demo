## Credit
[foojay](https://foojay.io/today/domain-driven-design-in-java-a-practical-guide/)
## agrregates and entities
- entity: `Flight` - represents a scheduled flight
- entity: `Passenger` -  represents a traveler
- value object: `SeatAssignment`- represents descriptive attributes without identity. 
       describes a passenger’s seat via seatNumber and class (e.g., economy, business) and
       doesn’t exist standalone without a passenger.
- aggregate root: `Flight` - manages its passengers and their seat assignments

## implementing
- Another core DDD practice is to allow entities to publish domain events as 
- part of their business logic. In the airport context, 
- when a passenger is added to a flight, you may want to 
- raise an event (like `PassengerAddedEvent`) so the rest of the system 
- can respond (updating manifests, sending notifications, etc.). 
- A typical approach is using Spring’s domain events support.

[./src/main/java/com/example/airpot/domain](./src/main/java/com/example/airpot/domain)

- The separation of entities and value objects helps maintain clarity. 
- Entities have identity (`Flight`, `Passenger`), while value objects describe or 
- detail entities without unique identities (`SeatAssignment`).

## contexts and modularization
-  divide the application into bounded contexts
  - Flight operations: Handles flight scheduling, passenger management, and communications 
  - Passenger services: Manages check-in, boarding, and seat assignments 
  - Ground services: Could involve baggage handling and gate assignments, but omitted here for simplicity


## Repositories, domain services, and factories
- `Repositories`
  - domain model with databases or external systems.
  - `FlightRepository`, `PassengerRepository`
  - Repositories expose aggregate roots and entities for retrieval and persistence, 
  - without exposing database details to domain logic.
  - [./src/main/java/com/example/airpot/repository](./src/main/java/com/example/airpot/repository)
- `Domain services`
  - Domain services encapsulate business logic that involves multiple domain objects or 
  - doesn’t naturally fit in an entity or value object.
  - e.g.  `FlightService` that manages passenger assignments to flights ensuring no double seat bookings
  - [./src/main/java/com/example/airpot/domainservice](./src/main/java/com/example/airpot/domainservice)
- `Factories`
  - Factories create complex aggregate instances while encapsulating creation logic.
