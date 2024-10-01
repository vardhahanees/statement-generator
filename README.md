# statement-generator

## Description
The **Statement Generator** provides a service for asynchronously generating account statements by integrating with a mocked Core Banking system. Users can request their account statements by specifying the account number, start date, and end date. The system will notify them once the statement is ready with a download link.

## Technologies Used
- **Spring Boot**: Framework for building the API.
- **JPA (Java Persistence API)**: For data persistence using an in-memory H2 database.
- **CompletableFuture**: Handles asynchronous processing.
- **JUnit**: For unit testing.

## API Endpoints

### **Request Statement**
- **Endpoint**: `/api/v1/statements`
- **Method**: `POST`
- **Request Body**:
  ```json
  {
    "accountNumber": "123456789",  // Account number for the statement request
    "fromDate": "2024-01-01",     // Start date for the statement
    "toDate": "2024-12-31"        // End date for the statement
  }

**Responses:**

- 202 Accepted
- 500 Internal Server Error
   
## **Setup Instructions**

**Clone the Repository:**

git clone https://github.com/vardhahanees/statement-generator.git

cd statement-generator-api

**Build the Project:**

Ensure you have Maven installed. Run the following command to build the project:

./mvnw clean package

**Run the Application:**

Use the following command to start the Spring Boot application:

./mvnw spring-boot:run

**Access the API:**

The API will be available at http://localhost:8080. You can use tools like Postman or cURL to interact with the endpoints.

## Testing Instructions

The project includes unit tests for both the service and controller layers. To run the tests, execute the following command:

./mvnw test

## Notification System

Upon successful generation of a statement, the system simulates a notification by printing a message to the console with a download link.

## Example Usage

Requesting a Statement

curl -X POST http://localhost:8080/api/v1/statements \
-H "Content-Type: application/json" \
-d '{
  "accountNumber": "123456789",
  "fromDate": "2024-01-01",
  "toDate": "2024-12-31"
}'






