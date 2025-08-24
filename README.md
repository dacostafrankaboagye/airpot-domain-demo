## Inspiration For the Project
[foojay's article on DDD in java](https://foojay.io/)

## System Architecture Overview

```mermaid
graph TB
    %% External Layer
    Client[Client Applications]
    
    %% Application Layer
    subgraph "Application Layer"
        FC[FlightController]
        GEH[GlobalExceptionHandler]
        DI[DataInitializer]
    end
    
    %% Domain Layer
    subgraph "Domain Layer"
        subgraph "Domain Services"
            FS[FlightService]
        end
        
        subgraph "Domain Entities"
            F[Flight]
            P[Passenger]
        end
        
        subgraph "Value Objects"
            SA[SeatAssignment]
        end
        
        subgraph "Factories"
            FF[FlightFactory]
        end
        
        subgraph "Domain Events"
            DE[Domain Events]
        end
    end
    
    %% Infrastructure Layer
    subgraph "Infrastructure Layer"
        subgraph "Repositories"
            FR[FlightRepository]
            PR[PassengerRepository]
        end
        
        subgraph "Database"
            MongoDB[(MongoDB)]
        end
    end
    
    %% Application Support
    subgraph "Application Support"
        subgraph "DTOs"
            FReq[FlightRequest]
            PReq[PassengerRequest]
        end
    end
    
    %% Connections
    Client --> FC
    FC --> FS
    FC --> FF
    FC --> FR
    FC --> FReq
    FC --> PReq
    
    FS --> FR
    FS --> PR
    FS --> F
    FS --> P
    
    FF --> F
    
    F --> P
    F --> SA
    F --> DE
    P --> SA
    
    FR --> MongoDB
    PR --> MongoDB
    
    FC --> GEH
    DI --> FR
    DI --> FF
    
    %% Styling
    classDef domainEntity fill:#e1f5fe
    classDef domainService fill:#f3e5f5
    classDef valueObject fill:#e8f5e8
    classDef repository fill:#fff3e0
    classDef controller fill:#fce4ec
    classDef dto fill:#f1f8e9
    
    class F,P domainEntity
    class FS domainService
    class SA valueObject
    class FR,PR repository
    class FC controller
    class FReq,PReq dto
```

## Component Interaction Flow

```mermaid
sequenceDiagram
    participant Client
    participant FlightController
    participant FlightService
    participant FlightFactory
    participant FlightRepository
    participant Flight
    participant Passenger
    participant MongoDB
    
    %% Create Flight Flow
    Client->>FlightController: POST /api/flights
    FlightController->>FlightFactory: createFlight()
    FlightFactory->>Flight: new Flight()
    Flight-->>FlightFactory: flight instance
    FlightFactory-->>FlightController: flight
    FlightController->>FlightRepository: save(flight)
    FlightRepository->>MongoDB: persist flight
    MongoDB-->>FlightRepository: saved flight
    FlightRepository-->>FlightController: saved flight
    FlightController-->>Client: HTTP 201 Created
    
    %% Add Passenger Flow
    Client->>FlightController: POST /api/flights/{flightNumber}/passengers
    FlightController->>FlightService: addPassengerToFlight()
    FlightService->>FlightRepository: findByFlightNumber()
    FlightRepository->>MongoDB: query flight
    MongoDB-->>FlightRepository: flight data
    FlightRepository-->>FlightService: flight
    FlightService->>Flight: addPassenger()
    Flight->>Flight: validate seat availability
    Flight-->>FlightService: passenger added
    FlightService->>FlightRepository: save(flight)
    FlightRepository->>MongoDB: update flight
    FlightService-->>FlightController: success
    FlightController-->>Client: HTTP 200 OK
```

## Domain Model Relationships

```mermaid
erDiagram
    Flight ||--o{ Passenger : contains
    Passenger ||--|| SeatAssignment : has
    
    Flight {
        string id PK
        string flightNumber UK
        string origin
        string destination
        datetime scheduledDeparture
        datetime scheduledArrival
        datetime createdAt
        datetime lastModifiedAt
    }
    
    Passenger {
        string id PK
        string name
        datetime createdAt
        datetime lastModifiedAt
    }
    
    SeatAssignment {
        string seatNumber
        string seatClass
    }
```

## DDD Layers and Responsibilities

### 1. **Domain Layer** (Core Business Logic)
- **Entities**: `Flight`, `Passenger` - Rich domain objects with business logic
- **Value Objects**: `SeatAssignment` - Immutable objects representing concepts
- **Domain Services**: `FlightService` - Complex business operations spanning multiple entities
- **Factories**: `FlightFactory` - Object creation with business rules
- **Domain Events**: Built into Flight entity for integration

### 2. **Application Layer** (Orchestration)
- **Controllers**: `FlightController` - REST API endpoints and request handling
- **DTOs**: `FlightRequest`, `PassengerRequest` - Data transfer objects
- **Exception Handling**: `GlobalExceptionHandler` - Centralized error handling

### 3. **Infrastructure Layer** (Technical Concerns)
- **Repositories**: `FlightRepository`, `PassengerRepository` - Data persistence abstraction
- **Database**: MongoDB - Data storage
- **Configuration**: `DataInitializer` - Application setup




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

![./images/entities.png](./images/entities.png)

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
  - [./src/main/java/com/example/airpot/factory](./src/main/java/com/example/airpot/factory)

## Application layer and integration
- a simple REST controller integrated with your services, 
- exposing the core functionality to clients
- The API exposes aggregate-root operations and domain behavior aligned with business processes.
- [./src/main/java/com/example/airpot/controller](./src/main/java/com/example/airpot/controller)

## Testing and evolving the model
- core scenarios, like flight creation, adding passengers, etc.


## Endpoints
-  base url = `http://localhost:8090/api/flights`
- `get all flights`
```bash
 curl -X GET --location "http://localhost/api/flights"
```
- sampleOutput
  - [./sampleOutputs/getAllFlights.json](./sampleOutputs/getAllFlights.json)

- `Creates a new flight`
```bash
curl -X POST --location "http://localhost/api/flights" \
    -H "Content-Type: application/json" \
    -d '{
          "flightNumber": "MX90",
          "origin": "SSU",
          "destination": "PPM",
          "scheduledDeparture": "2025-12-25T10:00:00",
          "scheduledArrival": "2025-12-25T13:00:00"
        }'
```
- sampleOutput
  - [./sampleOutputs/createFlight.json](./sampleOutputs/createFlight.json)

- `Retrieves flights within a specific departure time range`
```bash
curl -X GET --location "http://localhost:8090/api/flights/departures?start=2025-08-24T14:00:00&end=2025-08-24T23:59:59"
```
- sampleOutput
  - [./sampleOutputs/specificdeparturerange.json](./sampleOutputs/specificdeparturerange.json)

- `Retrieves flights for a specific route`
```bash
curl -X GET --location "http://localhost:8090/api/flights/route?origin=JFK&destination=LAX"
```
- sampleOutput
  - [./sampleOutputs/specificLocation.json](./sampleOutputs/specificLocation.json)

- `Retrieve by Flight Number`
```bash
curl -X GET --location "http://localhost:8090/api/flights/UA101"
```
- sampleOutput
  - [./sampleOutputs/byFlightNumber.json](./sampleOutputs/byFlightNumber.json)

- `delete by flight number`
```bash
curl -X DELETE --location "http://localhost:8090/api/flights/UIC"
```
- sampleOutput
```text
Flight deleted successfully
```

- `Add a passenger to a specific flight`
```bash
curl -X POST --location "http://localhost:8090/api/flights/UIC/passengers" \
    -H "Content-Type: application/json" \
    -d '{
          "name": "Max J Smith",
          "seatNumber": "23A",
          "seatClass": "First Class"
        }'
```
- sampleOutput
```text
Passenger added successfully
```

- `Remove a passenger from a specific flight`
```bash
curl -X DELETE --location "http://localhost:8090/api/flights/UIC/passengers/68ab31ff565d959f4cdf06d6"
```
- sampleOutput
```text
Passenger removed successfully
```
