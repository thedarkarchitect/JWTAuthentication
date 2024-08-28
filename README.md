# Spring Boot JWT Authentication

This project demonstrates a simple JWT (JSON Web Token) authentication implementation using Spring Boot. The application generates two tokens: an access token and a refresh token. The access token is valid for 5 minutes, while the refresh token is valid for 3 days. The tokens include a leeway of 5 minutes to account for differences in server times.

## Features

- **JWT Authentication**: Generate access and refresh tokens on login.
- **Token Expiration**: Access tokens expire in 5 minutes; refresh tokens expire in 3 days.
- **Token Refreshing**: Refresh access tokens using the refresh token.
- **Protected Endpoints**: Access protected resources with a valid access token.
- **In-Memory User Authentication**: Simple in-memory user authentication for demonstration.

## Prerequisites

- Java 17 or higher
- Maven or Gradle
- PostgreSQL (or any other database if configured)

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/your-username/spring-boot-jwt-authentication.git
cd spring-boot-jwt-authentication
```
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/your_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

#Build and run the application
```bash
mvn clean install
mvn spring-boot:run
```
