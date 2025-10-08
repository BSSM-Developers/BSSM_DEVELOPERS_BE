# External Integrations

## Google OAuth2 Integration

### Overview
The application uses Google OAuth2 with PKCE (Proof Key for Code Exchange) flow for secure authentication.

### Feign Clients

#### GoogleTokenFeign
**Purpose**: Exchange authorization code for access tokens

**Endpoint**: `https://oauth2.googleapis.com/token`

**Methods**:
- `getToken(GoogleTokenRequest)` - Exchange code for tokens

**Configuration**:
- Form encoding via `FeignConfig`
- Content-Type: `application/x-www-form-urlencoded`

#### GoogleResourceAccessFeign
**Purpose**: Retrieve user information from Google

**Endpoint**: `https://www.googleapis.com/oauth2/v2/userinfo`

**Methods**:
- `getUserResource(String accessToken)` - Get user profile

**Headers**:
- Authorization: `Bearer {accessToken}`

### PKCE Flow Implementation

#### Components
- **PkceUtil**: Generates code verifier and challenge
- **GoogleCodeVerifierRepository**: Redis storage for code verifiers
- **UrlBuilder**: Constructs OAuth2 authorization URLs

#### Flow Steps
1. **Generate PKCE params**:
   - Code verifier (random string)
   - Code challenge (SHA-256 hash of verifier)
   
2. **Store code verifier** in Redis with session key

3. **Redirect to Google** with:
   - `code_challenge`
   - `code_challenge_method=S256`
   - `state` parameter for CSRF protection

4. **Exchange code** for tokens using stored verifier

5. **Cleanup**: Remove code verifier from Redis

### Configuration Properties

#### GoogleOauthProperties
```yaml
google:
  oauth:
    client-id: ${GOOGLE_CLIENT_ID}
    client-secret: ${GOOGLE_CLIENT_SECRET}
    redirect-uri: ${GOOGLE_REDIRECT_URI}
    scope: "openid email profile"
```

**Fields**:
- `clientId`: OAuth2 client identifier
- `clientSecret`: OAuth2 client secret
- `redirectUri`: Callback URL after Google authentication
- `scope`: Requested permissions

### Security Considerations

- **PKCE** prevents authorization code interception
- **State parameter** prevents CSRF attacks
- **Code verifier** stored in Redis with short TTL
- **Refresh tokens** stored securely in Redis
- **Access tokens** validated via JWT

## JWT Implementation

### JwtProvider
**Responsibilities**:
- Generate access and refresh tokens
- Validate token signatures
- Extract user information from tokens

**Configuration**:
```yaml
jwt:
  accessExp: 3600      # 1 hour
  refreshExp: 604800   # 7 days
  header: Authorization
  prefix: Bearer
```

### JwtFilter
**Purpose**: Intercept HTTP requests and authenticate via JWT

**Flow**:
1. Extract token from Authorization header
2. Validate token signature and expiration
3. Set authentication in SecurityContext
4. Allow request to proceed

**Exempt paths**:
- `/auth/**` - Authentication endpoints
- `/public/**` - Public resources

### Token Storage

#### RefreshTokenRepository (Redis)
- **Key**: User ID
- **Value**: Refresh token
- **TTL**: 7 days
- **Purpose**: Token refresh and revocation

### Token Response
```json
{
  "accessToken": "eyJhbGc...",
  "refreshToken": "eyJhbGc...",
  "tokenType": "Bearer"
}
```

## Redis Integration

### Use Cases
1. **Refresh tokens**: Long-lived tokens for re-authentication
2. **Code verifiers**: PKCE flow state management
3. **Signup tokens**: Temporary tokens for signup approval

### Configuration
```yaml
spring:
  data:
    redis:
      host: ${REDIS_HOST:localhost}
      port: ${REDIS_PORT:6379}
```

### Repository Pattern
- Extends `CrudRepository<Entity, ID>`
- Automatic TTL management via `@TimeToLive` annotation
- Key prefix support via `@RedisHash`

## Client Configuration

### ClientProperties
**Purpose**: Frontend URL configuration for CORS and redirects

```yaml
client:
  url: ${CLIENT_URL:http://localhost:3000}
```

**Usage**:
- CORS allowed origins
- OAuth2 redirect after login
- Signup approval redirect

## Feign Configuration

### FeignConfig
**Location**: `global/config/FeignConfig.java`

**Features**:
- Form encoder for OAuth2 token requests
- Custom error decoder
- Request/response logging

**Bean Registration**:
```java
@Bean
public Encoder feignFormEncoder() {
    return new FormEncoder(new SpringEncoder(...));
}
```

## External Service Dependencies

### Required External Services
1. **Google OAuth2 Service**: User authentication
2. **Redis Server**: Token and session storage
3. **MySQL Database**: Persistent data storage

### Development Setup
- Ensure Redis is running: `redis-server`
- Configure Google OAuth2 credentials in environment
- Database migrations (manual, `ddl-auto: none`)