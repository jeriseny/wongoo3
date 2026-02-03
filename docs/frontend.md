# Frontend Architecture

## Tech Stack

- React 19 + TypeScript
- Tailwind CSS
- React Router v7
- Zustand (상태관리)
- Axios
- Vite

## Structure

```
frontend/src/
├── api/
│   └── client.ts           # Axios 인스턴스, API 모듈
├── components/
│   ├── common/             # 공통 컴포넌트
│   │   ├── LoadingSpinner.tsx
│   │   ├── ErrorAlert.tsx
│   │   └── OAuthButtons.tsx
│   ├── Header.tsx
│   ├── Layout.tsx
│   ├── ProtectedRoute.tsx
│   ├── PostCard.tsx
│   └── Pagination.tsx
├── pages/
│   ├── Board.tsx           # 게시판 (/, /board, /board/:slug)
│   ├── PostDetail.tsx      # 게시글 상세
│   ├── PostWrite.tsx       # 글 작성/수정
│   ├── Login.tsx
│   ├── Signup.tsx
│   ├── MyPage.tsx
│   └── OAuthCallback.tsx
├── stores/
│   └── authStore.ts        # 인증 상태 (Zustand)
├── utils/
│   ├── formatDate.ts
│   └── tokenManager.ts
├── types/
│   └── index.ts            # TypeScript 인터페이스
└── App.tsx                 # 라우터 설정
```

## Routes

| Path | Component | Auth | Description |
|------|-----------|------|-------------|
| `/` | Board | - | 전체 게시글 |
| `/board` | Board | - | 전체 게시글 |
| `/board/:slug` | Board | - | 게시판별 게시글 |
| `/post/:id` | PostDetail | - | 게시글 상세 |
| `/post/write` | PostWrite | Yes | 글 작성 |
| `/post/edit/:id` | PostWrite | Yes | 글 수정 |
| `/login` | Login | - | 로그인 |
| `/signup` | Signup | - | 회원가입 |
| `/mypage` | MyPage | Yes | 마이페이지 |

## API Modules

```typescript
boardApi.getList()              // 게시판 목록
postApi.getList(page, size, boardSlug)  // 게시글 목록
postApi.create({ title, content, boardSlug })
commentApi.getList(postId, page, size)
authApi.login({ email, password })
userApi.getMyInfo()
```

## Commands

```bash
npm install       # 의존성 설치
npm run dev       # 개발 서버 (5173)
npm run build     # 프로덕션 빌드
npm run lint      # ESLint
```

## Vite Proxy

```typescript
// vite.config.ts
server: {
  proxy: {
    '/api': 'http://localhost:8080'
  }
}
```
