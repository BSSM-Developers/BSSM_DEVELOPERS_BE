# Authentication Architecture

## Overview
The application implements a dual-track Google OAuth2 authentication system with different flows based on email domain.

## Authentication Flows

### 1. BSSM Accounts (@bssm.hs.kr)
- Direct signup and login without approval
- Immediate user registration upon first login
- Full access granted automatically

### 2. General Google Accounts
- **Existing users**: Login directly
- **New users**: 
  1. Create signup request
  2. Admin approval required
  3. Login after approval

## Key Components

### Service Layer
- **GoogleLoginService**: Main authentication orchestrator
  - Handles OAuth2 code exchange
  - Manages PKCE flow with code verifier storage
  - Orchestrates user login vs signup request creation
  
- **EmailValidator**: Determines email domain type (@bssm.hs.kr vs general)

- **UserLoginService**: User registration and retrieval

- **SignupRequestService**: Handles approval workflow for non-BSSM accounts

### JWT Implementation
- **JwtProvider**: Token generation and validation
- **JwtFilter**: Request authentication via JWT
- Token types:
  - Access token (1 hour expiry)
  - Refresh token (7 days expiry)

### External Integration
- **GoogleTokenFeign**: OAuth2 token exchange
- **GoogleResourceAccessFeign**: User info retrieval
- PKCE (Proof Key for Code Exchange) flow with session-based code verifier storage

### Security Configuration
- **SecurityConfig**: Spring Security setup
- **CurrentUser** annotation: Inject authenticated user into controllers
- **CurrentUserArgumentResolver**: Resolves @CurrentUser from JWT

## Data Models

### Auth Domain
- **RefreshToken**: Redis-stored refresh tokens
- **GoogleCodeVerifier**: Redis-stored PKCE code verifiers

### Signup Domain
- **SignupForm**: Signup request entity
- **SignupToken**: Temporary token for signup approval
- **SignUpFormState**: PENDING, APPROVED, REJECTED

### User Domain
- **User**: Main user entity
- **UserRole**: ADMIN, USER