# SFWE-405-505-Gaming-Platform

SFWE 405/505 Semester Project

This project is a full-stack gaming platform built with a Spring Boot backend and React frontend. It provides APIs and UI features for managing users, video games, developers, wishlists, shopping carts, checkout, game libraries, messaging, friend requests, and authentication using JWT.

## Prerequisites

- Java 21 installed
- Maven or Maven wrapper (`./mvnw`)
- Node.js / npm

## Tech Stack

- Java 21
- Spring Boot
- Spring Data JPA
- Spring Security (JWT)
- H2 Database
- Maven
- React / Vite

## Running the Backend
From the `backend` directory:

```bash
./mvnw spring-boot:run
# or
mvn spring-boot:run
```

Backend runs at:
http://localhost:8080

Base API URL:
http://localhost:8080/api

## Running the Frontend

```bash
cd frontend
npm install
npm run dev
```

The application will run at:
http://localhost:5173

## Database

Uses an H2 in-memory database.
H2 Console: http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:testdb

Username: sa
Password: blank


## Account Setup

There are no predefined user accounts in the system.

To use the platform:
1. Create a new account using the signup page (`/signup` in the frontend).
2. Log in using the credentials you created.
3. Use the returned JWT token (automatically handled by the frontend or manually in Postman) for protected requests.

This ensures all features (friend requests, wishlist, cart, etc.) are tied to your created account.

## Postman Setup

Postman files are located in the postman/ folder:

- SFWE_405_api_testing.postman_collection.json
- SFWE_405_Local_Variables.postman_environment.json

### Steps:
1. Import both files into Postman.
2. Select the environment: SFWE_405_Local_Variables.
3. Run the Auth → Login request.
4. Use the returned token for authenticated requests.
5. Test endpoints for users, video games, wishlist, shopping cart, friend requests, messaging, and checkout.

## Features
- User signup, login, and password reset
- JWT-based authentication
- Game store and game detail pages
- Shopping cart and checkout flow
- Wishlist functionality
- Game library and downloads
- Developer publishing and upload pages
- Friend requests, including send, accept, and deny
- Messaging between users
- User profile pages with bio and account information

## API Coverage

Includes endpoints for:
- Authentication
- User Profiles
- Friend Requests
- Video Games
- Developers
- Wishlist
- Shopping Cart
- Checkout / Orders
- Reviews
- Chat / Messaging
- Webstore

See Postman collection for full endpoint details.

## Notes
- Backend must be running before testing APIs.
- Frontend should be running when testing UI features. 
- Some endpoints require existing data, such as valid user IDs or game IDs.
- Authentication is required for protected endpoints.
- The H2 database resets on restart.
