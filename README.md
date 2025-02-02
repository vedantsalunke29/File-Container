# File Container Microservices Backend

## Overview
This project is a **Spring Boot microservices-based file container backend** that provides secure file storage and management. It includes authentication, API gateway protection, resilience mechanisms, and monitoring capabilities. Users can register, log in, and securely upload and retrieve files.

## Features
- **JWT Authentication (HS512)**: Secure user authentication and authorization.
- **API Gateway with Fallback Route**: Routes requests and verifies JWT tokens before accessing services.
- **Config Server**: Centralized configuration management.
- **Discovery Server**: Service discovery using Eureka.
- **Image Container Service**: Stores file metadata and communicates with the storage service using **RestTemplate**.
- **Image Storage Service**: Stores files and serves them when requested.
- **Resilience4j**: Provides circuit breaker and fault tolerance mechanisms.
- **Prometheus Monitoring**: Enables performance monitoring.
- **Docker & Docker Compose**: Simplified containerized deployment.

 

## Microservices Architecture
### 1. **API Gateway**
- Routes requests to appropriate services.
- Verifies JWT tokens before forwarding requests.
- Implements **Fallback Routes** for better resiliency.

### 2. **Auth Service**
- Handles user registration and login.
- Generates JWT tokens using **HS512** algorithm.

### 3. **Config Server**
- Provides configuration to all services dynamically.

### 4. **Discovery Server**
- Registers and discovers microservices using **Eureka**.

### 5. **Image Container Service**
- Stores metadata of uploaded files.
- Calls **Image Storage Service** for file storage and retrieval using **RestTemplate** instead of OpenFeign.

### 6. **Image Storage Service**
- Stores actual files.
- Provides file data to **Image Container Service**.

## Security & Resilience
- **JWT-based Authentication**: Secure access control using JSON Web Tokens.
- **Resilience4j**: Implements circuit breakers and retries to improve fault tolerance.
- **API Gateway Protection**: Ensures only authenticated users access the services.

## Monitoring
- **Prometheus**: Captures metrics for performance monitoring.
- **Spring Actuator**: Provides health and metrics endpoints.

## Deployment with Docker Compose
```sh
docker-compose up -d
```
This command will spin up all the services in containers.


## Technologies Used
- **Spring Boot** (Microservices, Security, Data JPA)
- **Spring Cloud** (Config Server, Eureka, Gateway)
- **JWT** (Authentication & Authorization)
- **Resilience4j** (Fault tolerance)
- **RestTemplate** (Inter-service communication instead of OpenFeign)
- **Prometheus** (Monitoring)
- **Docker & Docker Compose** (Containerization)

## Future Improvements
- Implement file versioning.
- Add user role-based access control.
- Integrate Grafana for better visualization of Prometheus metrics.

## Known Facts
- **RestTemplate is newer**: Unlike OpenFeign, RestTemplate provides more flexibility and better control over REST calls, making it a preferred choice in some cases for microservices.
- **API Gateway Fallback**: Using fallback mechanisms helps improve service resilience by gracefully handling failures.
- **Prometheus Integration**: Helps in capturing and monitoring service metrics effectively.

## Author
**Vedant Salunke**

