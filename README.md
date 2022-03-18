# Invocing-challenge
API (Java application) that lets you sum invoice documents in different currencies via a provided input file.
The application is based on Spring Boot.
# Usage

## Prerequisites

 - Java 11 or later.
 - Maven 3.8.3 or later.

## How to run the application

To start the application on default port 8089:

```
./mvnw spring-boot:run
```

## Send requests to application

```
curl -vvv -F 'exchangeRates={"EUR": "1.0", "USD": "1.11", "GBP": "0.76"};type=application/json' -F 'targetCurrency="USD";type=application/json' -F 'document=@your-data-file' http://localhost:<port>/sum
```

## Build the application

To build the project from source and run the tests use:

```
./mvnw clean install
```

If you need to change the port please update the applocation.properties and restrat the application