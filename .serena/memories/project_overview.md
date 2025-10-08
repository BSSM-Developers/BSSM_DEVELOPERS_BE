# BSSM Dev - Project Overview

## Purpose
BSSM Dev is a Spring Boot-based authentication and user management system designed for BSSM (Busan Software Meister High School). The application provides Google OAuth2 authentication with special handling for BSSM email accounts (@bssm.hs.kr) and a signup approval workflow for general Google accounts.

## Tech Stack

### Core Framework
- **Spring Boot 3.5.4** (Java 21)
- **Spring Security** - Authentication & Authorization
- **Spring Data JPA** - ORM with Hibernate
- **Spring Data Redis** - Session and token storage
- **Spring Cloud OpenFeign** - External API integration

### Database
- **MySQL** - Primary database with JPA
- **Redis** - Token and session storage

### Authentication & Security
- **JWT (jjwt 0.12.5)** - Token-based authentication
- **Google OAuth2** - Social login with PKCE flow

### Build Tool
- **Gradle** with Java 21 toolchain

### Additional Libraries
- **Lombok** - Boilerplate code reduction
- **Feign Form Spring** - Form encoding for OAuth2

## Project Structure

### Domain-Driven Design
```
src/main/java/com/example/bssm_dev/
├── domain/           # Business logic organized by domain
│   ├── auth/        # Google OAuth2, JWT, authentication flow
│   ├── user/        # User management
│   └── signup/      # Signup request approval workflow
├── global/          # Cross-cutting concerns
│   ├── config/      # Spring configurations & properties
│   ├── error/       # Exception handling
│   ├── jwt/         # JWT provider & filter
│   └── feign/       # External API clients
└── common/          # Shared utilities, DTOs, annotations
```

### Key Domain Structure
Each domain follows consistent organization:
- `controller/` - REST endpoints
- `service/` - Business logic (separated into command/query when needed)
- `repository/` - Data access layer
- `model/` - Entities and domain models
- `dto/request/` & `dto/response/` - Data transfer objects
- `mapper/` - Entity ↔ DTO conversion
- `exception/` - Domain-specific exceptions
- `validator/`, `util/`, `component/` - Supporting classes