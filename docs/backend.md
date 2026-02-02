# Backend Architecture

## Tech Stack
- **Java 21** + **Spring Boot 3.4**
- **Spring Security** + JWT authentication
- **Spring Data JPA** + QueryDSL
- **MySQL** (production) / **H2** (testing)
- **Gradle** build system

## Package Structure

```
org.wongoo.wongoo3/
├── domain/
│   ├── auth/           # Authentication & OAuth2
│   │   ├── controller/
│   │   ├── service/
│   │   ├── dto/
│   │   └── oauth2/     # OAuth2 providers (Naver, Kakao, Google)
│   ├── user/           # User management
│   │   ├── controller/
│   │   ├── service/
│   │   ├── dto/
│   │   ├── factory/
│   │   └── repository/
│   ├── post/           # Posts CRUD
│   ├── comment/        # Comments CRUD
│   ├── token/          # JWT token storage
│   ├── terms/          # Terms of service
│   │   ├── history/    # Terms version history
│   │   └── userterms/  # User-terms relationship
│   └── file/           # File upload
└── global/
    ├── config/         # Spring configurations
    ├── jwt/            # JWT utilities & filters
    ├── exception/      # Global error handling
    └── jpa/            # BaseTimeEntity
```

## Key Components

### Authentication Flow
1. **Local Login**: Email/password -> JWT tokens issued
2. **OAuth2 Login**: Provider redirect -> User profile -> JWT tokens
3. **Token Refresh**: Refresh token -> New access/refresh tokens

### Security
- JWT access token (short-lived)
- Refresh token stored in database
- `@CurrentUser` annotation for injecting logged-in user

### Entity Relationships
```
User
├── Post (1:N)
├── Comment (1:N)
├── RefreshToken (1:1)
└── UserTerms (1:N)

Post
└── Comment (1:N)

Terms
└── TermsHistory (1:N)
```

## Configuration

### application.yml (development)
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/wongoo3
    username: root
    password: password
  jpa:
    hibernate:
      ddl-auto: update
```

### application-test.yml (testing)
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
  jpa:
    hibernate:
      ddl-auto: create-drop
```

## Commands

```bash
# Build
./gradlew build

# Run tests
./gradlew test

# Start server
./gradlew bootRun

# Clean build
./gradlew clean build
```
