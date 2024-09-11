# Crewper Order Management System

## Overview

The Crewper Order Management System is a Spring Boot application designed to manage orders. It provides RESTful APIs for creating, updating, retrieving, and paginating orders. The application uses Spring Data JPA for database interactions and Camunda BPM for workflow management.

## Features

- **Create Order**: Start a new order process.
- **Retrieve Order**: Get details of an order by its ID.
- **Update Order Status**: Update the status of an existing order.
- **Paginate Orders**: Retrieve orders with pagination and sorting.

## Technologies Used

- **Java**
- **Spring Boot**
- **Spring Data JPA**
- **Camunda BPM**
- **MySQL**
- **Maven**

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven
- MySQL

### Installation

1. **Clone the repository**:
    ```sh
    git clone https://github.com/ahmedSherif-eng/crewper-order-management.git
    cd crewper-order-management
    ```

2. **Configure the database**:
   Update the `src/main/resources/application.yaml` file with your MySQL database credentials.
    ```yaml
    spring:
      datasource:
        url: jdbc:mysql://localhost:3306/camunda
        username: YOUR_USERNAME
        password: YOUR_PASSWORD
        driver-class-name: com.mysql.cj.jdbc.Driver
      jpa:
        hibernate:
          ddl-auto: update
        show-sql: false
        database-platform: org.hibernate.dialect.MySQLDialect

    camunda.bpm:
      database:
        schema-update: true
      admin-user:
        id: a
        password: a
      generic-properties:
        properties:
          historyTimeToLive: P1D
    ```

3. **Build the project**:
    ```sh
    mvn clean install
    ```

4. **Run the application**:
    ```sh
    mvn spring-boot:run
    ```

## API Endpoints

### Create Order

- **URL**: `/api/orders`
- **Method**: `POST`
- **Request Body**: `OrderDTO`
- **Description**: Starts a new order process.

### Retrieve Order

- **URL**: `/api/orders/{orderId}`
- **Method**: `GET`
- **Description**: Retrieves an order by its ID.

### Update Order Status

- **URL**: `/api/orders/{orderId}/status`
- **Method**: `PUT`
- **Request Params**: `orderStatus`
- **Description**: Updates the status of an existing order.

### Paginate Orders

- **URL**: `/api/orders`
- **Method**: `GET`
- **Request Params**: `page`, `size`, `sortBy`
- **Description**: Retrieves orders with pagination and sorting.
