# Wongoo Board

Spring Boot + React 기반의 커뮤니티 게시판 웹 애플리케이션

## Tech Stack

**Backend**: Java 21, Spring Boot 3.4, Spring Security, JWT, JPA, QueryDSL, MySQL
**Frontend**: React 19, TypeScript, Tailwind CSS, Zustand, Vite
**Infra**: AWS EC2, Nginx, Docker, Jenkins

## Quick Start

```bash
# Backend (requires MySQL on localhost:3306)
cd backend && ./gradlew bootRun

# Frontend
cd frontend && npm install && npm run dev
```

- Frontend: http://localhost:5173
- Backend API: http://localhost:8080
- Swagger: http://localhost:8080/swagger-ui/index.html

## Features

- User registration, login, profile management
- OAuth2 social login (Naver, Kakao, Google)
- Post CRUD with pagination
- Comments on posts
- JWT authentication with refresh tokens

## Documentation

See [`/docs`](./docs) for detailed documentation:
- [API Reference](./docs/api.md)
- [Backend Architecture](./docs/backend.md)
- [Frontend Architecture](./docs/frontend.md)
- [Development Guide](./docs/development.md)

## License

MIT License
