#Shravani-Dhuri_AI-DS_SY_A_41-ASSIGNMENT-9-JAVA

A secure Spring Boot REST API for managing student records, built using a **3-layer MVC architecture** and **Data Transfer Objects (DTOs)**. The application secures endpoints using **Spring Security** (Basic Authentication) with role-based access control (RBAC).

---

## 🚀 Features
- **3-Layer MVC Architecture**: Separation of concerns with Controllers, Services, and Repositories.
- **DTO Pattern**: Uses request/response DTOs to encapsulate transfer data and avoid exposing database entities directly.
- **Role-Based Access Control (RBAC)**: Secure access configurations:
  - **`ADMIN`**: Full access (Read, Create, Update, Delete).
  - **`USER`**: Read-only access (GET requests).
  - **Unauthenticated**: No access allowed to student APIs.
- **H2 In-Memory Database**: Quick integration and testing with schema auto-creation.
- **Database Console**: Interactive database viewer available at `/h2-console` (publicly accessible).
- **Data Seeding**: Automatically populates the database with 3 mock students on startup.
- **Integration Tests**: Comprehensive test suite verifying security constraints, role clearances, and CRUD operations.

---

## 🛠️ Tech Stack & Dependencies
- **Java**: Version 21
- **Framework**: Spring Boot (v3.2.5)
  - `spring-boot-starter-web`
  - `spring-boot-starter-data-jpa`
  - `spring-boot-starter-security`
  - `spring-boot-starter-validation`
- **Database**: H2 (In-Memory)
- **Utilities**: Lombok (for reducing boilerplate code)
- **Testing**: JUnit 5, MockMvc, and Spring Security Test Support

---

## 📂 Project Structure
```text
student-api
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           └── studentapi
│   │   │               ├── StudentApiApplication.java (Main Entry Point)
│   │   │               ├── DataSeeder.java (Mock data populator)
│   │   │               ├── config
│   │   │               │   └── SecurityConfig.java (Spring Security configs)
│   │   │               ├── controller
│   │   │               │   └── StudentController.java (REST API Endpoints)
│   │   │               ├── dto
│   │   │               │   ├── StudentRequestDTO.java (Input representation)
│   │   │               │   └── StudentResponseDTO.java (Output representation)
│   │   │               ├── entity
│   │   │               │   └── Student.java (JPA Entity model)
│   │   │               ├── exception
│   │   │               │   └── GlobalExceptionHandler.java (Central error handling)
│   │   │               ├── repository
│   │   │               │   └── StudentRepository.java (Data Access Layer)
│   │   │               └── service
│   │   │                   └── StudentService.java (Business Logic Layer)
│   │   └── resources
│   │       └── application.yml (Configuration properties)
│   └── test
│       └── java
│           └── com
│               └── example
│                   └── studentapi
│                       └── StudentSecurityIntegrationTests.java (Security tests)
```

---

## 🔐 Credentials
The application uses HTTP Basic Authentication with in-memory users. Here are the default credentials configured in `SecurityConfig.java`:

| Role | Username | Password | Permissions |
| :--- | :--- | :--- | :--- |
| **ADMIN** | `admin` | `admin123` | GET, POST, PUT, DELETE |
| **USER** | `user` | `user123` | GET only |

---

## 🔌 API Endpoints
All API endpoints are prefixed with `/api/v1/students`.

| Method | Endpoint | Required Role | Description |
| :--- | :--- | :--- | :--- |
| **GET** | `/api/v1/students` | `USER` or `ADMIN` | Retrieve list of all students |
| **GET** | `/api/v1/students/{id}` | `USER` or `ADMIN` | Retrieve a single student by ID |
| **POST** | `/api/v1/students` | `ADMIN` | Create a new student record |
| **PUT** | `/api/v1/students/{id}` | `ADMIN` | Update an existing student record |
| **DELETE** | `/api/v1/students/{id}` | `ADMIN` | Delete a student record by ID |

---

## ⚙️ Running the Application

### Prerequisites
- Java 21 JDK or higher
- Maven (or use the included wrapper `mvnw`)

### Start the Application
Clone the repository, navigate to the project directory, and run:
```bash
./mvnw spring-boot:run
```
Once started, the application will be accessible at: `http://localhost:8080`

### Access H2 Console
The in-memory database console is available at `http://localhost:8080/h2-console`.
- **JDBC URL**: `jdbc:h2:mem:studentdb`
- **Username**: `sa`
- **Password**: *(leave blank)*

---

## 🧪 Testing the APIs
You can test the endpoints using any REST client (e.g., Postman or Curl) by providing the username and password in the Basic Auth headers.

### Sample CURL Requests

#### 1. Retrieve all students (as `user`):
```bash
curl -u user:user123 http://localhost:8080/api/v1/students
```

#### 2. Create a new student (as `admin`):
```bash
curl -u admin:admin123 -X POST http://localhost:8080/api/v1/students \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "David",
    "lastName": "Miller",
    "email": "david.miller@university.edu",
    "age": 23,
    "course": "Chemistry"
  }'
```

#### 3. Attempt to create a new student as a read-only `user` (should return `403 Forbidden`):
```bash
curl -u user:user123 -X POST http://localhost:8080/api/v1/students \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Fake",
    "lastName": "Student",
    "email": "fake@university.edu",
    "age": 20,
    "course": "Art"
  }'
```

---

## 🧪 Verification
Run integration tests to verify endpoint accessibility rules:
```bash
./mvnw test
```
