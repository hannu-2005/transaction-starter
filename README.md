# Transaction Starter Project

This is the starter project for the Customer Transactions exercise.

## Before you start

The first thing you should do after cloning the repository is:

### Linux / macOS

```bash
./mvnw clean test
```

### Windows

```bat
mvnw.cmd clean test
```

The sample test should pass before you begin implementing the exercise.

## What is already provided

* Java 17
* Spring Boot
* Maven wrapper
* Spring Web
* Spring Data JPA
* H2 embedded database
* JUnit / Spring Boot Test
* A sample REST endpoint: `GET /api/sample`
* A sample test that loads the Spring context

## Exercise

Implement these four operations:

1. Create transaction
2. Get transaction
3. Update transaction status
4. Get all transactions for a customer

You may change the surrounding design if you believe your solution is better.

## Transaction fields

Every transaction contains:

* Transaction ID
* Customer ID
* Amount
* Currency
* Transaction Type
* Transaction Status

### Validation rules

Define what makes a transaction valid. At minimum, consider:

* Transaction ID
* Customer ID
* Amount
* Currency
* Transaction type
* Initial status

Also explain any business validation you add beyond the annotations already supplied.

## API Endpoints

### 1. Create Transaction

**POST** `/api/transactions`

Creates a new transaction after validating the request and business rules.

**Example Request:**

```json
{
  "transactionId": "TXN1001",
  "customerId": "CUST101",
  "amount": 1250.50,
  "currency": "INR",
  "transactionType": "PAYMENT",
  "transactionStatus": "PENDING"
}
```

### 2. Get Transaction

**GET** `/api/transactions/{transactionId}`

Retrieves a transaction using its transaction ID.

**Example Request:**

```text
GET /api/transactions/TXN1001
```

**Example Response:**

```json
{
  "transactionId": "TXN1001",
  "customerId": "CUST101",
  "amount": 1250.50,
  "currency": "INR",
  "transactionType": "PAYMENT",
  "transactionStatus": "PENDING"
}
```

### 3. Update Transaction Status

**PATCH** `/api/transactions/{transactionId}/status`

Updates the transaction status while enforcing the allowed status transitions.

**Example Request:**

```text
PATCH /api/transactions/TXN1001/status
```

```json
{
  "transactionStatus": "PROCESSING"
}
```

Example status flow:

```text
PENDING → PROCESSING
PENDING → FAILED
PROCESSING → COMPLETED
PROCESSING → FAILED
```

`COMPLETED` and `FAILED` are terminal statuses and cannot be changed to another status.

Submitting the same status again is allowed.

### 4. Get Customer Transactions

**GET** `/api/transactions/customer/{customerId}`

Retrieves all transactions associated with a customer.

**Example Request:**

```text
GET /api/transactions/customer/CUST101
```

**Example Response:**

```json
[
  {
    "transactionId": "TXN1001",
    "customerId": "CUST101",
    "amount": 1250.50,
    "currency": "INR",
    "transactionType": "PAYMENT",
    "transactionStatus": "PENDING"
  }
]
```

## Testing Expectations

Add at least four meaningful tests.

The tests should cover more than just application startup.

The test suite covers transaction creation, retrieval, status updates, customer transaction retrieval, and business-rule validation.

## Implementation Details

### Validation Rules

The following validation rules are implemented:

* Transaction ID is required and must be unique.
* Customer ID is required and cannot be blank.
* Amount is required and must be greater than 0.
* Currency is required and must be a 3-letter uppercase code such as INR, USD, or EUR.
* Transaction type is required.
* Supported transaction types are:

  * PAYMENT
  * REFUND
  * TRANSFER
* A newly created transaction must have the initial status `PENDING`.

### Business Validation

In addition to the validation annotations, the following business rules are applied:

* Duplicate transaction IDs are rejected.
* New transactions must start with `PENDING` status.
* Only PAYMENT, REFUND, and TRANSFER transaction types are accepted.
* Invalid status transitions are rejected.

## Status Transition Rules

The transaction status follows this lifecycle:

```text
PENDING → PROCESSING
PENDING → FAILED
PROCESSING → COMPLETED
PROCESSING → FAILED
```

`COMPLETED` and `FAILED` are terminal statuses and cannot be changed to another status.

Submitting the same status again is allowed.

## Implemented API Endpoints

* POST `/api/transactions` - Create transaction
* GET `/api/transactions/{transactionId}` - Get transaction
* PATCH `/api/transactions/{transactionId}/status` - Update transaction status
* GET `/api/transactions/customer/{customerId}` - Get all transactions for a customer

## Error Handling

The application provides centralized error handling for:

* Transaction not found
* Invalid status transitions
* Validation errors
* Duplicate transaction IDs

HTTP responses:

* `404 NOT FOUND` - Transaction not found
* `400 BAD REQUEST` - Validation or business-rule error

## AI Assistance Disclosure

AI tools were used selectively during development for technical guidance, debugging, and clarification of implementation and testing concepts.

AI assistance was used selectively for technical guidance and to clarify specific implementation details, including Spring Boot REST API design, validation, exception handling, JPA repository usage, status transition logic, and testing approaches. 
The suggestions were reviewed against the assignment requirements, and the final implementation, business rules, and design decisions were independently evaluated and tested.

The suggested solutions were reviewed and adapted to fit the requirements of this assignment. I made the final implementation decisions and corrected or adjusted suggestions where necessary.

The application was verified by running the complete Maven test suite and manually testing the REST APIs using Postman. The final test run completed successfully with all tests passing.



## Known Limitations

- The application uses an in-memory H2 database, so data is lost when the application stops.
- Authentication and authorization are not implemented because they are outside the assignment scope.
- Pagination is not implemented for customer transaction retrieval.


## What I Would Improve With More Time

- Add more integration tests for the REST APIs.
- Add API documentation using OpenAPI/Swagger.
- Add authentication and authorization.
- Add structured logging and monitoring.
- For production deployment, consider using a persistent database such as MySQL instead of the current in-memory H2 database.