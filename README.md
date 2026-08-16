# SmartPark

SmartPark is a parking management system built with Spring Boot. It manages parking lots, vehicles, parking sessions, check-ins, check-outs, parking availability, and automatic vehicle checkout.

## Features

- Parking lot registration
- Unique parking lot ID
- Parking capacity management
- Vehicle registration
- Unique license plate validation
- Vehicle types:
  - Car
  - Motorcycle
  - Truck
- Owner name validation
- Vehicle check-in
- Vehicle check-out
- Parking duration calculation
- Parking cost calculation per minute
- Parking availability tracking
- Automatic occupied-space updates
- Automatic checkout after 15 minutes
- View currently checked-in vehicles grouped by parking lot
- H2 database support
- Spring Security and OAuth2 Resource Server support
- DTO validation

## Technologies

- Java
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- OAuth2 Resource Server
- Hibernate
- H2 Database
- Lombok
- Jakarta Bean Validation
- Maven

## Project Structure

```text
src/main/java/com/smartpark
├── config
├── controller
├── dto
├── entity
├── exception
├── mapper
├── repository
├── service
└── SmartparkApplication.java
