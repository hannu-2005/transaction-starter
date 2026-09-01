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

### Project Structure

The application follows a simple layered architecture to keep responsibilities separated:

* **Controller** – Handles HTTP requests and responses.
* **Service** – Contains transaction business logic and validation rules.
* **Repository** – Handles persistence using Spring Data JPA.
* **Entity/Model** – Represents transaction data.
* **Exception Handling** – Handles expected application errors and returns appropriate HTTP responses.

### Validation Rules

The following validation rules are applied:

* Transaction ID is required and must be unique.
* Customer ID is required and cannot be blank.
* Amount is required and must be greater than 0.
* Currency is required and must be a valid 3-letter uppercase currency code.
* Transaction type is required.
* Supported transaction types are `PAYMENT`, `REFUND`, and `TRANSFER`.
* A newly created transaction must have `PENDING` status.

### Business Rules

* Duplicate transaction IDs are rejected.
* New transactions must start with `PENDING` status.
* Only supported transaction types are accepted.
* Transaction status changes are restricted to valid transitions.
* `COMPLETED` and `FAILED` are terminal statuses.
* Submitting the current status again is allowed.

### Status Transition Rules

The transaction lifecycle is:

```text
PENDING → PROCESSING
PENDING → FAILED
PROCESSING → COMPLETED
PROCESSING → FAILED
```

Transitions outside this flow are rejected as invalid business requests.

### Error Handling

The application handles the following cases:

* Transaction not found
* Duplicate transaction ID
* Invalid input
* Invalid transaction status transition

The API returns:

* `400 BAD REQUEST` for validation and business-rule failures.
* `404 NOT FOUND` when the requested transaction does not exist.

### Testing

Automated tests are included for the main transaction operations and business rules.

The test suite covers:

* Successful transaction creation
* Validation failure
* Duplicate transaction ID
* Transaction not found
* Transaction retrieval
* Status update
* Customer transaction retrieval
* Invalid status transition

The complete test suite is executed using:

```bat
mvnw.cmd clean test
```

REST APIs were also manually verified using Postman.

## AI Usage Disclosure

AI assistance was used during development for technical guidance, debugging, and clarification of Spring Boot, validation, JPA, exception handling, and testing concepts.

The generated suggestions were reviewed against the assignment requirements rather than being used without verification. Implementation decisions were made based on the project requirements, and changes were tested after implementation.

The final application was verified by running the Maven test suite and manually testing the REST endpoints using Postman.

## Known Limitations

* The application uses the H2 database provided by the starter project. As an in-memory database, transaction data is not retained after the application is stopped.
* Authentication and authorization are not implemented because they are outside the scope of the assignment.
* Pagination is not implemented for customer transaction retrieval.

## What I Would Improve With More Time

* Add more REST-level integration tests.
* Add API documentation using OpenAPI/Swagger.
* Improve structured logging and monitoring.
* Add pagination for customer transaction retrieval.
* Introduce a persistent database configuration for a production deployment.


