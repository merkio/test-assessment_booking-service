**Prerequisites**

- JDK 21
- IntelliJ (recommended) or your favorite Java IDE
- Recommended: Bash environment with installed 'curl' or Postman for testing

**Preamble**

Your mission would you decide to accept it:

As part of the development team you are creating software to alleviate the
issues from the Sales team. For this, a new requirement has been raised to implement a RESTful web service that stores
_booking_ objects in memory and return information about them.

Note: A _booking_ is a request from any of our customers to acquire one of products.

**Challenge**

The bookings to be stored have the following fields:
- booking_id
- description
- price
- currency
- subscription_start_date
- email
- department

Please define as many departments as you will like, and create a unique method `doBusiness()` for each department. 

Feel free to select the best data type that, in your opinion, would define those fields the best.

You should not use a database or some other sort of persistence but come up with an own data structure to store the
transactions.

In any moment where the implementation implies the usage of an external server (i.e. sending e-mail's) feel free to mock
creatively these external resources, keeping in mind that all real-world use-cases must be covered.


**API Specification:**

**POST /bookingservice/bookings**

Creates a new booking and sends an e-mail with the details.

**PUT /bookingservice/bookings/{booking_id}**

Insert, replace if already exists a booking.

**GET /bookingservice/bookings/{booking_id}**

Returns the specified booking as JSON.

**GET /bookingservice/bookings/department/{department}**

Returns a JSON list of all bookings ids with the given department.

**GET /bookingservice/bookings/currencies**

Returns a JSON list with all used currencies in the existing bookings.

**GET /bookingservice/sum/{currency}**

Returns the sum of all bookings prices with the given currency.

**GET /bookingservice/bookings/dobusiness/{booking_id}**

Returns the result of `doBusiness()` for the given booking corresponding department.


# Solution Project
## Package Structure
- API - contains everything related to the REST API and conversion to the domain objects
- Application - business logic, split into 2 parts booking and department
- Deployment - main class to run and configs
- Domain - business domain object and interfaces to work with repositories, external services, fake implementations etc.
- Repository - repository adapters implementations
## What implemented and what not
### Implemented
- Basic functionality
- Positive cases tests covered controller endpoints, service, in memory repository
- Added OpenAPI dependency to have swagger UI for development purposes
### Not Implemented 
- Tests for negative cases and edge cases
- Security Configuration
- Sending email or store data in persistence storage or database
- Observability (logging, metrics, tracing etc.)
- ...

To run project go to deployment package and start BookingServiceApplication main method.

To have access to the swagger UI open the link in a browser: http://localhost:8080/swagger-ui/index.html
