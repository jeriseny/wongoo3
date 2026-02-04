# API Reference

Base URL: `/api`

## Auth

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/auth/login/local` | - | 로그인 |
| POST | `/auth/reissue` | - | 토큰 재발급 |
| GET | `/oauth2/authorization/naver` | - | 네이버 OAuth |

## User

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/user/signup` | - | 회원가입 |
| GET | `/user/info` | O | 내 정보 |
| PATCH | `/user/info` | O | 정보 수정 |

## Board

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/board` | - | 게시판 목록 |
| GET | `/board/{slug}` | - | 게시판 상세 |

## Post

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/post` | - | 게시글 목록 |
| GET | `/post/{id}` | - | 게시글 상세 |
| POST | `/post` | O | 게시글 작성 |
| PATCH | `/post/{id}` | Author | 게시글 수정 |
| DELETE | `/post/{id}` | Author | 게시글 삭제 |
| GET | `/post/{id}/like` | - | 좋아요 상태 |
| POST | `/post/{id}/like` | O | 좋아요 토글 |

**Query Params**: `boardSlug`, `page`, `size`, `searchType`, `keyword`, `sortBy`

## Comment

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/post/{postId}/comment` | - | 댓글 목록 |
| POST | `/post/{postId}/comment` | O | 댓글 작성 |
| PATCH | `/comment/{id}` | Author | 댓글 수정 |
| DELETE | `/comment/{id}` | Author | 댓글 삭제 |

## Error

| Status | Code |
|--------|------|
| 400 | BAD_REQUEST |
| 401 | UNAUTHORIZED |
| 403 | FORBIDDEN |
| 404 | NOT_FOUND |
