# API Reference

Base URL: `/api`

## Board

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/board` | - | 게시판 목록 |
| GET | `/board/{slug}` | - | 게시판 상세 |
| POST | `/board` | Admin | 게시판 생성 |

### Response
```json
{ "id": 1, "name": "자유게시판", "slug": "free", "description": "..." }
```

---

## Post

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/post` | - | 게시글 목록 |
| GET | `/post?boardSlug=free` | - | 게시판별 목록 |
| GET | `/post/{id}` | - | 게시글 상세 |
| POST | `/post` | User | 게시글 작성 |
| PATCH | `/post/{id}` | Author | 게시글 수정 |
| DELETE | `/post/{id}` | Author | 게시글 삭제 |

### Create Request
```json
{ "title": "제목", "content": "내용", "boardSlug": "free" }
```

### Response
```json
{
  "id": 1,
  "title": "제목",
  "content": "내용",
  "authorNickname": "작성자",
  "boardSlug": "free",
  "boardName": "자유게시판",
  "viewCount": 0,
  "createdAt": "2024-01-01T00:00:00"
}
```

---

## Comment

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/post/{postId}/comment` | - | 댓글 목록 |
| POST | `/post/{postId}/comment` | User | 댓글 작성 |
| PATCH | `/comment/{id}` | Author | 댓글 수정 |
| DELETE | `/comment/{id}` | Author | 댓글 삭제 |

---

## Auth

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/auth/login/local` | 로그인 |
| POST | `/auth/reissue` | 토큰 재발급 |
| GET | `/oauth2/authorization/naver` | 네이버 OAuth |

### Login Request
```json
{ "email": "test@test.com", "password": "password", "rememberMe": false }
```

### Token Response
```json
{
  "accessToken": "...",
  "refreshToken": "...",
  "accessTokenExpiresIn": 1800000,
  "refreshTokenExpiresIn": 1209600000
}
```

---

## User

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/user/signup` | - | 회원가입 |
| GET | `/user/info` | User | 내 정보 |
| PATCH | `/user/info` | User | 정보 수정 |
| POST | `/user/change-password` | User | 비밀번호 변경 |

---

## Error Response

```json
{ "code": "NOT_FOUND", "message": "리소스를 찾을 수 없습니다" }
```

| Status | Code | Description |
|--------|------|-------------|
| 400 | BAD_REQUEST | 잘못된 요청 |
| 401 | UNAUTHORIZED | 인증 필요 |
| 403 | FORBIDDEN | 권한 없음 |
| 404 | NOT_FOUND | 리소스 없음 |
