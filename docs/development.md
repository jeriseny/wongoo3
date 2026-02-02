# Development Guide

## Prerequisites
- Java 21
- Node.js 18+
- MySQL 8.0
- Gradle (or use wrapper)

## Quick Start

### 1. Database Setup
```bash
# Create MySQL database
mysql -u root -p
CREATE DATABASE wongoo3;
```

### 2. Backend
```bash
cd backend

# Configure database (edit application.yml if needed)
# Run server
./gradlew bootRun
```
Server starts at `http://localhost:8080`

### 3. Frontend
```bash
cd frontend

npm install
npm run dev
```
Dev server starts at `http://localhost:5173`

## Running Both Servers

```bash
# Terminal 1: Backend
cd backend && ./gradlew bootRun

# Terminal 2: Frontend
cd frontend && npm run dev
```

## Testing

### Backend Tests
```bash
cd backend
./gradlew test                    # All tests
./gradlew test --tests "AuthServiceTest"  # Specific test
```

### Frontend Build Check
```bash
cd frontend
npm run build   # TypeScript compilation
npm run lint    # ESLint check
```

## Common Development Tasks

### Adding a New API Endpoint

1. **Backend**
   - Create/update DTO in `domain/{feature}/dto/`
   - Add method in `domain/{feature}/service/`
   - Add endpoint in `domain/{feature}/controller/`

2. **Frontend**
   - Add type in `types/index.ts`
   - Add API call in `api/client.ts`
   - Use in component

### Adding a New Page

1. Create component in `frontend/src/pages/`
2. Add route in `App.tsx`
3. Wrap with `ProtectedRoute` if auth required

### Environment Variables

**Backend** (`application.yml`):
- Database connection
- JWT secret/expiration
- OAuth2 client credentials

**Frontend** (`.env`):
- API base URL (if not using proxy)

## Debugging

### Backend Logs
Check console output from `./gradlew bootRun`

### Frontend
- Browser DevTools Console
- React DevTools extension
- Network tab for API calls

## Code Style

### Backend
- Follow existing package structure
- Use Lombok annotations
- Services handle business logic
- Controllers are thin (validation + delegation)

### Frontend
- Functional components with hooks
- TypeScript strict mode
- Tailwind for styling
- Extract reusable components to `components/common/`
