# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Development Commands

### Build and Run
```bash
./gradlew build          # Build the project
./gradlew bootRun        # Run the Spring Boot application
./gradlew test           # Run all tests
./gradlew test --tests "ClassName"  # Run specific test class
```

### Code Quality
```bash
./gradlew compileJava    # Compile and check for compilation errors
```

## Architecture Overview

### Domain-Driven Design Structure
The project follows a domain-driven design pattern with clear separation:

- `domain/` - Business logic organized by domain (auth, user, signup)
- `global/` - Cross-cutting concerns (config, error handling, JWT, feign clients)
- `common/` - Shared utilities and DTOs

### Authentication Flow
The application implements Google OAuth2 authentication with dual email domain support:

1. **BSSM accounts** (`@bssm.hs.kr`): Direct signup and login
2. **General Google accounts**: Require admin approval workflow
   - Existing users → Login directly
   - New users → Create signup request → Admin approval → Login

Key components:
- `GoogleLoginService`: Main authentication orchestrator
- `EmailValidator`: Determines email domain type
- `UserLoginService`: User registration and retrieval
- `SignupRequestService`: Handles approval workflow for non-BSSM accounts

### Data Layer Patterns

#### Entity Conventions
- Use `@Entity` with Lombok `@Getter`, `@NoArgsConstructor`
- ID fields named descriptively (e.g., `userId`, `signupRequestId`)
- Static factory methods: `Entity.of(params)`

#### DTO Conventions
- Use `record` for all request/response DTOs
- No "Dto" suffix in class names
- Place in `dto/request/` and `dto/response/` packages

#### Mapper Pattern
- Spring `@Component` mappers for entity ↔ DTO conversion
- Manual mapping (no MapStruct)
- Methods: `toEntity(dto)`, `toResponse(entity)`

### Error Handling Architecture

#### Custom Exceptions
- All extend `GlobalException` with `ErrorCode` enum
- Singleton pattern using inner `Holder` class
- Static `raise()` method for exception creation

#### Error Codes
Defined in `ErrorCode` enum with HTTP status codes and messages:
- `BSSM_EMAIL_INVALID` (403)
- `SIGNUP_REQUEST_ALREADY_EXISTS` (400)
- `BASE64_CONVERSION_FAIL` (500)

### External Integrations

#### Google OAuth2
- `GoogleTokenFeign`: Token exchange
- `GoogleResourceAccessFeign`: User info retrieval
- PKCE flow implementation with session-based code verifier storage

#### JWT Implementation
- Custom `JwtProvider` for token generation/validation
- `JwtFilter` for request authentication
- Configurable via `JwtProperties` (`application.yaml`)

### Configuration Management

#### Properties Classes
- `@ConfigurationProperties` with `@ConfigurationPropertiesScan`
- `GoogleOauthProperties`: OAuth2 client configuration
- `JwtProperties`: JWT settings
- `ClientProperties`: Frontend URL configuration

#### Database Configuration
- MySQL with JPA/Hibernate
- `ddl-auto: none` in production
- SQL formatting enabled, logging controlled by profile

## Code Style Conventions

### Constants and Magic Numbers
- Extract boolean comparisons to variables: `if (isUserExists)` not `if (isUserExists == true)`
- Use descriptive constant names for string literals
- Avoid magic numbers and boolean constants where direct usage is clearer

### Service Layer Patterns
- Service classes focus on business logic
- Query operations separated into `*QueryService` classes
- Transaction boundaries clearly defined with `@Transactional`

### Feign Client Configuration
- Centralized in `FeignConfig`
- Form encoding support for OAuth2 token requests
- Base packages: `com.example.bssm_dev.global.feign`