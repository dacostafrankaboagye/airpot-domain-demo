## Credit
![foojay](https://foojay.io/today/domain-driven-design-in-java-a-practical-guide/)
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