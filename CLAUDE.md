# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Structure (Monorepo)

```
wongoo3/
├── backend/      # Spring Boot 3.4 / Java 21 API Server
├── frontend/     # React + TypeScript (Vite)
├── CLAUDE.md     # This file
└── README.md
```

## Backend (Spring Boot)

### Build & Test Commands

```bash
cd backend

# Build (compiles and generates QueryDSL classes)
./gradlew build

# Run tests (uses H2 in-memory database with test profile)
./gradlew test

# Run a single test class
./gradlew test --tests "org.wongoo.wongoo3.domain.user.service.UserServiceTest"

# Start application (requires MySQL on localhost:3306)
./gradlew bootRun

# Clean generated sources
./gradlew clean
```

### Package Structure

```
org.wongoo.wongoo3/
├── domain/           # Business logic (feature-based organization)
│   ├── auth/         # Authentication (local login, OAuth2, token reissue)
│   ├── user/         # User registration, profile management
│   ├── post/         # Post CRUD (create, read, update, delete with pagination)
│   ├── comment/      # Comment CRUD (nested under posts)
│   ├── token/        # JWT token issuance and refresh token storage
│   ├── terms/        # Terms of service, user agreements, version history
│   └── file/         # File upload handling
└── global/           # Cross-cutting concerns
    ├── config/       # Spring configurations (Security, Web, QueryDSL, Swagger)
    ├── jwt/          # JWT utilities (JwtProvider, JwtParser, JwtAuthenticationFilter)
    ├── exception/    # GlobalExceptionHandler, WebErrorException, WebErrorCode
    ├── web/          # LoginUserArgumentResolver for @CurrentUser injection
    ├── jpa/          # BaseTimeEntity (createdAt/updatedAt)
    └── policy/       # Business validation policies
```

### Authentication Flow

**JWT Authentication:**
- `JwtAuthenticationFilter` extracts Bearer token from Authorization header
- `JwtParser` validates and parses claims (userId, email, role)
- `LoginUserArgumentResolver` injects `@CurrentUser LoginUser` into controller methods
- Access token: 30 min, Refresh token: 14 days

**OAuth2 (Naver implemented):**
- `OAuth2Provider` interface with `NaverOAuth2Provider` implementation
- `OAuth2ProviderRegistry` manages providers by `ProviderType` enum
- `OAuth2Service` orchestrates OAuth2 login flow

### Database

- **Production**: MySQL (localhost:3306/wongoo3)
- **Tests**: H2 in-memory with `@ActiveProfiles("test")` and `create-drop` schema

### API Endpoints

**Public (no auth required):**
- `POST /api/user/signup` - Local registration
- `POST /api/user/signup/social` - OAuth2 registration
- `POST /api/auth/**` - Login, token reissue, OAuth2 callbacks
- `GET /swagger-ui/**`, `/v3/api-docs/**` - API documentation

**Post API (auth required):**

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/post` | Create a new post |
| GET | `/api/post/{postId}` | Get post detail (increments view count) |
| GET | `/api/post?page=0&size=10` | Get paginated post list |
| PATCH | `/api/post/{postId}` | Update post (author only) |
| DELETE | `/api/post/{postId}` | Delete post (author only) |

**Comment API (auth required):**

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/post/{postId}/comment` | Create a comment on a post |
| GET | `/api/post/{postId}/comment?page=0&size=20` | Get paginated comments for a post |
| PATCH | `/api/comment/{commentId}` | Update comment (author only) |
| DELETE | `/api/comment/{commentId}` | Delete comment (author only) |

### Entity Relationships

```
User (1) ──── (N) Post (1) ──── (N) Comment
  │                                    │
  └────────────── (N) ─────────────────┘
```

---

## Frontend (React + TypeScript)

### Build & Dev Commands

```bash
cd frontend

# Install dependencies
npm install

# Start development server (default: http://localhost:5173)
npm run dev

# Build for production
npm run build

# Preview production build
npm run preview

# Lint code
npm run lint
```

### Tech Stack

- **React 19** with TypeScript
- **Vite** for build tooling
- **ESLint** for code quality

### Project Structure

```
frontend/
├── public/           # Static assets
├── src/
│   ├── assets/       # Images, fonts, etc.
│   ├── App.tsx       # Main application component
│   └── main.tsx      # Entry point
├── index.html
├── vite.config.ts
└── package.json
```

---

## Development Notes

- Backend runs on `http://localhost:8080`
- Frontend runs on `http://localhost:5173`
- For API calls from frontend, configure CORS or use Vite proxy
