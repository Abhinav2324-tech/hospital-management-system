# Hospital Management System

A Spring Boot backend for user registration, JWT authentication, and patient record management. Users log in to receive a token, then use that token to access the patient API. Patient records are stored in MySQL and remain available after the application restarts.

The `main` branch contains only this README. Select **`develop`** in the GitHub branch dropdown to view the application code and Postman collection.

## Features

- Register users with a unique email address and store passwords as BCrypt hashes.
- Log in with an email and password to receive a signed JWT.
- Require a valid Bearer token for all patient routes.
- Create, list, retrieve, update, and delete patient records in MySQL.

## Tech Stack

Java 17, Spring Boot, Spring Web, Spring Security, JJWT, Spring Data JPA, MySQL, Lombok, Maven, and Postman.

## Project Structure

The main Java package is `com.bridgelabz.hospitalmanagementsystem`.

| Package | Responsibility |
| --- | --- |
| `config` | Configures Spring Security and handles JWT signing, verification, and request authentication. |
| `controller` | Exposes the authentication and patient REST endpoints. |
| `dto` | Defines API request and response data. |
| `entity` | Defines the `User`, `Role`, and `Patient` types. |
| `repository` | Provides database access through Spring Data JPA. |
| `service` | Handles registration, login, and patient CRUD logic. |

## Run Locally

1. Clone the repository, open it in Git Bash, and switch to the application branch:

   ```bash
   git switch develop
   ```

2. Start MySQL and create the database:

   ```sql
   CREATE DATABASE IF NOT EXISTS hospital_db;
   ```

3. The app connects to MySQL on `localhost:3306` and uses `root` by default. If your username is different, run `export DB_USERNAME='your_mysql_username'`. Then enter your database password, generate a local JWT signing key, and start the app:

   ```bash
   read -rsp 'MySQL password: ' DB_PASSWORD
   echo
   export DB_PASSWORD
   export JWT_SECRET="$(openssl rand -base64 32)"
   ./mvnw spring-boot:run
   ```

4. The API runs at `http://localhost:8080`. Hibernate creates or updates the `users` and `patients` tables at startup. If you generate a new signing key, log in again to get a new token.

## API Endpoints

| Method | Endpoint | Authentication | Success status |
| --- | --- | --- | --- |
| `POST` | `/api/auth/register` | None | `201 Created` |
| `POST` | `/api/auth/login` | None | `200 OK` |
| `POST` | `/api/patients` | Bearer token | `201 Created` |
| `GET` | `/api/patients` | Bearer token | `200 OK` |
| `GET` | `/api/patients/{id}` | Bearer token | `200 OK` |
| `PUT` | `/api/patients/{id}` | Bearer token | `200 OK` |
| `DELETE` | `/api/patients/{id}` | Bearer token | `204 No Content` |

Registration accepts `name`, `email`, and `password`. Login accepts `email` and `password` and returns a `token`. Send that token with each patient request:

```http
Authorization: Bearer <token>
```

Patient create and update requests accept `name`, `age`, `gender`, `disease`, `address`, and `phoneNumber`.

## Test with Postman

Import `postman/Hospital-Management-API.postman_collection.json` from `develop`. Create a local environment with these variables:

| Variable | Value |
| --- | --- |
| `baseUrl` | `http://localhost:8080` |
| `demoEmail` | Your test account email |
| `demoPassword` | Your test account password |
| `token` | The token returned by login |
| `patientId` | The ID returned when you add a patient |

Register or use an existing test account, log in, and save the token. Create a patient and use the returned ID to retrieve, update, and delete that record. A patient request without a token should return `401 Unauthorized`. Retrieving a deleted patient with a valid token should return `404 Not Found`.

## Development Workflow

The project was built in eight use cases covering setup, entities, repositories, DTOs, services, JWT security, REST controllers, and Postman testing. Each use case was developed on its own `feature/UCN/...` branch and merged into `develop`.
