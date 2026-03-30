# Postman Collection

This folder contains the Postman collection and environment for testing the Gaming Platform APIs.

---

## Importing into Postman

1. Open Postman
2. Click **Import**
3. Import BOTH files:
   - `SFWE_405_api_testing.postman_collection.json`
   - `SFWE_405_Local_Variables.json`
4. In Postman, select the environment:
   - Top right dropdown → choose **SFWE_405_Local_Variables**

---

## Environment Setup (No Manual Entry Required)

The environment file already includes all required variable values.

Once imported and selected, the requests will work without needing to manually enter any values.

---

## Variables Included

| Variable     | Value                     |
|--------------|---------------------------|
| baseUrl      | http://localhost:8080     |
| api          | /api                      |
| id           | 1                         |
| developerId  | 1                         |
| gameId       | 1                         |
| userId       | 1                         |
| cartId       | 1                         |
| rating       | 5                         |
| comment      | Great game!               |

---

## Notes

- The backend server must be running before sending requests.
- Default base URL assumes local development: http://localhost:8080

- Some endpoints depend on existing data (e.g., gameId, userId), so certain requests may require creating records first.

---

## Covered APIs

The collection includes endpoints for:

- Video Games
- User Profiles
- User Types
- Orders
- Category
- Chat
- Developers
- Game Library
- Reviews
- Shopping Cart
- Webstore