# CLAUDE.md

## Project Overview

커뮤니티 게시판 애플리케이션 (Spring Boot + React)

```
wongoo3/
├── backend/     # Spring Boot 3.4 / Java 21
├── frontend/    # React 19 + TypeScript + Tailwind
└── docs/        # 상세 문서
```

## Quick Commands

```bash
# Backend
cd backend && ./gradlew bootRun

# Frontend
cd frontend && npm run dev

# Docker (local)
docker-compose up -d --build
```

## Backend Structure

```
org.wongoo.wongoo3/
├── domain/
│   ├── auth/        # 인증, OAuth2
│   ├── user/        # 사용자
│   ├── board/       # 게시판
│   ├── post/        # 게시글
│   ├── comment/     # 댓글
│   ├── token/       # JWT 토큰
│   └── terms/       # 약관
└── global/
    ├── config/      # 설정
    ├── jwt/         # JWT 유틸
    ├── security/    # OAuth2 핸들러
    └── exception/   # 예외 처리
```

## Frontend Structure

```
frontend/src/
├── api/           # API 클라이언트
├── components/    # 컴포넌트
├── pages/         # 페이지
├── stores/        # Zustand 스토어
├── utils/         # 유틸리티
└── types/         # TypeScript 타입
```

## Key API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/board` | 게시판 목록 |
| GET | `/api/post?boardSlug=free` | 게시글 목록 (게시판별) |
| POST | `/api/post` | 게시글 작성 |
| GET | `/api/post/{id}` | 게시글 상세 |
| POST | `/api/auth/login/local` | 로그인 |
| GET | `/api/oauth2/authorization/naver` | 네이버 OAuth |

## Profiles

- `local`: 로컬 개발 (기본값)
- `prod`: 프로덕션/Docker

## Documentation

- [docs/api.md](docs/api.md) - API 레퍼런스
- [docs/backend.md](docs/backend.md) - 백엔드 구조
- [docs/frontend.md](docs/frontend.md) - 프론트엔드 구조
