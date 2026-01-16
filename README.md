# Task Management API

[![Java](https://img.shields.io/badge/Java-17-blue.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.1-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

A production-ready Task Management REST API built with Spring Boot 3.2, demonstrating enterprise-level Java development practices, security implementation, and modern architectural patterns.

---

## 📑 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Technology Stack](#-technology-stack)
- [Prerequisites](#-prerequisites)
- [Setup Instructions](#-setup-instructions)
- [API Documentation](#-api-documentation)
- [Example API Requests](#-example-api-requests)
- [Configuration](#-configuration)
- [Testing](#-testing)
- [Database](#-database)
- [Architecture](#-architecture)
- [Troubleshooting](#-troubleshooting)
- [License](#-license)

---

## 🎯 Overview

The Task Management API is a comprehensive RESTful service that provides a robust backend solution for task management applications. Built with enterprise-grade standards, it features JWT-based authentication, role-based authorization, advanced filtering capabilities, and complete CRUD operations for task management.

This project demonstrates:
- Clean architecture with layered separation of concerns
- Production-ready security implementation
- Comprehensive error handling and validation
- API documentation with OpenAPI/Swagger
- Efficient caching strategies
- Asynchronous email notifications
- Full test coverage with unit and integration tests

---

## ✨ Features

### Authentication & Security
- ✅ **JWT Authentication** - Secure token-based authentication with access and refresh tokens
- ✅ **Role-Based Access Control (RBAC)** - USER and ADMIN role management
- ✅ **Password Encryption** - BCrypt hashing for secure password storage
- ✅ **Token Refresh Mechanism** - Seamless token renewal without re-authentication

### Task Management
- ✅ **Full CRUD Operations** - Create, Read, Update, Delete tasks
- ✅ **Advanced Filtering** - Filter by status, priority, assigned user, and search text
- ✅ **Pagination & Sorting** - Efficient data retrieval with customizable page size and sorting
- ✅ **Status Management** - Track tasks through TODO, IN_PROGRESS, DONE, CANCELLED states
- ✅ **Priority Levels** - Organize tasks by LOW, MEDIUM, HIGH, URGENT priorities
- ✅ **Task Assignment** - Assign tasks to specific users

### Technical Features
- ✅ **RESTful API Design** - Standard HTTP methods and status codes
- ✅ **API Documentation** - Interactive Swagger/OpenAPI documentation
- ✅ **Caching Strategy** - Caffeine cache for improved performance
- ✅ **Async Operations** - Non-blocking email notifications
- ✅ **Audit Trail** - Automatic tracking of created/updated timestamps
- ✅ **Global Exception Handling** - Consistent error responses
- ✅ **Input Validation** - Request validation with detailed error messages
- ✅ **Docker Support** - Containerized deployment with Docker Compose
- ✅ **Health Checks** - Actuator endpoints for monitoring
- ✅ **Profile-Based Configuration** - Separate configurations for dev, test, and prod

---

## 🛠 Technology Stack

### Core Framework
- **Java 17** - LTS version with modern language features
- **Spring Boot 3.2.1** - Latest Spring Boot framework
- **Spring Framework 6** - Core Spring components

### Spring Ecosystem
- **Spring Web** - RESTful web services
- **Spring Security 6** - Authentication and authorization
- **Spring Data JPA** - Database abstraction layer
- **Spring Validation** - Request validation
- **Spring Cache** - Caching abstraction
- **Spring Mail** - Email functionality
- **Spring Actuator** - Health monitoring and metrics

### Security & Authentication
- **JJWT 0.12.3** - JSON Web Token implementation
- **BCrypt** - Password hashing algorithm

### Database
- **PostgreSQL 15** - Production database
- **H2 Database** - In-memory database for development and testing
- **Hibernate** - JPA implementation

### Development Tools
- **Lombok 1.18.30** - Reduce boilerplate code
- **MapStruct 1.5.5** - Type-safe bean mapping
- **SpringDoc OpenAPI 2.3.0** - API documentation

### Caching
- **Caffeine** - High-performance caching library

### Build & Deployment
- **Maven 3.8+** - Dependency management and build tool
- **Docker** - Containerization
- **Docker Compose** - Multi-container orchestration

### Testing
- **JUnit 5** - Unit testing framework
- **Mockito** - Mocking framework
- **Spring Security Test** - Security testing utilities
- **Spring Boot Test** - Integration testing support

---

## 📋 Prerequisites

Before you begin, ensure you have the following installed:

- **Java Development Kit (JDK) 17 or higher**
  ```bash
  java -version
  ```

- **Apache Maven 3.8 or higher**
  ```bash
  mvn -version
  ```

- **Docker and Docker Compose** (optional, for containerized deployment)
  ```bash
  docker --version
  docker-compose --version
  ```

- **PostgreSQL 15** (optional, if not using Docker)
  - Only required if running locally without Docker
  - Default credentials: username=`admin`, password=`admin123`, database=`taskmanager`

---

## 🚀 Setup Instructions

### 1. Clone the Repository

```bash
git clone <repository-url>
cd javaapi
```

### 2. Configure Application Properties

The application uses profile-based configuration. The default profile is `dev`.

#### Development Profile (application-dev.yml)
Uses H2 in-memory database - no additional configuration needed.

#### Production Profile (application-prod.yml)
Update database credentials if needed:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/taskmanager
    username: admin
    password: admin123
```

#### JWT Configuration (application.yml)
**IMPORTANT**: Change the JWT secret for production:

```yaml
jwt:
  secret: your-secret-key-change-this-in-production-min-256-bits
  expiration: 900000  # 15 minutes
  refresh-expiration: 604800000  # 7 days
```

### 3. Build the Project

```bash
mvn clean install
```

This will:
- Compile the source code
- Run all tests
- Package the application as a JAR file

### 4. Run Locally

#### Option A: Using Maven (Development Mode)

```bash
mvn spring-boot:run
```

This runs the application with the `dev` profile using H2 database.

#### Option B: Using Java JAR (Production Mode)

```bash
# Build the JAR
mvn clean package -DskipTests

# Run with production profile
java -jar target/task-manager-api-1.0.0.jar --spring.profiles.active=prod
```

**Note**: Ensure PostgreSQL is running before starting in production mode.

### 5. Run with Docker

#### Option A: Docker Compose (Recommended)

Start both PostgreSQL and the application:

```bash
# Build and start all services
docker-compose up --build

# Run in detached mode
docker-compose up -d --build

# View logs
docker-compose logs -f app

# Stop all services
docker-compose down

# Stop and remove volumes
docker-compose down -v
```

#### Option B: Docker Only

```bash
# Build the application first
mvn clean package -DskipTests

# Build Docker image
docker build -t task-manager-api .

# Run container (requires PostgreSQL running)
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/taskmanager \
  -e SPRING_DATASOURCE_USERNAME=admin \
  -e SPRING_DATASOURCE_PASSWORD=admin123 \
  task-manager-api
```

### 6. Verify Installation

Once the application is running, verify it's working:

```bash
# Check health endpoint
curl http://localhost:8080/actuator/health

# Expected response
{"status":"UP"}
```

Access the application:
- **API Base URL**: http://localhost:8080/api/v1
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/v3/api-docs
- **H2 Console** (dev profile only): http://localhost:8080/h2-console

---

## 📚 API Documentation

### Interactive Documentation

Once the application is running, access the **Swagger UI** for interactive API documentation:

🔗 **http://localhost:8080/swagger-ui.html**

### OpenAPI Specification

The OpenAPI 3.0 specification is available at:

🔗 **http://localhost:8080/v3/api-docs**

### API Endpoints Summary

#### Authentication Endpoints (`/api/v1/auth`)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/register` | Register a new user | ❌ |
| POST | `/login` | Login with credentials | ❌ |
| POST | `/refresh` | Refresh access token | ✅ |
| POST | `/logout` | Logout current user | ✅ |

#### Task Endpoints (`/api/v1/tasks`)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/` | Create a new task | ✅ |
| GET | `/` | Get all tasks (with filters) | ✅ |
| GET | `/{id}` | Get task by ID | ✅ |
| PUT | `/{id}` | Update task | ✅ |
| DELETE | `/{id}` | Delete task | ✅ |
| PATCH | `/{id}/status` | Update task status | ✅ |
| GET | `/user/{userId}` | Get tasks by user | ✅ |

#### User Endpoints (`/api/v1/users`)

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/` | Get all users | ✅ (ADMIN) |
| GET | `/{id}` | Get user by ID | ✅ |
| GET | `/me` | Get current user | ✅ |

### Query Parameters

#### Task Filtering (`GET /api/v1/tasks`)

- `status` - Filter by status: TODO, IN_PROGRESS, DONE, CANCELLED
- `priority` - Filter by priority: LOW, MEDIUM, HIGH, URGENT
- `assignedToUserId` - Filter by assigned user ID
- `search` - Search in title and description
- `page` - Page number (default: 0)
- `size` - Page size (default: 10)
- `sortBy` - Sort field (default: createdAt)
- `sortDir` - Sort direction: asc, desc (default: desc)

---

## 💻 Example API Requests

### 1. Register a New User

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john.doe@example.com",
    "password": "SecurePass123!",
    "firstName": "John",
    "lastName": "Doe"
  }'
```

**Response:**
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "expiresIn": 900000
  },
  "timestamp": "2024-01-16T10:30:00"
}
```

### 2. Login

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@example.com",
    "password": "SecurePass123!"
  }'
```

**Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "expiresIn": 900000
  },
  "timestamp": "2024-01-16T10:35:00"
}
```

### 3. Create a Task

```bash
curl -X POST http://localhost:8080/api/v1/tasks \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
  -d '{
    "title": "Implement user authentication",
    "description": "Add JWT-based authentication to the API",
    "status": "TODO",
    "priority": "HIGH",
    "dueDate": "2024-02-01T00:00:00",
    "assignedToUserId": 1
  }'
```

**Response:**
```json
{
  "success": true,
  "message": "Task created successfully",
  "data": {
    "id": 1,
    "title": "Implement user authentication",
    "description": "Add JWT-based authentication to the API",
    "status": "TODO",
    "priority": "HIGH",
    "dueDate": "2024-02-01T00:00:00",
    "assignedToUserId": 1,
    "createdAt": "2024-01-16T10:40:00",
    "updatedAt": "2024-01-16T10:40:00"
  },
  "timestamp": "2024-01-16T10:40:00"
}
```

### 4. Get Tasks with Filtering

```bash
# Get all high priority tasks that are in progress
curl -X GET "http://localhost:8080/api/v1/tasks?status=IN_PROGRESS&priority=HIGH&page=0&size=10&sortBy=createdAt&sortDir=desc" \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"

# Search tasks by keyword
curl -X GET "http://localhost:8080/api/v1/tasks?search=authentication&page=0&size=10" \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"

# Get tasks assigned to a specific user
curl -X GET "http://localhost:8080/api/v1/tasks?assignedToUserId=1" \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

**Response:**
```json
{
  "success": true,
  "message": "Tasks retrieved successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "title": "Implement user authentication",
        "description": "Add JWT-based authentication to the API",
        "status": "IN_PROGRESS",
        "priority": "HIGH",
        "dueDate": "2024-02-01T00:00:00",
        "assignedToUserId": 1,
        "createdAt": "2024-01-16T10:40:00",
        "updatedAt": "2024-01-16T11:00:00"
      }
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 10,
      "sort": {
        "sorted": true,
        "unsorted": false
      }
    },
    "totalElements": 1,
    "totalPages": 1,
    "last": true
  },
  "timestamp": "2024-01-16T11:05:00"
}
```

### 5. Update a Task

```bash
curl -X PUT http://localhost:8080/api/v1/tasks/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
  -d '{
    "title": "Implement JWT authentication",
    "description": "Add JWT-based authentication with refresh tokens",
    "status": "IN_PROGRESS",
    "priority": "URGENT",
    "dueDate": "2024-01-25T00:00:00"
  }'
```

### 6. Update Task Status

```bash
curl -X PATCH "http://localhost:8080/api/v1/tasks/1/status?status=DONE" \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

### 7. Get Task by ID

```bash
curl -X GET http://localhost:8080/api/v1/tasks/1 \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

### 8. Get User's Tasks

```bash
curl -X GET "http://localhost:8080/api/v1/tasks/user/1?page=0&size=10" \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

### 9. Delete a Task

```bash
curl -X DELETE http://localhost:8080/api/v1/tasks/1 \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

### 10. Refresh Access Token

```bash
curl -X POST http://localhost:8080/api/v1/auth/refresh \
  -H "Authorization: Bearer YOUR_REFRESH_TOKEN"
```

### 11. Logout

```bash
curl -X POST http://localhost:8080/api/v1/auth/logout \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

### 12. Get Current User Profile

```bash
curl -X GET http://localhost:8080/api/v1/users/me \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

---

## ⚙️ Configuration

### Environment Variables

The application can be configured using environment variables:

| Variable | Description | Default | Required |
|----------|-------------|---------|----------|
| `SPRING_PROFILES_ACTIVE` | Active profile (dev/prod/test) | `dev` | No |
| `SERVER_PORT` | Application port | `8080` | No |
| `SPRING_DATASOURCE_URL` | Database JDBC URL | H2 in dev | Yes (prod) |
| `SPRING_DATASOURCE_USERNAME` | Database username | `admin` | Yes (prod) |
| `SPRING_DATASOURCE_PASSWORD` | Database password | `admin123` | Yes (prod) |
| `JWT_SECRET` | JWT signing secret | (default) | Yes (prod) |
| `JWT_EXPIRATION` | Access token expiration (ms) | `900000` | No |
| `JWT_REFRESH_EXPIRATION` | Refresh token expiration (ms) | `604800000` | No |

### Application Profiles

#### Development Profile (`dev`)
- Uses H2 in-memory database
- SQL logging enabled
- H2 console available at `/h2-console`
- Debug logging for application packages
- Auto-creates database schema

**Activate:**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
# or
java -jar target/task-manager-api-1.0.0.jar --spring.profiles.active=dev
```

#### Production Profile (`prod`)
- Uses PostgreSQL database
- SQL logging disabled
- Info-level logging
- Schema validation (no auto-creation)
- Optimized for performance

**Activate:**
```bash
java -jar target/task-manager-api-1.0.0.jar --spring.profiles.active=prod
```

#### Test Profile (`test`)
- Uses H2 in-memory database
- Optimized for testing
- Schema auto-creation for tests

**Activate:**
```bash
mvn test -Dspring.profiles.active=test
```

### JWT Configuration

Edit `src/main/resources/application.yml`:

```yaml
jwt:
  # CRITICAL: Change this in production!
  # Must be at least 256 bits (32 characters)
  secret: your-256-bit-secret-key-change-this-in-production-environment
  
  # Access token expiration: 15 minutes (in milliseconds)
  expiration: 900000
  
  # Refresh token expiration: 7 days (in milliseconds)
  refresh-expiration: 604800000
```

### Database Configuration

#### PostgreSQL (Production)

Create the database:
```sql
CREATE DATABASE taskmanager;
CREATE USER admin WITH PASSWORD 'admin123';
GRANT ALL PRIVILEGES ON DATABASE taskmanager TO admin;
```

#### H2 Console (Development)

Access H2 console at: http://localhost:8080/h2-console

Settings:
- JDBC URL: `jdbc:h2:mem:taskdb`
- Username: `sa`
- Password: (leave empty)

### Cache Configuration

The application uses Caffeine cache with the following settings:
- Maximum size: 1000 entries
- Expiration: 10 minutes after write
- Cached entities: Tasks, Users

### Email Configuration (Optional)

To enable email notifications, add to `application.yml`:

```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: your-email@gmail.com
    password: your-app-password
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
```

---

## 🧪 Testing

### Run All Tests

```bash
mvn test
```

### Run Specific Test Class

```bash
mvn test -Dtest=TaskServiceTest
```

### Run Tests with Coverage

```bash
mvn clean test jacoco:report
```

Coverage report will be available at: `target/site/jacoco/index.html`

### Integration Tests

```bash
mvn verify
```

### Test Structure

```
src/test/java/com/taskmanager/
├── controller/          # Controller layer tests
│   ├── AuthControllerTest.java
│   ├── TaskControllerTest.java
│   └── UserControllerTest.java
├── service/            # Service layer tests
│   ├── AuthServiceTest.java
│   ├── TaskServiceTest.java
│   └── UserServiceTest.java
└── integration/        # Integration tests
    └── TaskApiIntegrationTest.java
```

### Testing Best Practices

- Unit tests use Mockito for mocking dependencies
- Integration tests use in-memory H2 database
- Spring Security Test for authentication testing
- Tests follow AAA pattern (Arrange, Act, Assert)

---

## 🗄️ Database

### Development Database (H2)

- **Type**: In-memory database
- **URL**: `jdbc:h2:mem:taskdb`
- **Console**: http://localhost:8080/h2-console
- **Persistence**: Data is lost on application restart
- **Use Case**: Development and testing

**Advantages:**
- Zero configuration required
- Fast startup
- No external dependencies
- Automatic schema creation

### Production Database (PostgreSQL)

- **Version**: PostgreSQL 15
- **Default Port**: 5432
- **Default Database**: `taskmanager`
- **Default Credentials**: username=`admin`, password=`admin123`

**Setup with Docker:**
```bash
docker run -d \
  --name taskmanager-postgres \
  -e POSTGRES_DB=taskmanager \
  -e POSTGRES_USER=admin \
  -e POSTGRES_PASSWORD=admin123 \
  -p 5432:5432 \
  postgres:15-alpine
```

**Manual Setup:**
```sql
-- Create database
CREATE DATABASE taskmanager;

-- Create user
CREATE USER admin WITH PASSWORD 'admin123';

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE taskmanager TO admin;
```

### Database Schema

#### Users Table
- `id` (BIGINT, Primary Key)
- `username` (VARCHAR, Unique)
- `email` (VARCHAR, Unique)
- `password` (VARCHAR, BCrypt hashed)
- `first_name` (VARCHAR)
- `last_name` (VARCHAR)
- `role` (VARCHAR: USER, ADMIN)
- `created_at` (TIMESTAMP)
- `updated_at` (TIMESTAMP)

#### Tasks Table
- `id` (BIGINT, Primary Key)
- `title` (VARCHAR)
- `description` (TEXT)
- `status` (VARCHAR: TODO, IN_PROGRESS, DONE, CANCELLED)
- `priority` (VARCHAR: LOW, MEDIUM, HIGH, URGENT)
- `due_date` (TIMESTAMP)
- `assigned_to_user_id` (BIGINT, Foreign Key)
- `created_at` (TIMESTAMP)
- `updated_at` (TIMESTAMP)

### Database Migrations

The application uses Hibernate DDL auto-generation:
- **Development**: `create-drop` - Schema is recreated on each startup
- **Production**: `validate` - Schema must exist; only validates structure

For production migrations, consider using:
- Flyway
- Liquibase

---

## 🏗️ Architecture

### Layered Architecture

The application follows a clean layered architecture:

```
┌─────────────────────────────────────────┐
│         Presentation Layer              │
│         (Controllers)                   │
│  - REST endpoints                       │
│  - Request/Response DTOs                │
│  - Input validation                     │
└─────────────────────────────────────────┘
                   ↓
┌─────────────────────────────────────────┐
│         Service Layer                   │
│         (Business Logic)                │
│  - Business rules                       │
│  - Transaction management               │
│  - DTO mapping                          │
└─────────────────────────────────────────┘
                   ↓
┌─────────────────────────────────────────┐
│         Repository Layer                │
│         (Data Access)                   │
│  - JPA repositories                     │
│  - Custom queries                       │
│  - Database operations                  │
└─────────────────────────────────────────┘
                   ↓
┌─────────────────────────────────────────┐
│         Database Layer                  │
│  - PostgreSQL / H2                      │
│  - Entities                             │
└─────────────────────────────────────────┘
```

### Security Flow

```
┌──────────────┐
│   Client     │
└──────┬───────┘
       │ HTTP Request + JWT Token
       ↓
┌──────────────────────────┐
│  Security Filter Chain   │
│  - JwtAuthFilter         │
│  - CORS Configuration    │
└──────┬───────────────────┘
       │ Validate Token
       ↓
┌──────────────────────────┐
│  Authentication Manager  │
│  - Extract user details  │
│  - Verify token          │
└──────┬───────────────────┘
       │ Set SecurityContext
       ↓
┌──────────────────────────┐
│     Controller           │
│  - @PreAuthorize         │
│  - Role-based access     │
└──────┬───────────────────┘
       │
       ↓
┌──────────────────────────┐
│    Service Layer         │
└──────┬───────────────────┘
       │
       ↓
┌──────────────────────────┐
│   Response to Client     │
└──────────────────────────┘
```

### Package Structure

```
com.taskmanager/
├── config/              # Configuration classes
│   ├── SecurityConfig.java
│   ├── CacheConfig.java
│   └── OpenApiConfig.java
├── controller/          # REST controllers
│   ├── AuthController.java
│   ├── TaskController.java
│   └── UserController.java
├── dto/                # Data Transfer Objects
│   ├── request/
│   └── response/
├── entity/             # JPA entities
│   ├── User.java
│   ├── Task.java
│   ├── Role.java
│   ├── TaskStatus.java
│   └── Priority.java
├── repository/         # Spring Data repositories
│   ├── UserRepository.java
│   └── TaskRepository.java
├── service/            # Business logic
│   ├── AuthService.java
│   ├── TaskService.java
│   ├── UserService.java
│   └── EmailService.java
├── security/           # Security components
│   ├── JwtTokenProvider.java
│   ├── JwtAuthFilter.java
│   └── UserDetailsServiceImpl.java
├── mapper/             # MapStruct mappers
│   ├── TaskMapper.java
│   └── UserMapper.java
├── exception/          # Exception handling
│   ├── GlobalExceptionHandler.java
│   ├── ResourceNotFoundException.java
│   ├── UnauthorizedException.java
│   └── BadRequestException.java
└── TaskManagerApplication.java
```

### Design Patterns Used

- **Repository Pattern** - Data access abstraction
- **Service Layer Pattern** - Business logic encapsulation
- **DTO Pattern** - Data transfer between layers
- **Builder Pattern** - Object construction (via Lombok)
- **Strategy Pattern** - Authentication strategies
- **Filter Pattern** - JWT authentication filter
- **Dependency Injection** - Spring IoC container

---

## 🔧 Troubleshooting

### Common Issues and Solutions

#### 1. Application Won't Start - Port Already in Use

**Error:**
```
Web server failed to start. Port 8080 was already in use.
```

**Solution:**
```bash
# Find process using port 8080
lsof -i :8080  # macOS/Linux
netstat -ano | findstr :8080  # Windows

# Kill the process or change port
SERVER_PORT=8081 mvn spring-boot:run
```

#### 2. Database Connection Failed

**Error:**
```
Connection to localhost:5432 refused
```

**Solution:**
- Ensure PostgreSQL is running: `docker-compose up postgres`
- Verify connection details in `application-prod.yml`
- Check database exists: `psql -U admin -d taskmanager`
- Use H2 for development: `--spring.profiles.active=dev`

#### 3. JWT Token Invalid or Expired

**Error:**
```
401 Unauthorized - Token expired or invalid
```

**Solution:**
- Request a new token via `/api/v1/auth/login`
- Use refresh token at `/api/v1/auth/refresh`
- Ensure token is sent as: `Authorization: Bearer <token>`
- Check JWT secret is consistent across restarts

#### 4. Maven Build Fails - Compilation Error

**Error:**
```
Compilation failure - cannot find symbol
```

**Solution:**
```bash
# Clean and rebuild
mvn clean install

# Ensure annotation processors are working
mvn clean compile

# Check Java version
java -version  # Should be 17+
```

#### 5. Lombok Not Working in IDE

**Solution:**
- Install Lombok plugin for your IDE
- Enable annotation processing in IDE settings
- IntelliJ: Settings → Build → Compiler → Annotation Processors → Enable
- Eclipse: Install Lombok via lombok.jar

#### 6. MapStruct Generated Classes Not Found

**Solution:**
```bash
# Regenerate MapStruct mappers
mvn clean compile

# Check generated sources in: target/generated-sources/annotations
```

#### 7. H2 Console Not Accessible

**Error:**
```
404 Not Found - /h2-console
```

**Solution:**
- Only available in `dev` profile
- Restart with: `--spring.profiles.active=dev`
- Check: `spring.h2.console.enabled=true` in application-dev.yml

#### 8. Docker Container Exits Immediately

**Solution:**
```bash
# Check logs
docker-compose logs app

# Common causes:
# - JAR file not built: Run `mvn clean package` first
# - Database not ready: Increase `depends_on` wait time
# - Environment variables incorrect: Check docker-compose.yml
```

#### 9. Tests Failing

**Solution:**
```bash
# Run tests with debug output
mvn test -X

# Skip tests temporarily
mvn clean install -DskipTests

# Run specific test
mvn test -Dtest=TaskServiceTest
```

#### 10. CORS Errors in Browser

**Error:**
```
Access to XMLHttpRequest blocked by CORS policy
```

**Solution:**
- Check `SecurityConfig.java` for CORS configuration
- Add your frontend URL to allowed origins
- Ensure preflight OPTIONS requests are allowed

### Getting Help

If you encounter issues not covered here:

1. **Check Logs**: `docker-compose logs -f app` or console output
2. **Enable Debug Logging**: Set `logging.level.com.taskmanager=DEBUG`
3. **Verify Configuration**: Check `application.yml` and environment variables
4. **Test Endpoints**: Use Swagger UI to test API directly
5. **Database State**: Check H2 console or PostgreSQL for data issues

### Useful Commands

```bash
# Check application health
curl http://localhost:8080/actuator/health

# View active profile
curl http://localhost:8080/actuator/env | grep spring.profiles.active

# Clean Docker resources
docker system prune -a --volumes

# Reset database
docker-compose down -v && docker-compose up -d

# View real-time logs
tail -f logs/application.log
```

---

## 📄 License

This project is licensed under the MIT License.

```
MIT License

Copyright (c) 2024 Task Manager API

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

---

## 📞 Support

For issues, questions, or contributions, please open an issue in the repository.

---

**Built with ❤️ using Spring Boot**