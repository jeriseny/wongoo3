# CLAUDE.md

This file provides guidance to Claude Code when working with code in this repository.

## Project Overview

Monorepo containing a Spring Boot backend and React frontend for a community board application.

```
wongoo3/
├── backend/     # Spring Boot 3.4 / Java 21
├── frontend/    # React 19 + TypeScript + Tailwind
└── docs/        # Detailed documentation
```

## Quick Commands

### Backend
```bash
cd backend
./gradlew build      # Build
./gradlew test       # Run tests
./gradlew bootRun    # Start server (port 8080)
```

### Frontend
```bash
cd frontend
npm install          # Install deps
npm run dev          # Dev server (port 5173)
npm run build        # Production build
npm run lint         # Lint
```

## Package Structure

### Backend
```
org.wongoo.wongoo3/
├── domain/
│   ├── auth/      # Authentication, OAuth2
│   ├── user/      # User management
│   ├── post/      # Posts CRUD
│   ├── comment/   # Comments CRUD
│   ├── token/     # JWT tokens
│   └── terms/     # Terms of service
│       └── history/
└── global/
    ├── config/    # Configurations
    ├── jwt/       # JWT utilities
    └── exception/ # Error handling
```

### Frontend
```
frontend/src/
├── api/           # Axios client
├── components/
│   └── common/    # Reusable components
├── pages/         # Route components
├── stores/        # Zustand stores
├── utils/         # Utility functions
└── types/         # TypeScript types
```

## Key API Endpoints

- `POST /api/auth/login/local` - Login
- `GET /api/user/info` - Get user info
- `GET /api/post` - List posts
- `POST /api/post` - Create post
- `GET /api/post/{id}/comment` - List comments

## Documentation

See `/docs` for detailed documentation:
- `api.md` - Full API reference
- `backend.md` - Backend architecture
- `frontend.md` - Frontend architecture
- `development.md` - Development guide
