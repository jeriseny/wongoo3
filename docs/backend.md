# Backend Architecture

## Tech Stack

- Java 21 + Spring Boot 3.4
- Spring Security + JWT + OAuth2
- Spring Data JPA + MySQL
- Gradle

## Package Structure

```
org.wongoo.wongoo3/
├── domain/
│   ├── auth/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── dto/
│   │   └── oauth2/
│   │       ├── provider/     # OAuth2 프로바이더 (Naver)
│   │       ├── dto/
│   │       └── registry/
│   ├── user/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── dto/
│   │   └── repository/
│   ├── board/                # 게시판
│   │   ├── controller/
│   │   ├── service/
│   │   ├── dto/
│   │   └── repository/
│   ├── post/                 # 게시글
│   │   ├── controller/
│   │   ├── service/
│   │   ├── dto/
│   │   └── repository/
│   ├── comment/
│   ├── token/
│   └── terms/
└── global/
    ├── config/               # Security, CORS, RestClient
    ├── jwt/                  # JWT 파싱, 생성, 필터
    ├── security/oauth2/      # OAuth2 핸들러
    ├── exception/            # 예외 처리
    └── jpa/                  # BaseTimeEntity
```

## Entity Relationships

```
Board (1) ─── (N) Post (1) ─── (N) Comment
                    │
User (1) ──────────(N)
```

## Configuration

### Profiles

| Profile | 용도 | 설정 파일 |
|---------|-----|----------|
| local | 로컬 개발 | application-local.yml |
| prod | 프로덕션/Docker | application-prod.yml |

### application.yml (공통)
```yaml
spring:
  profiles:
    active: local
  jpa:
    hibernate:
      ddl-auto: none
```

### application-local.yml
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/wongoo3
app:
  frontend-url: http://localhost:5173
```

## Commands

```bash
./gradlew bootRun          # 서버 실행
./gradlew build            # 빌드
./gradlew test             # 테스트
```

## Docker

```bash
docker-compose up -d --build
```

`SPRING_PROFILES_ACTIVE=local` 환경변수로 프로필 선택
