# TaskFlow

A full-stack task management application built with Next.js and Spring Boot, secured with JWT authentication.

## Tech Stack

**Frontend:** Next.js (App Router), vanilla CSS

**Backend:** Spring Boot, Spring Security, PostgreSQL, JWT (JJWT)

## Features

- User registration and login with JWT-based authentication
- Create, read, update, and delete personal tasks
- Tasks are scoped per user — no cross-user data access
- Completed tasks are visually distinguished and sorted to the bottom
- Inline task description editing

## Getting Started

### Prerequisites

- Node.js 18+
- Java 17+
- PostgreSQL

### Backend

1. Create a PostgreSQL database named `taskflow`
2. Update credentials in `taskflow-api/src/main/resources/application.properties`
3. Run the Spring Boot application:

```bash
./mvnw spring-boot:run
```

### Frontend

```bash
cd taskflow-web
npm install
npm run dev
```

Open [http://localhost:3000](http://localhost:3000) to use the app.

## Project Structure

```
taskflow/
├── taskflow-api/      # Spring Boot REST API
└── taskflow-web/      # Next.js frontend
```
