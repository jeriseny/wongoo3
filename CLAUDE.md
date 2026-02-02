# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Structure (Monorepo)

```
wongoo3/
├── backend/      # Spring Boot 3.4 / Java 21 API Server
├── frontend/     # React 19 + TypeScript + Tailwind CSS
├── CLAUDE.md     # This file
└── README.md
```

---

## Backend (Spring Boot)

### Build & Test Commands

```bash
cd backend

# Build (compiles and generates QueryDSL classes)
./gradlew build

# Run tests (uses H2 in-memory database with test profile)
./gradlew test

# Start application (requires MySQL on localhost:3306)
./gradlew bootRun
```

### Package Structure

```
org.wongoo.wongoo3/
├── domain/
│   ├── auth/         # Authentication (login, OAuth2, token)
│   ├── user/         # User management
│   ├── post/         # Post CRUD
│   ├── comment/      # Comment CRUD
│   ├── token/        # JWT token storage
│   ├── terms/        # Terms of service
│   └── file/         # File upload
└── global/
    ├── config/       # Spring configurations
    ├── jwt/          # JWT utilities
    ├── exception/    # Error handling
    └── jpa/          # BaseTimeEntity
```

### API Endpoints

**Auth:**
- `POST /api/auth/login/local` - Login with email/password
- `POST /api/auth/reissue` - Refresh token

**User:**
- `POST /api/user/signup` - Register
- `POST /api/user/info` - Get my info
- `PATCH /api/user/info` - Update info

**Post:**
- `GET /api/post` - List posts (paginated)
- `GET /api/post/{id}` - Get post detail
- `POST /api/post` - Create post
- `PATCH /api/post/{id}` - Update post
- `DELETE /api/post/{id}` - Delete post

**Comment:**
- `GET /api/post/{id}/comment` - List comments
- `POST /api/post/{id}/comment` - Create comment
- `PATCH /api/comment/{id}` - Update comment
- `DELETE /api/comment/{id}` - Delete comment

---

## Frontend (React + TypeScript)

### Build & Dev Commands

```bash
cd frontend

npm install          # Install dependencies
npm run dev          # Start dev server (http://localhost:5173)
npm run build        # Build for production
npm run lint         # Lint code
```

### Tech Stack

- **React 19** + TypeScript
- **Tailwind CSS** - Styling
- **React Router v7** - Routing
- **Zustand** - State management
- **Axios** - HTTP client
- **Vite** - Build tool

### Project Structure

```
frontend/src/
├── api/
│   └── client.ts           # Axios instance with interceptors
├── components/
│   ├── Header.tsx          # Navigation bar
│   ├── Layout.tsx          # Page layout
│   ├── ProtectedRoute.tsx  # Auth guard
│   ├── PostCard.tsx        # Post list item
│   ├── CommentItem.tsx     # Comment item
│   └── Pagination.tsx      # Pagination component
├── pages/
│   ├── Home.tsx            # Post list (/)
│   ├── Login.tsx           # Login page (/login)
│   ├── Signup.tsx          # Register page (/signup)
│   ├── PostDetail.tsx      # Post detail (/post/:id)
│   ├── PostWrite.tsx       # Write/Edit (/post/write, /post/edit/:id)
│   └── MyPage.tsx          # My page (/mypage)
├── stores/
│   └── authStore.ts        # Auth state (Zustand)
├── types/
│   └── index.ts            # TypeScript types
├── App.tsx                 # Router setup
├── main.tsx                # Entry point
└── index.css               # Tailwind import
```

### Routes

| Path | Page | Auth Required |
|------|------|---------------|
| `/` | Home (Post list) | No |
| `/login` | Login | No |
| `/signup` | Sign up | No |
| `/post/:id` | Post detail | No |
| `/post/write` | Write post | Yes |
| `/post/edit/:id` | Edit post | Yes |
| `/mypage` | My page | Yes |

### API Proxy

Vite dev server proxies `/api/*` to `http://localhost:8080` (backend).

---

## Development

### Run Both Servers

```bash
# Terminal 1: Backend
cd backend && ./gradlew bootRun

# Terminal 2: Frontend
cd frontend && npm run dev
```

### Test Flow

1. **회원가입** → `/signup`
2. **로그인** → `/login`
3. **게시글 작성** → `/post/write`
4. **게시글 조회** → `/post/{id}`
5. **댓글 작성** → Post detail page
6. **마이페이지** → `/mypage`
