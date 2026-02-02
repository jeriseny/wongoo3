# API Reference

## Authentication

### Login
- **POST** `/api/auth/login/local`
- Request: `{ email, password, rememberMe }`
- Response: `{ accessToken, refreshToken, accessTokenExpiresIn, refreshTokenExpiresIn }`

### Token Reissue
- **POST** `/api/auth/reissue`
- Request: `{ token, rememberMe }`
- Response: `{ accessToken, refreshToken, ... }`

### OAuth2 Login
- **GET** `/api/oauth2/authorization/{provider}`
- Providers: `naver`, `kakao`, `google`
- Redirects to provider login page, then callback with tokens

---

## User

### Sign Up
- **POST** `/api/user/signup`
- Request: `{ email, nickname, phoneNumber, password, termsAgreeList }`
- Response: User ID

### Get User Info
- **GET** `/api/user/info`
- Headers: `Authorization: Bearer {token}`
- Response: `{ email, nickName, phoneNumber, isSocial, providerType, createdAt }`

### Update User Info
- **PATCH** `/api/user/info`
- Headers: `Authorization: Bearer {token}`
- Request: `{ nickname, phoneNumber }`

### Change Password
- **POST** `/api/user/change-password`
- Headers: `Authorization: Bearer {token}`
- Params: `currentPassword`, `newPassword`

---

## Posts

### List Posts
- **GET** `/api/post`
- Params: `page` (default 0), `size` (default 10)
- Response: Paginated list of posts

### Get Post
- **GET** `/api/post/{id}`
- Response: `{ id, title, content, authorNickname, viewCount, createdAt, updatedAt }`

### Create Post
- **POST** `/api/post`
- Headers: `Authorization: Bearer {token}`
- Request: `{ title, content }`
- Response: Created post

### Update Post
- **PATCH** `/api/post/{id}`
- Headers: `Authorization: Bearer {token}`
- Request: `{ title, content }`
- Response: Updated post

### Delete Post
- **DELETE** `/api/post/{id}`
- Headers: `Authorization: Bearer {token}`

---

## Comments

### List Comments
- **GET** `/api/post/{postId}/comment`
- Params: `page`, `size`
- Response: Paginated list of comments

### Create Comment
- **POST** `/api/post/{postId}/comment`
- Headers: `Authorization: Bearer {token}`
- Request: `{ content }`
- Response: Created comment

### Update Comment
- **PATCH** `/api/comment/{id}`
- Headers: `Authorization: Bearer {token}`
- Request: `{ content }`
- Response: Updated comment

### Delete Comment
- **DELETE** `/api/comment/{id}`
- Headers: `Authorization: Bearer {token}`

---

## Error Responses

| HTTP Status | Error Code | Description |
|-------------|------------|-------------|
| 400 | BAD_REQUEST | Invalid request |
| 401 | UNAUTHORIZED | Authentication required |
| 403 | FORBIDDEN | Access denied |
| 404 | NOT_FOUND | Resource not found |
| 409 | CONFLICT | Duplicate resource |
