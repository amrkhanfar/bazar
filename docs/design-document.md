## Overview 
Bazar is a microservice-based online book store system that consists of three main services:

1. Frontend Service: Handles client requests
2. Catalog Service: Manages book inventory
3. Order Service: Proccesses purchase transactions

## How It Works

- **Frontend Service**: Provides API endpoints for consumer, routing requests to required services
- **Catalog Service**: Keeps book inventory database with CRUD operations
- **Order Service**: Proccess orders and keeps order history in a dedicated database

### Communication Flow
1. Client -> Frontend -> Catalog (for searches/info)
2. Client -> Frontend -> Order -> Catalog (for purchase)

### Key Features
- RESTful APIs for all services
- H2 database for data persistence
- Containerized with with Docker
- Service discovery through Docker netwroking

### Data Persistence
- Catalog service uses H2 database at `/data/catalogdb`
- Order service uses H2 database at `/data/orderdb`
- Data persists in Docker volumes

### Technologies
- Spring Boot
- Docker

## Design Tradeoffs

### Chosen Approaches
1. **Seperate databases**: Each service has its own database for independence
2. **Basic error handling**: Global exception handlers using spring `@AdviceController` with custom responses

## Possible Improvements

### Extensions
1. **Authentication**: Add JWT authentication to secure API endpoints
2. **Caching**: Implement a chaching strategy for frequent queries

### Implementation Sketch

- For JWT Authentication:
1. Add spring security and jjwt dependencies
2. Create Authentication service 
3. Create JWT utility class to handle token generation and validation
4. Properly configure spring security to secure the endpoints

- For Chaching (using Redis)
1. Add Redis (for spring boot) dependecy
2. Configure Reddis connection
3. Enable caching by annotating the main class using `@EnableChaching` annotation
4. Add caching to frequent queries and setup cache eviction to clear on upadates. 

## Running the application

### Requirements
- Docker
- Java 17 JDK

### Steps
1. Clone the repository
2. Run `docker-compose build` then `docker-compose up`
3. You can access the services through: 
    - Frontend: http://localhost:8080
    - Catalog: http://localhost:8081
    - Order: http://localhost:8082

    