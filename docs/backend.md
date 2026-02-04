# Backend

Java 21 + Spring Boot 3.4 + MySQL

## Package

```
org.wongoo.wongoo3/
├── domain/
│   ├── auth/        # 인증, OAuth2
│   ├── user/        # 사용자
│   ├── board/       # 게시판
│   ├── post/        # 게시글, 좋아요
│   ├── comment/     # 댓글
│   ├── token/       # RefreshToken
│   └── terms/       # 약관
└── global/
    ├── config/      # Security, CORS
    ├── jwt/         # JWT 처리
    ├── security/    # OAuth2 핸들러
    └── exception/   # 예외 처리
```

## Entity

```
Board (1) ─── (N) Post (1) ─── (N) Comment
                   │
User (1) ─────────(N)
                   │
              PostLike
```

## Tables

`WK_USER`, `WK_BOARD`, `WK_POST`, `WK_COMMENT`, `WK_POST_LIKE`, `WK_TERMS`, `WK_REFRESH_TOKEN`

## Profile

| Profile | 용도 |
|---------|-----|
| local | 로컬 개발 (기본) |
| prod | Docker/프로덕션 |

## Commands

```bash
./gradlew bootRun    # 실행
./gradlew build      # 빌드
./gradlew test       # 테스트
```
