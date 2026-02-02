# Wongoo Board

Spring Boot + React 기반의 게시판 웹 애플리케이션입니다.

## 프로젝트 소개

학원 과정에서 기초 JDBC를 사용해 CRUD를 구현하고,
프레임워크 도움 없이 **자바 코드로 직접 서블릿을 등록하며 웹 요청·응답 흐름을 경험**해본 이후,
페이징, 조회 조건 처리 등 기본적인 웹 기능들을 하나씩 직접 구현해왔습니다.

이 프로젝트는 그러한 기초 학습 경험과
이후 쌓은 **실무 경험 및 학습한 Spring 기반 기술들을 바탕으로**,
웹 서비스의 구조와 흐름을 다시 한 번 정리하며 새로 만들어보는 개인 웹 프로젝트입니다.

## 주요 기능

- **회원 관리**: 회원가입, 로그인, 마이페이지
- **게시글**: 작성, 조회, 수정, 삭제 (페이지네이션)
- **댓글**: 게시글에 댓글 작성, 수정, 삭제
- **인증**: JWT 기반 인증, OAuth2 소셜 로그인 (네이버)

## 기술 스택

### Backend
- Java 21
- Spring Boot 3.4
- Spring Security + JWT
- Spring Data JPA + QueryDSL
- MySQL / H2 (테스트)

### Frontend
- React 19 + TypeScript
- Tailwind CSS
- React Router v7
- Zustand (상태 관리)
- Axios
- Vite

### Infra / DevOps
- AWS EC2
- Nginx
- Docker
- Jenkins

## 프로젝트 구조

```
wongoo3/
├── backend/          # Spring Boot API 서버
│   ├── src/
│   ├── build.gradle.kts
│   └── gradlew
├── frontend/         # React 클라이언트
│   ├── src/
│   ├── package.json
│   └── vite.config.ts
├── CLAUDE.md         # 개발 가이드
└── README.md
```

## 실행 방법

### 사전 요구사항
- Java 21+
- Node.js 18+
- MySQL 8.0+

### Backend 실행

```bash
cd backend

# 데이터베이스 설정 (application.yml 수정 필요)
# MySQL: localhost:3306/wongoo3

# 실행
./gradlew bootRun
```

### Frontend 실행

```bash
cd frontend

# 의존성 설치
npm install

# 개발 서버 실행
npm run dev
```

### 접속

- Frontend: http://localhost:5173
- Backend API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui/index.html

## 화면 구성

| 페이지 | 경로 | 설명 |
|--------|------|------|
| 홈 | `/` | 게시글 목록 |
| 로그인 | `/login` | 이메일/비밀번호 로그인 |
| 회원가입 | `/signup` | 신규 회원 등록 |
| 게시글 상세 | `/post/:id` | 게시글 내용 + 댓글 |
| 글쓰기 | `/post/write` | 새 게시글 작성 |
| 글수정 | `/post/edit/:id` | 게시글 수정 |
| 마이페이지 | `/mypage` | 내 정보 관리 |

## API 문서

Swagger UI에서 전체 API 문서를 확인할 수 있습니다:
http://localhost:8080/swagger-ui/index.html

## 라이선스

MIT License
