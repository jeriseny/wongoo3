# wongoo3

커뮤니티 게시판 (Spring Boot 3.4 + React 19)

## 실행

```bash
cd backend && ./gradlew bootRun   # :8080
cd frontend && npm run dev        # :5173
```

## 구조

```
backend/   → Java 21, Spring Security, JPA, MySQL
frontend/  → TypeScript, Tailwind, Zustand, Axios
```

## 주요 도메인

| 도메인 | 설명 |
|--------|------|
| auth | 로그인, OAuth2 (Naver) |
| user | 회원가입, 정보수정 |
| board | 게시판 (notice, free, qna) |
| post | 게시글 CRUD, 좋아요 |
| comment | 댓글 CRUD |

## API

| Endpoint | 설명 |
|----------|------|
| `GET /api/post?boardSlug=free` | 게시글 목록 |
| `POST /api/post` | 게시글 작성 |
| `POST /api/post/{id}/like` | 좋아요 토글 |
| `POST /api/auth/login/local` | 로그인 |

## 참고

- [docs/api.md](docs/api.md) - API 명세
- [docs/backend.md](docs/backend.md) - 백엔드
- [docs/frontend.md](docs/frontend.md) - 프론트엔드
