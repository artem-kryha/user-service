# User Service Application

This project is a Spring Boot application that manages user data using PostgreSQL and MySQL databases. The application supports filtering users and demonstrates integration testing with Testcontainers.

---

## Features
- REST API for user management
- Database integration with PostgreSQL and MySQL
- Filtering users by parameters like `username`, `first name`, and `last name`
- Comprehensive integration tests using Testcontainers
- Docker Compose setup for running the application locally

---

## Prerequisites
- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)
- Java 17 or higher
- Maven 3.6 or higher

---

## Getting Started

### 1. Clone the Repository
```bash
git clone git@github.com:artem-kryha/user-service.git
cd user-service

mvn clean package
docker-compose up --build
