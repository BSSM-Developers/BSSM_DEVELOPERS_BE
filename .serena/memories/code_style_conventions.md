# Code Style and Conventions

## Entity Patterns

### Entity Class Structure
```java
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EntityName {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long entityNameId;  // Descriptive ID naming
    
    // Fields with appropriate annotations
    
    // Static factory method
    public static EntityName of(params) {
        return new EntityName(params);
    }
}
```

**Conventions:**
- Use `@Entity` with Lombok `@Getter`, `@NoArgsConstructor`
- ID fields named descriptively (e.g., `userId`, `signupRequestId`)
- Static factory methods: `Entity.of(params)` for object creation
- Protected no-args constructor for JPA

## DTO Patterns

### Request/Response DTOs
```java
public record UserRequest(String email, String name) {}
public record UserResponse(Long userId, String email, String name) {}
```

**Conventions:**
- Use `record` for all request/response DTOs
- **No "Dto" suffix** in class names
- Place in `dto/request/` and `dto/response/` packages
- Records are immutable and concise

## Mapper Pattern

### Manual Entity-DTO Mapping
```java
@Component
public class EntityMapper {
    public Entity toEntity(EntityRequest dto) { ... }
    public EntityResponse toResponse(Entity entity) { ... }
}
```

**Conventions:**
- Spring `@Component` mappers
- Manual mapping (no MapStruct)
- Standard method names:
  - `toEntity(dto)` - DTO → Entity
  - `toResponse(entity)` - Entity → DTO

## Exception Handling

### Custom Exception Pattern
```java
public class CustomException extends GlobalException {
    private CustomException() {
        super(ErrorCode.CUSTOM_ERROR);
    }

    private static class Holder {
        private static final CustomException INSTANCE = new CustomException();
    }

    public static CustomException raise() {
        throw Holder.INSTANCE;
    }
}
```

**Conventions:**
- All exceptions extend `GlobalException` with `ErrorCode` enum
- Singleton pattern using inner `Holder` class
- Static `raise()` method for exception throwing
- Private constructor prevents external instantiation

### Error Codes
```java
public enum ErrorCode {
    BSSM_EMAIL_INVALID(403, "BSSM 이메일이 아닙니다"),
    SIGNUP_REQUEST_ALREADY_EXISTS(400, "이미 가입 신청이 존재합니다"),
    BASE64_CONVERSION_FAIL(500, "Base64 변환에 실패했습니다");
    
    // Fields: httpStatus, message
}
```

## Service Layer Patterns

### Service Organization
- **Command services**: Write operations, state changes
- **Query services**: Read operations, queries
- Clear separation: `UserCommandService`, `UserQueryService`

### Transaction Boundaries
```java
@Transactional
public void commandMethod() { }

@Transactional(readOnly = true)
public Entity queryMethod() { }
```

## Code Style Rules

### Boolean Comparisons
❌ **Avoid:**
```java
if (isUserExists == true) { }
if (isActive == false) { }
```

✅ **Prefer:**
```java
if (isUserExists) { }
if (!isActive) { }
```

### Constants and Magic Values
- Extract boolean comparisons to variables with descriptive names
- Use descriptive constant names for string literals
- Avoid magic numbers
- Direct usage is clearer than unnecessary constants

### Naming Conventions
- Entities: Descriptive ID fields (`userId`, not `id`)
- DTOs: No "Dto" suffix
- Services: Domain-focused names
- Validators, Utils, Components: Clear, purpose-driven names

## Configuration Management

### Properties Classes
```java
@ConfigurationProperties(prefix = "google.oauth")
public record GoogleOauthProperties(
    String clientId,
    String clientSecret,
    String redirectUri
) {}
```

**Conventions:**
- Use `@ConfigurationProperties` with records
- Enable with `@ConfigurationPropertiesScan`
- Immutable configuration via records

### Feign Configuration
- Centralized in `FeignConfig`
- Form encoding support for OAuth2
- Base packages: `com.example.bssm_dev.global.feign`