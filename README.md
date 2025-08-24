## agrregates and entities
- entity: `Flight` - represents a scheduled flight
- entity: `Passenger` -  represents a traveler
- value object: `SeatAssignment`- represents descriptive attributes without identity. 
       describes a passenger’s seat via seatNumber and class (e.g., economy, business) and
       doesn’t exist standalone without a passenger.
- aggregate root: `Flight` - manages its passengers and their seat assignments