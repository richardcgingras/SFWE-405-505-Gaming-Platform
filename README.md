# SFWE-405-505-Gaming-Platform
SFWE 405/505 Semester Project

This project is a Spring Boot backend for a gaming platform. It provides APIs for managing users, video games, developers, wishlists, chat messaging, and more. Authentication is implemented using JWT.

## Tech Stack
- Java 21
- Spring Boot
- Spring Data JPA
- Spring Security (JWT)
- H2 Database
- Maven

## Running the Backend
From the `backend` directory: ./mvnw spring-boot:run
Backend runs at: http://localhost:8080

## Database
Uses an H2 in-memory database
H2 Console: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:testdb
Username: sa
Password: (blank)

## Authentication
  ### Login
    curl -X POST http://localhost:8080/api/auth/login \
    -H "Content-Type: application/json" \
    -d '{"username":"johna","password":"password"}'

Example response:
{"accessToken":"eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiIxMDEiLCJpYXQiOjE3NzQ5MTczMzUsImV4cCI6MTc3NTAwMzczNX0.VTnnIUtDnB2i3UgUhzixa2Bv4XToNbso6QRYzUVaKdpOF8CNk3oQ0J_xBq0f08XD","tokenType":"Bearer"}

## Postman Setup
Postman files are located in the postman/ folder:
-SFWE_405_api_testing.postman_collection.json
-SFWE_405_Local_Variables.postman_environment.json

Steps:
-Import both files into Postman
-Select the environment: SFWE_405_Local_Variables
-Run the Auth -> Login request
-Use the token for authenticated requests

## API Coverage
Includes endpoints for:
-Authentication
-Video Games
-User Profiles
-Developers
-Wishlist
-Chat / Messaging
-Orders, Reviews, Shopping Cart, Webstore

See Postman collection for full endpoint details.

## Notes
-Backend must be running before testing APIs
-Some endpoints require existing data (IDs must exist)
-Authentication is required for protected endpoints
-Database resets on restart

## Frontend
Frontend setup is documented separately in the frontend README.

